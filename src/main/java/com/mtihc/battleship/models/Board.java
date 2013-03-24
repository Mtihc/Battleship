package com.mtihc.battleship.models;

public class Board {

	private Tile[][] board;
	private Ship[] ships;

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
	
	
	
	
	protected void onTileHit(Tile tile) {
		if(tile.hasShip()) {
			Ship ship = tile.getShip();
			if(ship.isDestroyed()) {
				onShipDestoyed(ship);
			}
		}
		// TODO notify observers?
	}

	protected void onShipDestoyed(Ship ship) {
		if(areAllShipsDestroyed()) {
			onAllShipsDestroyed();
		}
		// TODO notify observers?
	}
	
	protected void onAllShipsDestroyed() {
		
		// TODO notify observers?
	}
}
