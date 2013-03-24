package com.mtihc.battleship.models;

public class Ship {

	private Tile[] tiles;
	
	Ship(int size) {
		tiles = new Tile[size];
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
		
		for (int i = 0; i < args.length; i++) {
			Tile tile = args[i];
			
			tiles[i] = tile;
			tile.setShip(this);
		}
	}
	
	public void remove() {
		
		for (int i = 0; i < tiles.length; i++) {
			Tile tile = tiles[i];

			tiles[i] = null;
			tile.setShip(null);
		}
	}

}
