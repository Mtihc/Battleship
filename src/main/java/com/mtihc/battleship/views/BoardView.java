package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Board.Ship;
import com.mtihc.battleship.models.Board.Tile;

public class BoardView {

	private Board board;
	private Location origin;
	private BlockFace face;

	private BoardLocationStrategy locationStrategy;
	private BoardDrawStrategy drawStrategy;
	
	private BoardObserver observer = new BoardObserver();

	public BoardView(Board board, Location origin, BlockFace face) {
		setBoard(board);
		this.origin = origin;
		this.face = face;
		this.locationStrategy = BoardLocationStrategy.HORIZONTAL;
		this.drawStrategy = BoardDrawStrategy.NORMAL;
	}

	/**
	 * The board model
	 * @return the board model
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Set the board model.
	 * 
	 * <p>Automatically adds/removes this view as observer.</p>
	 * 
	 * @param board the board model to set
	 */
	public void setBoard(Board board) {
		if(this.board != board) {
			if(this.board != null) {
				this.board.removeObserver(observer);
			}
			if(board != null) {
				board.addObserver(observer);
			}
			this.board = board;
		}
	}

	/**
	 * Return origin location
	 * @return the origin location
	 */
	public Location getOrigin() {
		return origin;
	}

	/**
	 * Return the facing direction (can only be NORTH, EAST, SOUTH or WEST)
	 * @return the facing direction
	 */
	public BlockFace getFace() {
		return face;
	}

	/**
	 * Returns the draw strategy.
	 * 
	 * @return draw strategy
	 */
	public BoardDrawStrategy getDrawStrategy() {
		return drawStrategy;
	}

	/**
	 * Set the draw strategy.
	 * <p>Change the draw strategy to, for example, hide ships. Or give the board a different look.</p>
	 * @param drawStrategy
	 */
	public void setDrawStrategy(BoardDrawStrategy drawStrategy) {
		this.drawStrategy = drawStrategy;
	}

	/**
	 * Draws the entire board
	 */
	public void draw() {
		drawStrategy.draw(this);
	}

	/**
	 * Draws a certain tile on the board
	 * @param x the tile's x-coordinate on the board
	 * @param y the tile's y-coordinate on the board
	 */
	public void draw(int x, int y) {
		drawStrategy.draw(this, x, y);
	}

	/**
	 * Returns the location strategy.
	 * @return location strategy
	 */
	public BoardLocationStrategy getLocationStrategy() {
		return locationStrategy;
	}

	/**
	 * Set the location strategy. 
	 * <p>Change the location strategy to, for example, create a vertical board.</p>
	 * @param locationStrategy
	 */
	public void setLocationStrategy(BoardLocationStrategy locationStrategy) {
		this.locationStrategy = locationStrategy;
	}
	
	public Location tileToLocation(int x, int y) {
		return locationStrategy.tileToLocation(this, x, y);
	}
	
	public Tile locationToTile(Location location) {
		return locationStrategy.locationToTile(this, location);
	}
	

	public class BoardObserver implements Board.Observer {

		@Override
		public void onMiss(Board board, Tile tile) {
			draw(tile.getX(), tile.getY());
		}

		@Override
		public void onHit(Board board, Tile tile) {
			draw(tile.getX(), tile.getY());
		}

		@Override
		public void onShipPlace(Board board, Ship ship) {
			Tile[] tiles = ship.getTiles();
			for (Tile tile : tiles) {
				draw(tile.getX(), tile.getY());
			}
		}

		@Override
		public void onAllShipsPlaced(Board board) {
			
		}

		@Override
		public void onShipRemove(Board board, Ship ship) {
			Tile[] tiles = ship.getTiles();
			for (Tile tile : tiles) {
				draw(tile.getX(), tile.getY());
			}
		}

		@Override
		public void onShipDestroyed(Board board, Ship ship) {
			
		}

		@Override
		public void onAllShipsDestroyed(Board board) {
			
		}

	}

}
