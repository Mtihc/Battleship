package com.mtihc.battleship.models;

import java.util.LinkedHashSet;

public class Board {

	public interface Observer {
		void onMiss(Tile tile);

		void onHit(Tile tile);

		void onShipDestoyed(Ship ship);
		
		void onAllShipsDestroyed(Board board);

		void onShipPlace(Ship ship);

		void onShipRemove(Ship ship);
	}
	
	private Tile[][] board;
	private Ship[] ships;
	private LinkedHashSet<Observer> observers = new LinkedHashSet<Observer>();

	public Board(int width, int height, Ship[] ships) {
		
		// create tiles
		this.board = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				board[x][y] = new Tile(this, x, y);
			}
		}
		
		// clone ships array
		this.ships = ships.clone();
	}
	
	public int getWidth() {
		return board.length;
	}
	
	public int getHeight() {
		return board[0].length;
	}
	
	public Tile getTile(int x, int y) {
		return board[x][y];
	}

	public Ship[] getShips() {
		return ships;
	}
	
	public int getShipCount() {
		return ships.length;
	}

	public boolean areAllShipsDestroyed() {
		for (Ship ship : ships) {
			if(!ship.isDestroyed()) {
				return false;
			}
		}
		return true;
	}
	
	
	

	public boolean addObserver(Observer observer) {
		return observers.add(observer);
	}
	
	public boolean removeObserver(Observer observer) {
		return observers.remove(observer);
	}
	
	protected void onMiss(Tile tile) {
		// notify observers
		for (Observer observer : observers) {
			observer.onMiss(tile);
		}
	}

	protected void onHit(Tile tile) {
		Ship ship = tile.getShip();
		if(ship.isDestroyed()) {
			onShipDestoyed(ship);
		}
		// notify observers
		for (Observer observer : observers) {
			observer.onHit(tile);
		}
	}

	protected void onShipDestoyed(Ship ship) {
		if(areAllShipsDestroyed()) {
			onAllShipsDestroyed();
		}
		// notify observers
		for (Observer observer : observers) {
			observer.onShipDestoyed(ship);
		}
	}
	
	protected void onAllShipsDestroyed() {
		
		// notify observers
		for (Observer observer : observers) {
			observer.onAllShipsDestroyed(this);
		}
	}

	protected void onShipPlace(Ship ship) {
		// notify observers
		for (Observer observer : observers) {
			observer.onShipPlace(ship);
		}
		
	}

	protected void onShipRemove(Ship ship) {
		// notify observers
		for (Observer observer : observers) {
			observer.onShipRemove(ship);
		}
	}
}
