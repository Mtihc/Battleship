package com.mtihc.battleship.controllers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Board.Ship;
import com.mtihc.battleship.models.Board.Tile;
import com.mtihc.battleship.models.GamePlayer;
import com.mtihc.battleship.views.BoardView;
import com.mtihc.battleship.views.GameView;
import com.mtihc.battleship.views.GameView.GameSideView;

public class GamePlayerController {

	private GameController controller;
	private GamePlayer player;
	private GameSideView view;
	GamePlayerController enemy;

	GamePlayerController(GameController controller, GamePlayer player, GameSideView view) {
		this.controller = controller;
		this.player = player;
		this.view = view;
	}

	public GameController getController() {
		return controller;
	}

	public GamePlayer getGamePlayer() {
		return player;
	}
	
	public OfflinePlayer getPlayer() {
		return player.getPlayer();
	}
	
	public GameSideView getGameSideView() {
		return view;
	}
	
	public GamePlayerController getEnemyController() {
		return enemy;
	}

	protected void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(!controller.areAllShipsPlaced(player.getBoard())) {
				placeShip(event);
			}
		}
		else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

		}
		else {
			return;
		}

		event.setUseInteractedBlock(Result.DENY);
		event.setUseItemInHand(Result.DENY);
		event.setCancelled(true);

	}

	private void placeShip(PlayerInteractEvent event) {
		// still in setup mode, because not all ships are placed
		BlockFace facing = GameView.yawToFace(event.getPlayer().getLocation().getYaw());
		Board board = player.getBoard();
		BoardView boardView = view.getInteractiveView();
		Tile tile = boardView.locationToTile(event.getClickedBlock().getLocation());
		
		StringBuilder string = new StringBuilder();
		string.append("player " + event.getPlayer().getName() + " clicked");
		
		if(tile == null) {
			// clicked outside of the board
			string.append(" outside the board");
			return;
		}
		string.append(" inside the board");
		Bukkit.getLogger().info("");
		if(tile.hasShip()) {
			// there's already a ship
			string.append(", but there's already a ship.");
			return;
		}
		string.append(", and there's no ship yet.");
		controller.getPlugin().getLogger().info(string.toString());
		
		Ship[] ships = board.getShips();
		for (int i = 0; i < ships.length; i++) {
			Ship ship = ships[i];

			Bukkit.getLogger().info("Ship similar: " + ship.getType().isSimilar(event.getItem()));
			Bukkit.getLogger().info("Ship placed: " + ship.isPlaced());
			if(!ship.getType().isSimilar(event.getItem()) || ship.isPlaced()) {
				// different type of ship, or it's already placed,
				// continue to try the next ship
				continue;
			}
			// placing a ship


			if(!placeShip(ship, tile, event.getClickedBlock(), facing)) {
				// couldn't place this ship
				// continue to try the next ship
				Bukkit.getLogger().info("Ship cant be placed");
				continue;
			}
			Bukkit.getLogger().info("Ship was placed!");

			// remove ship items from player's inventory
			int amountToRemove = ship.getSize();
			ItemStack itemToRemove = ship.getType().getNormal().toItemStack();
			for (ItemStack item : event.getPlayer().getInventory()) {
				if(itemToRemove.isSimilar(item)) {
					if (item.getAmount() > amountToRemove){
						item.setAmount(item.getAmount() - amountToRemove);
	                    amountToRemove = 0;
	                }else{
	                    amountToRemove -= item.getAmount();
	                    event.getPlayer().getInventory().remove(item);
	                }
				}
			}
			// TODO event.getPlayer().updateInventory();

			// break the loop, since we placed a ship
			break;
		}
	}

	private boolean placeShip(Ship ship, Tile clickedTile, Block clickedBlock, BlockFace facing) {
		BoardView boardView = view.getInteractiveView();
		Tile[] shipTiles = new Tile[ship.getSize()];
		shipTiles[0] = clickedTile;
		Block tileBlock = clickedBlock;
		Tile next;

		// find the other Tiles for the Ship
		for (int j = 1; j < ship.getSize(); j++) {
			tileBlock = tileBlock.getRelative(facing);
			next = boardView.locationToTile(tileBlock.getLocation());
			if(next == null) {
				// ship does not fit on the board
				Bukkit.getLogger().info("Ship does not fit on the board");
				return false;
			}
			else if(next.hasShip()) {
				// this tile already occupied by a ship
				Bukkit.getLogger().info("Tile is already occupied by a ship");
				return false;
			}
			shipTiles[j] = next;
		}
		ship.place(shipTiles);
		return true;
	}

}
