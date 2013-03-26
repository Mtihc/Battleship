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
		this.view = new GameView(game, left.getPlayer(), right.getPlayer());
		
		view.getLeftSide().getBoard().addObserver(this);
		view.getRightSide().getBoard().addObserver(this);
		
	}
	
	public String getId() {
		return game.getId();
	}
	
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
	
	public void initialize() throws GameException {
		view.draw();
		
		checkOnline();
		
		getLeftPlayer().getPlayer().teleport(
				view.getLeftSide().getInteractiveView().getCenterLocation());
		getRightPlayer().getPlayer().teleport(
				view.getRightSide().getInteractiveView().getCenterLocation());
		
		// TODO start placing ships
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Game getGame() {
		return game;
	}
	
	public GameView getView() {
		return view;
	}

	public OfflinePlayer getLeftPlayer() {
		return view.getLeftSide().getPlayer();
	}

	public OfflinePlayer getRightPlayer() {
		return view.getRightSide().getPlayer();
	}

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

	public boolean areAllShipsPlaced(Board board) {
		Ship[] ships = board.getShips();
		for (Ship ship : ships) {
			if(!ship.isPlaced()) {
				return false;
			}
		}
		return true;
	}

	public boolean areAllShipsDestroyed(Board board) {
		Ship[] ships = board.getShips();
		for (Ship ship : ships) {
			if(!ship.isDestroyed()) {
				return false;
			}
		}
		return true;
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
	public void onShipDestoyed(Ship ship) {
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
