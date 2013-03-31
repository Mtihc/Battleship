package com.mtihc.battleship.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Board.Ship;
import com.mtihc.battleship.models.Board.Tile;
import com.mtihc.battleship.models.GamePlayer;
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
			if(!view.getGameBoard().areAllShipsPlaced()) {
				placeShip(event);
			}
			else /*if(view.getOtherSideView().getGameBoard().areAllShipsPlaced())*/ {
				// ships are placed on both sides
				placeBomb(event);
			}
		}
		else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if(!view.getGameBoard().areAllShipsPlaced()) {
				removeShip(event);
			}
			else if(view.getOtherSideView().getGameBoard().areAllShipsPlaced()) {
				// ships are placed on both sides
			}
		}
		else {
			return;
		}

		event.setUseInteractedBlock(Result.DENY);
		event.setUseItemInHand(Result.DENY);
		event.setCancelled(true);

	}

	private void placeShip(PlayerInteractEvent event) {
		Board board = player.getBoard();
		
		// 
		// Try to find a ship that matches the item in hand
		// 
		Ship[] ships = board.getShips();
		Ship ship = null;
		for (int i = 0; i < ships.length; i++) {
			Ship next = ships[i];
			if(!next.getType().isSimilar(event.getItem()) || next.isPlaced()) {
				// different type of ship, or it's already placed,
				// continue to try the next ship
				continue;
			}
			ship = next;
			break;
		}
		if(ship == null) {
			// not holding a ship that isn't placed yet 
			return;
		}
		
		// 
		// Which tile was clicked?
		//
		Block tileBlock = event.getClickedBlock();
		Tile clickedTile = view.getInteractiveView().locationToTile(tileBlock.getLocation());
		if(clickedTile == null) {
			// can only place ships on the board
			return;
		}
		if(clickedTile.hasShip()) {
			// there's already a ship on this tile
			return;
		}

		// 
		// Find the other Tiles for the Ship
		// 
		Tile[] shipTiles = new Tile[ship.getSize()];
		shipTiles[0] = clickedTile;
		Tile next;
		BlockFace face = GameView.yawToFace(event.getPlayer().getLocation().getYaw());
		for (int j = 1; j < ship.getSize(); j++) {
			tileBlock = tileBlock.getRelative(face);
			next = view.getInteractiveView().locationToTile(tileBlock.getLocation());
			if(next == null) {
				// ship does not fit on the board
				return ;
			}
			else if(next.hasShip()) {
				// the ship would overlap with another ship
				return;
			}
			shipTiles[j] = next;
		}
		ship.place(shipTiles);

		// 
		// Remove ship items from player's inventory
		// 
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
		
		// 
		// Draw the ship
		// 
		for (int i = 0; i < shipTiles.length; i++) {
			Tile shipTile = shipTiles[i];
			view.getInteractiveView().draw(shipTile.getX(), shipTile.getY());
		}
		
		// 
		// Notify observers?
		// 
		// TODO onShipPlace(ship);
		
		// 
		// Move your ships to the vertical board
		// 
		if(player.getBoard().areAllShipsPlaced()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(controller.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					view.switchViews();
					// TODO onPlayerReady(GamePlayerController.this);
				}
			}, 60);
		}
		
	}

	private void removeShip(PlayerInteractEvent event) {
		// 
		// Which tile was clicked?
		// 
		Tile tile = view.getInteractiveView().locationToTile(event.getClickedBlock().getLocation());
		if(tile == null) {
			// not on the board
			return;
		}
		if(!tile.hasShip()) {
			// there's no ship to remove
			return;
		}
		
		Tile[] shipTiles = tile.getShip().getTiles();
		
		// 
		// Remove the ship
		// 
		Ship ship = tile.getShip();
		ship.remove();
		
		// 
		// Re-Draw the tiles where the ship was
		// 
		for (int i = 0; i < shipTiles.length; i++) {
			Tile shipTile = shipTiles[i];
			view.getInteractiveView().draw(shipTile.getX(), shipTile.getY());
		}
		
		
		// 
		// Give ship items back
		// 
		event.getPlayer().getInventory().addItem(ship.getType().getNormal().toItemStack(ship.getSize()));
		
		// 
		// Notify observers
		// 
		// TODO onShipRemove(ship, shipTiles);
	}
	
	private void placeBomb(PlayerInteractEvent event) {
		// 
		// Which tile was clicked?
		// 
		Tile tile = view.getInteractiveView().locationToTile(event.getClickedBlock().getLocation());
		if(tile == null) {
			// not on the board
			return;
		}
		// 
		// Which tile on the enemy's board does that correspond to?
		// 
		final Tile enemyTile = player.getBoard().getOtherBoard().getTile(tile.getX(), tile.getY());
		if(enemyTile.isHit()) {
			// tile was already hit before
			return;
		}
		
		// 
		// Play TNT effect
		// 
		Location loc = event.getClickedBlock().getLocation();
		loc.setY(loc.getY() + 1);
		loc.setX(loc.getX() + 0.5);
		loc.setZ(loc.getZ() + 0.5);
		TNTPrimed primedTnt = (TNTPrimed) loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
		primedTnt.setIsIncendiary(false);
		primedTnt.setYield(0);
		primedTnt.setVelocity(new Vector(0, 0.25, 0));
		primedTnt.setFuseTicks(40);
		
		// 
		// Schedule the hit for when the TNT explodes
		// 
		Bukkit.getScheduler().scheduleSyncDelayedTask(controller.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				// 
				// See method hit
				// 
				hit(enemyTile.getX(), enemyTile.getY());
			}
		}, primedTnt.getFuseTicks());
	}
	
	private void hit(int x, int y) {
		// TODO is it this players turn to attack?
		
		Board board = enemy.getGamePlayer().getBoard();
		Tile tile = board.getTile(x, y);

		// 
		// Update tile
		// 
		if(tile.isHit()) {
			// tile already hit
			return;
		}
		tile.setHit(true);
		
		// 
		// Draw tile
		// 
		enemy.getGameSideView().getProjectorView().draw(tile.getX(), tile.getY());
		view.getInteractiveView().draw(tile.getX(), tile.getY());
		
		// 
		// Notify observers?
		// 
		if(tile.hasShip()) {
			// TODO onHit
//			onHit(tile);
			Ship ship = tile.getShip();
			if(ship.isDestroyed()) {
				// TODO onShipDestroyed
//				onShipDestroyed(ship);
				
				if(board.areAllShipsDestroyed()) {
					// TODO onAllShipsDestroyed
//					onAllShipsDestroyed();
				}
			}
		}
		else {
			// TOODO onMiss
//			onMiss(tile);
		}
		
	}
	

}
