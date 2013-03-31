package com.mtihc.battleship.controllers;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.GamePlayer;
import com.mtihc.battleship.models.GamePlayerInventory;
import com.mtihc.battleship.models.ShipType;
import com.mtihc.battleship.views.GameView;

public class GameController {

	private JavaPlugin plugin;
	private Game game;
	private GameView view;
	private GamePlayerController leftPlayer;
	private GamePlayerController rightPlayer;

	public GameController(JavaPlugin plugin, Game game) {
		this.plugin = plugin;
		this.game = game;
		this.view = new GameView(game);
		
		GamePlayer leftPlayer = game.getLeftBoard().getGamePlayer();
		GamePlayer rightPlayer = game.getRightBoard().getGamePlayer();
		
		this.leftPlayer = new GamePlayerController(this, leftPlayer, view.getLeftSideView());
		this.rightPlayer = new GamePlayerController(this, rightPlayer, view.getRightSideView());
		this.leftPlayer.enemy = this.rightPlayer;
		this.rightPlayer.enemy = this.leftPlayer;
		
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Game getGame() {
		return game;
	}

	public GameView getGameView() {
		return view;
	}

	public GamePlayerController getLeftPlayerController() {
		return leftPlayer;
	}

	public GamePlayerController getRightPlayerController() {
		return rightPlayer;
	}

	/**
	 * Throws an exception with appropriate text, when any of the players are offline.
	 * @throws GameException when both players are offline, or either one of them
	 */
	public void checkOnline() throws GameException {
		OfflinePlayer left = leftPlayer.getPlayer();
		OfflinePlayer right = rightPlayer.getPlayer();
		
		if(!left.isOnline() && !right.isOnline()) {
			throw new GameException(left.getName() + " and " + right.getName() + " are both offline.");
		}
		else if(!left.isOnline()) {
			throw new GameException(left.getName() + " is offline.");
		}
		else if(!right.isOnline()) {
			throw new GameException(right.getName() + " is offline.");
		}
	}
	
	/**
	 * Start the game. 
	 * 
	 * <p>Draws the board. Teleports players. </p>
	 * <p>And prepares to start placing ships.</p>
	 * 
	 * @throws GameException
	 */
	public void start() throws GameException {
		view.draw();
		
		checkOnline();
		
		Player leftPlayer = getLeftPlayerController().getPlayer().getPlayer();
		Player rightPlayer = getRightPlayerController().getPlayer().getPlayer();
		
		// 
		// Teleport players
		// 
		
		Location loc = GameView.getCenterLocation(view.getLeftSideView().getInteractiveView());
		loc.setY(loc.getY() + 1);
		leftPlayer.teleport(loc);
		
		loc = GameView.getCenterLocation(view.getRightSideView().getInteractiveView());
		loc.setY(loc.getY() + 1);
		rightPlayer.teleport(loc);
		
		// save players' inventories
		// TODO give inventories back at some point
		this.leftPlayer.getGamePlayer().setOriginalInventory(new GamePlayerInventory(leftPlayer.getInventory()));
		this.rightPlayer.getGamePlayer().setOriginalInventory(new GamePlayerInventory(rightPlayer.getInventory()));
		
		// clear players' inventories
		leftPlayer.getInventory().clear();
		rightPlayer.getInventory().clear();
		
		// give ship items
		ShipType[] ships = game.getShipTypes();
		for (ShipType shipType : ships) {
			leftPlayer.getInventory().addItem(shipType.getNormal().toItemStack(shipType.getShipSize()));
			rightPlayer.getInventory().addItem(shipType.getNormal().toItemStack(shipType.getShipSize()));
		}
		leftPlayer.updateInventory();
		rightPlayer.updateInventory();
	}

}
