package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.models.Tile;

public class BoardView implements Board.Observer {

	public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };

	private Board board;
	private Location origin;
	private BlockFace facing;

	private BoardLocationStrategy locationStrategy;

	private BoardDrawStrategy drawStrategy;

	public BoardView(Board board, Location origin, BlockFace facing) {
		this.board = board;
		this.board.addObserver(this);
		this.origin = origin;
		this.facing = facing;
		this.locationStrategy = BoardLocationStrategy.HORIZONTAL;
		this.drawStrategy = BoardDrawStrategy.NORMAL;
	}

	public static BlockFace yawToFace(float yaw) {
		return axis[Math.round(yaw / 90f) & 0x3];
	}

	public Board getBoard() {
		return board;
	}
	
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

	public Location getOrigin() {
		return origin;
	}

	public BlockFace getFacingDirection() {
		return facing;
	}

	public BoardDrawStrategy getDrawStrategy() {
		return drawStrategy;
	}

	public void setDrawStrategy(BoardDrawStrategy drawStrategy) {
		this.drawStrategy = drawStrategy;
	}

	public BoardLocationStrategy getLocationStrategy() {
		return locationStrategy;
	}

	public void setLocationStrategy(BoardLocationStrategy locationStrategy) {
		this.locationStrategy = locationStrategy;
	}

	public void draw() {
		drawStrategy.draw(this);
	}

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
	public void onShipDestoyed(Ship ship) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllShipsDestroyed(Board board) {
		// TODO Auto-generated method stub

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
