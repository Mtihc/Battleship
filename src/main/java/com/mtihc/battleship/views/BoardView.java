package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.models.Tile;

public class BoardView implements Board.Observer {

	// only using SOUTH, WEST, NORTH, EAST
	public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };

	/**
	 * Convert a yaw value to one of the following BlockFace enum values: NORTH, EAST, SOUTH or WEST.
	 * @param yaw the yaw value
	 * @return the BlockFace enum value
	 */
	public static BlockFace yawToFace(float yaw) {
		return axis[Math.round(yaw / 90f) & 0x3];
	}
	
	/**
	 * Convert a BlockFace enum value, to a yaw value. Only accepts: NORTH, EAST, SOUTH, WEST.
	 * Other values will result in 0, which happens to be south.
	 * @param face the BlockFace enum value
	 * @return the yaw value
	 */
	public static float faceToYaw(BlockFace face) {
		switch(face) {
		case EAST:
			return -90;
		case NORTH:
			return 180;
		case WEST:
			return 90;
		case SOUTH:
			return 0;
		default:
			return 0;
		}
	}
	
	
	
	
	
	private Board board;
	private Location origin;
	private BlockFace facing;

	private BoardLocationStrategy locationStrategy;

	private BoardDrawStrategy drawStrategy;

	public BoardView(Board board, Location origin, BlockFace facing) {
		setBoard(board);
		this.origin = origin;
		this.facing = facing;
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
				this.board.removeObserver(this);
			}
			if(board != null) {
				board.addObserver(this);
			}
			this.board = board;
		}
	}
	
	/**
	 * Define another BoardView, and let it switch models and draw strategy with this BoardView.
	 * @param otherView the other BoardView
	 */
	public void switchViews(BoardView otherView) {
		// switch draw strategy
		BoardDrawStrategy s = this.drawStrategy;
		setDrawStrategy(otherView.getDrawStrategy());
		otherView.setDrawStrategy(s);
		
		// switch boards
		Board b = this.board;
		setBoard(otherView.getBoard());
		otherView.setBoard(b);
		
		// redraw
		draw();
		otherView.draw();
	}
	
	/**
	 * Get the center location of this board view. (assuming the horizontal location strategy is used)
	 * 
	 * @return the center location
	 */
	public Location getCenterLocation() {
		Location result = locationStrategy.tileToLocation(this, board.getWidth() / 2, board.getHeight() / 2);
		result.setYaw(faceToYaw(facing));
		result.setPitch(0);
		return result;
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
	public BlockFace getFacingDirection() {
		return facing;
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
	 * 
	 * <p>Change the draw strategy to, for example, hide ships. Or give the board a different look.</p>
	 * 
	 * @param drawStrategy
	 */
	public void setDrawStrategy(BoardDrawStrategy drawStrategy) {
		this.drawStrategy = drawStrategy;
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
	 * 
	 * <p>Change the location strategy to, for example, create a vertical board.</p>
	 * 
	 * @param locationStrategy
	 */
	public void setLocationStrategy(BoardLocationStrategy locationStrategy) {
		this.locationStrategy = locationStrategy;
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
	
	@Override
	public void onMiss(Tile tile) {
		draw(tile.getX(), tile.getY());
	}

	@Override
	public void onHit(Tile tile) {
		draw(tile.getX(), tile.getY());
	}

	@Override
	public void onShipDestroyed(Ship ship) {
		// empty
	}

	@Override
	public void onShipPlace(Ship ship) {
		Tile[] tiles = ship.getTiles();
		for (Tile tile : tiles) {
			draw(tile.getX(), tile.getY());
		}
	}

	@Override
	public void onShipRemove(Ship ship) {
		Tile[] tiles = ship.getTiles();
		for (Tile tile : tiles) {
			draw(tile.getX(), tile.getY());
		}
	}

}
