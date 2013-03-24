package com.mtihc.battleship.models;

public class Ship {

	private Board board;
	private Tile[] tiles;
	
	public Ship(int size) {
		tiles = new Tile[size];
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getSize() {
		return tiles.length;
	}
	
	public Tile getTile(int index) {
		return tiles[index];
	}
	
	public Tile[] getTiles() {
		return tiles.clone();
	}
	
	public boolean isPlaced() {
		for (int i = 0; i < tiles.length; i++) {
			if(tiles[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isDestroyed() {
		for (int i = 0; i < tiles.length; i++) {
			if(!tiles[i].isHit()) {
				return false;
			}
		}
		return true;
	}
	
	public void place(Tile ...args) throws Exception {
		if(args.length < tiles.length || args.length > tiles.length) {
			throw new Exception("Incorrect number of tiles. Expected " + tiles.length + ", got " + args.length + ".");
		}
		
		board = getBoard(args);
		
		for (int i = 0; i < args.length; i++) {
			Tile tile = args[i];
			
			tiles[i] = tile;
			tile.setShip(this);
		}
		
		board.onShipPlace(this);
	}

	public void remove() {
		
		board.onShipRemove(this);
		
		for (int i = 0; i < tiles.length; i++) {
			Tile tile = tiles[i];

			tiles[i] = null;
			tile.setShip(null);
		}
		
		board = null;
	}
	
	private Board getBoard(Tile[] tiles) throws Exception {
		Board result = tiles[0].getBoard();
		for (int i = 1; i < tiles.length; i++) {
			if(tiles[i].getBoard() != result) {
				throw new Exception("These tiles are not from the same board.");
			}
		}
		return result;
	}

}
