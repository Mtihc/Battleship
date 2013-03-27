package com.mtihc.battleship.models;

import java.util.LinkedHashSet;

public class Board {

	public interface Observer {
		void onMiss(Tile tile);

		void onHit(Tile tile);

		void onShipDestroyed(Ship ship);

		void onShipPlace(Ship ship);

		void onShipRemove(Ship ship);
	}
	
	private Tile[][] board;
	private Ship[] ships;
	private LinkedHashSet<Observer> observers = new LinkedHashSet<Observer>();
	Board enemy;

	public Board(int width, int height, ShipType[] shipTypes) {
		
		// create tiles
		this.board = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				board[x][y] = new Tile(this, x, y);
			}
		}
		
		// create ships array
		this.ships = new Ship[shipTypes.length];
		for (int i = 0; i < shipTypes.length; i++) {
			this.ships[i] = new Ship(this, shipTypes[i]);
		}
	}
	
	public Board getEnemy() {
		return enemy;
	}
	
	/**
	 * The width of the board (columns)
	 * @return the width of the board
	 */
	public int getWidth() {
		return board.length;
	}
	
	/**
	 * The height of the board (rows)
	 * @return the height of the board
	 */
	public int getHeight() {
		return board[0].length;
	}
	
	/**
	 * Returns the tile at the specified coordinates on the board.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the Tile at the given coordinates on the board
	 * @throws IndexOutOfBoundsException when the x-coordinate or y-coordate are out of bounds
	 */
	public Tile getTile(int x, int y) throws IndexOutOfBoundsException {
		return board[x][y];
	}

	/**
	 * All the ships that belong to this board.
	 * @return an array of Ship objects
	 */
	public Ship[] getShips() {
		return ships;
	}
	
	/**
	 * The amount of ships that belong to this board.
	 * @return the amount of ships
	 */
	public int getShipCount() {
		return ships.length;
	}
	
	
	/**
	 * Add an observer to the list.
	 * @param observer the observer to add
	 */
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	/**
	 * Remove an observer from the list.
	 * @param observer the observer to remove
	 */
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}
	
	/**
	 * Called when the enemy attacked this board, but didn't hit a ship.
	 * @param tile the tile that was hit
	 */
	protected void onMiss(Tile tile) {
		// notify observers
		for (Observer observer : observers) {
			observer.onMiss(tile);
		}
	}

	/**
	 * Called when the enemy attacked and hit a ship!
	 * @param tile the tile that was hit and contains part of the ship
	 */
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

	/**
	 * Called when all all tiles belonging to a ship are hit. 
	 * @param ship the ship that was destroyed
	 */
	protected void onShipDestoyed(Ship ship) {
		// notify observers
		for (Observer observer : observers) {
			observer.onShipDestroyed(ship);
		}
	}

	/**
	 * Called when a ship is placed on the board, during setup.
	 * @param ship the ship that was placed
	 */
	protected void onShipPlace(Ship ship) {
		// notify observers
		for (Observer observer : observers) {
			observer.onShipPlace(ship);
		}
		
	}

	/**
	 * Called when a ship is removed from the board, during setup.
	 * @param ship
	 */
	protected void onShipRemove(Ship ship) {
		// notify observers
		for (Observer observer : observers) {
			observer.onShipRemove(ship);
		}
	}
}
