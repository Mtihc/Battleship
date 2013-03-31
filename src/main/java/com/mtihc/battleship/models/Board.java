package com.mtihc.battleship.models;


public class Board {
	
	private Tile[][] tiles;
	private Ship[] ships;
	
	public Board(int width, int height, ShipType[] shipTypes) {

		// create tiles
		this.tiles = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = new Tile(x, y);
			}
		}                    
		
		// create ships array
		this.ships = new Ship[shipTypes.length];
		for (int i = 0; i < shipTypes.length; i++) {
			this.ships[i] = new Ship(shipTypes[i]);
		}
	}
	
	/**
	 * The width of the board (columns)
	 * @return the width of the board
	 */
	public int getWidth() {
		return tiles.length;
	}
	
	/**
	 * The height of the board (rows)
	 * @return the height of the board
	 */
	public int getHeight() {
		return tiles[0].length;
	}
	
	/**
	 * Returns the tile at the specified coordinates on the board.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the Tile at the given coordinates on the board
	 * @throws IndexOutOfBoundsException when the x-coordinate or y-coordate are out of bounds
	 */
	public Tile getTile(int x, int y) throws IndexOutOfBoundsException {
		return tiles[x][y];
	}

	/**
	 * All the ships that belong to this board.
	 * @return an array of Ship objects
	 */
	public Ship[] getShips() {
		return ships.clone();
	}
	
	/**
	 * Returns the ship at the current index. 
	 * @param index
	 * @return
	 */
	public Ship getShip(int index) {
		return ships[index];
	}
	
	/**
	 * The amount of ships that belong to this board.
	 * @return the amount of ships
	 */
	public int getShipCount() {
		return ships.length;
	}
	
	public boolean areAllShipsDestroyed() {
		for (int i = 0; i < ships.length; i++) {
			Ship ship = ships[i];
			if(!ship.isDestroyed()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean areAllShipsPlaced() {
		for (int i = 0; i < ships.length; i++) {
			Ship ship = ships[i];
			if(!ship.isPlaced()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * The Tile class represents a tile on the board.
	 * <p>A tile can get hit. When it's occupied by a ship, it's a hit. When there's no ship, it's a miss.</p>
	 * 
	 * @author Mitch
	 *
	 */
	public class Tile {
		private int x;
		private int y;
		private boolean hit;
		private Ship ship;

		Tile(int x, int y) {
			this.x = x;
			this.y = y;
			this.hit = false;
			this.ship = null;
		}
		
		public Board getBoard() {
			return Board.this;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public boolean isHit() {
			return hit;
		}

		public void setHit(boolean hit) {
			this.hit = hit;
		}
		
		public boolean hasShip() {
			return ship != null;
		}

		public Ship getShip() {
			return ship;
		}
	}
	
	/**
	 * The Ship class represents a ship on the board. 
	 * 
	 * @author Mitch
	 *
	 */
	public class Ship {
		private ShipType type;
		private Tile[] tiles;

		Ship(ShipType type) {
			this.type = type;
			this.tiles = new Tile[type.getShipSize()];
		}
		
		public Board getBoard() {
			return Board.this;
		}
		
		public ShipType getType() {
			return type;
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

		public void place(Tile[] tiles) {
			this.tiles = new Tile[getSize()];
			if(tiles == null || tiles.length != getSize()) {
				return;
			}
			for (int i = 0; i < this.tiles.length; i++) {
				this.tiles[i] = tiles[i];
				this.tiles[i].ship = this;
			}
			
		}
		
		public void remove() {
			for (Tile tile : tiles) {
				tile.ship = null;
			}
			this.tiles = new Tile[getSize()];
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
	}
}
