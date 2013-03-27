package com.mtihc.battleship.controllers;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.models.ShipType;
import com.mtihc.battleship.models.Tile;
import com.mtihc.battleship.views.GameView;

public class GameController implements Board.Observer {

	private JavaPlugin plugin;
	private Game game;
	private GameView view;
	
	GameController(JavaPlugin plugin, GamePlayer left, GamePlayer right, Game game) {
		this.plugin = plugin;
		this.game = game;
		// relation between GameView and Game model
		this.view = new GameView(game, left.getPlayer(), right.getPlayer());
		
		// relation between GameView and this controller
		view.getLeftSide().getBoard().addObserver(this);
		view.getRightSide().getBoard().addObserver(this);
		
	}
	
	/**
	 * The id of the game model
	 * @return id of the game model
	 */
	public String getId() {
		return game.getId();
	}

	/**
	 * The plugin
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}

	/**
	 * The game model
	 * @return the game model
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * The game view
	 * @return the game view
	 */
	public GameView getView() {
		return view;
	}

	/**
	 * The left player
	 * @return the left player
	 */
	public OfflinePlayer getLeftPlayer() {
		return view.getLeftSide().getPlayer();
	}

	/**
	 * The right player
	 * @return the right player
	 */
	public OfflinePlayer getRightPlayer() {
		return view.getRightSide().getPlayer();
	}
	
	/**
	 * Throws an exception with appropriate text, when any of the players are offline.
	 * @throws GameException when both players are offline, or either one of them
	 */
	public void checkOnline() throws GameException {
		OfflinePlayer left = getLeftPlayer();
		OfflinePlayer right = getRightPlayer();
		
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
	 * Initialize the game. 
	 * 
	 * <p>Draws the board. Teleports players. </p>
	 * <p>And prepares to start placing ships.</p>
	 * 
	 * @throws GameException
	 */
	public void initialize() throws GameException {
		view.draw();
		
		checkOnline();
		
		Player leftPlayer = getLeftPlayer().getPlayer();
		Player rightPlayer = getRightPlayer().getPlayer();
		
		Location loc = view.getLeftSide().getInteractiveView().getCenterLocation();
		loc.setY(loc.getY() - 1);
		leftPlayer.teleport(loc);
		
		loc = view.getRightSide().getInteractiveView().getCenterLocation();
		loc.setY(loc.getY() - 1);
		rightPlayer.teleport(loc);
		
		// start placing ships, 
		// TODO save players' inventories
		
		// clear players' inventories
		leftPlayer.getInventory().clear();
		rightPlayer.getInventory().clear();
		
		// give ship items
		ShipType[] ships = game.getShipTypes();
		for (ShipType shipType : ships) {
			leftPlayer.getInventory().addItem(shipType.getNormal().toItemStack());
			rightPlayer.getInventory().addItem(shipType.getNormal().toItemStack());
		}
		
	}

	@Override
	public void onMiss(Tile tile) {
		// TODO player says: Miss!
	}

	@Override
	public void onHit(Tile tile) {
		// TODO player says: Hit!
	}

	@Override
	public void onShipDestroyed(Ship ship) {
		// TODO player says: You sunk my ship.getName()
		
		if(!game.areAllShipsDestroyed()) {
			return;
		}
		// TODO game end
	}

	@Override
	public void onShipPlace(Ship ship) {
		Board board = ship.getBoard();
		if(!board.areAllShipsPlaced()) {
			return;
		}
		if(!board.areAllShipsPlaced()) {
			return;
		}
		// all ships are placed on both sides
		
		// we are done placing ships, 
		// switch views so we can start placing bombs
		view.getLeftSide().getInteractiveView().switchViews(view.getLeftSide().getProjectorView());
		view.getRightSide().getInteractiveView().switchViews(view.getRightSide().getProjectorView());
		
		// TODO start placing bombs
		Player leftPlayer = getLeftPlayer().getPlayer();
		Player rightPlayer = getRightPlayer().getPlayer();
		// TODO see whos turn it is, give TNT
	}

	@Override
	public void onShipRemove(Ship ship) {
		
	}

}
