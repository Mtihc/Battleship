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
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.models.Tile;
import com.mtihc.battleship.views.BoardView;
import com.mtihc.battleship.views.GameView;
import com.mtihc.battleship.views.GameView.GameViewSide;

public class GamePlayer {

	private OfflinePlayer player;
	GameController controller;
	GameViewSide view;

	public GamePlayer(OfflinePlayer player) {
		this.player = player;
	}

	public String getName() {
		return player.getName();
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	public GameController getController() {
		return controller;
	}
	
	public GameView.GameViewSide getView() {
		return view;
	}

	protected void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		event.setUseItemInHand(Result.DENY);
		event.setCancelled(true);
		
		if(!controller.areAllShipsPlaced(view.getBoard())) {
			// still in setup mode, TODO use GameState instead?
			
			Block block = event.getClickedBlock();
			BlockFace facing = BoardView.yawToFace(event.getPlayer().getLocation().getYaw());
			ItemStack item = event.getItem();
			Board board = view.getBoard();
			BoardView boardView = view.getInteractiveView();
			Tile tile = boardView.getLocationStrategy().locationToTile(boardView, block.getLocation());
			if(tile == null) {
				// clicked outside of the board
				return;
			}
			if(tile.hasShip()) {
				// there's already a ship
				return;
			}
			
			Ship[] ships = board.getShips();
			for (int i = 0; i < ships.length; i++) {
				Ship ship = ships[i];
				
				if(ship.getType().isSimilar(item) && !ship.isPlaced()) {
					// placing a ship
					
					Bukkit.getLogger().info("clicked tile x:" + tile.getX() + ", y:" + tile.getY());
					
					int n = ship.getSize();
					Tile[] tiles = new Tile[n];
					tiles[0] = tile;
					Block tileBlock = block;
					Tile next;
					for (int j = 1; j < n; j++) {
						tileBlock = tileBlock.getRelative(facing);
						next = boardView.getLocationStrategy().locationToTile(boardView, tileBlock.getLocation());
						if(next == null) {
							// ship does not fit on the board
							return;
						}
						tiles[j] = next;
					}
					try {
						ship.place(tiles);
					} catch (Exception e) {
						// failed to place ship
						return;
					}
					// TODO remove ship items from player's inventory
					//event.getPlayer().getInventory().remove(new ItemStack(item.getTypeId(), n, item.getData().getData()));
					
					// break the loop, since we found a ship that matches the item in your hand
					break;
				}
			}
			
			// TODO place part of the ship (or entire ship using facing direction of player?)
		}
		else {
			// not in setup mode, must be battling
			
			// TODO place TNT
		}
	}

}
