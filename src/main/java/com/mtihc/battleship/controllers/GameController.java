package com.mtihc.battleship.controllers;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.Ship;
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
	 * Returns the other board
	 * @param board input board
	 * @return other board
	 */
	public Board getEnemyBoard(Board board) {
		if(board == view.getLeftSide().getBoard()) {
			return view.getRightSide().getBoard();
		}
		else if(board == view.getRightSide().getBoard()) {
			return view.getLeftSide().getBoard();
		}
		else {
			return null;
		}
	}

	/**
	 * Returns whether all ships are placed on a board
	 * @param board the board
	 * @return true if all ships are placed, false otherwise
	 */
	public boolean areAllShipsPlaced(Board board) {
		Ship[] ships = board.getShips();
		for (Ship ship : ships) {
			if(!ship.isPlaced()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns wether all ships are destroyed on a board
	 * @param board the board
	 * @return true if all ships are destroyed, false otherwise
	 */
	public boolean areAllShipsDestroyed(Board board) {
		Ship[] ships = board.getShips();
		for (Ship ship : ships) {
			if(!ship.isDestroyed()) {
				return false;
			}
		}
		return true;
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
		
		getLeftPlayer().getPlayer().teleport(
				view.getLeftSide().getInteractiveView().getCenterLocation());
		getRightPlayer().getPlayer().teleport(
				view.getRightSide().getInteractiveView().getCenterLocation());
		
		// TODO start placing ships
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
		
		Board board = ship.getBoard();
		if(!areAllShipsDestroyed(board)) {
			return;
		}
		board = getEnemyBoard(board);
		if(!areAllShipsDestroyed(board)) {
			return;
		}
		
		// TODO game end
	}

	@Override
	public void onShipPlace(Ship ship) {
		Board board = ship.getBoard();
		if(!areAllShipsPlaced(board)) {
			return;
		}
		board = getEnemyBoard(board);
		if(!areAllShipsPlaced(board)) {
			return;
		}
		// all ships are placed on both sides
		
		// we are done placing ships, 
		// switch views so we can start placing bombs
		view.getLeftSide().getInteractiveView().switchViews(view.getLeftSide().getProjectorView());
		view.getRightSide().getInteractiveView().switchViews(view.getRightSide().getProjectorView());
		
		// TODO start placing bombs
	}

	@Override
	public void onShipRemove(Ship ship) {
		
	}

}
