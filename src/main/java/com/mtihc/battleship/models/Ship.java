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

}
