package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.models.Tile;

public class BoardView implements Board.Observer {
	
	public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
	
	private Board board;
	private Location origin;
	private BlockFace facing;

	private TileLocationStrategy tileLocationStrategy;
	
	public BoardView(Board board, Location origin, BlockFace facing) {
		this.board = board;
		this.board.addObserver(this);
		this.origin = origin;
		this.facing = facing;
		this.tileLocationStrategy = TileLocationStrategy.HORIZONTAL;
	}
	
	public static BlockFace yawToFace(float yaw) {
		return axis[Math.round(yaw / 90f) & 0x3];
    }
	
	public Board getBoard() {
		return board;
	}
	
	public Location getOrigin() {
		return origin;
	}
	
	public BlockFace getFacingDirection() {
		return facing;
	}

	public TileLocationStrategy getTileLocationStrategy() {
		return tileLocationStrategy;
	}

	public void setTileLocationStrategy(TileLocationStrategy tileLocationStrategy) {
		this.tileLocationStrategy = tileLocationStrategy;
	}
	
	public void draw() {
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				draw(x, y);
			}
		}
	}
	
	public void draw(int x, int y) {
		Block block = tileLocationStrategy.getTileLocation(this, x, y).getBlock();
		Tile tile = board.getTile(x, y);
		
		if(tile.isHit()) {
			if(tile.hasShip()) {
				// draw, broken ship
				drawDamagedShip(tile, block);
			}
			else {
				// draw, enemy missed
				drawMiss(tile, block);
			}
		}
		else {
			if(tile.hasShip()) {
				// draw, ship
				drawShip(tile, block);
			}
			else {
				// draw, board
				drawBoard(tile, block);
			}
		}
		
	}

	protected void drawBoard(Tile tile, Block block) {
		block.setTypeIdAndData(98, (byte)0, false);// bricks
	}

	protected void drawShip(Tile tile, Block block) {
		block.setTypeIdAndData(5, (byte) 0, false);// plank
	}

	protected void drawMiss(Tile tile, Block block) {
		block.setTypeIdAndData(98, (byte) 2, false);// cracked bricks
	}

	protected void drawDamagedShip(Tile tile, Block block) {
		block.setTypeIdAndData(5, (byte) 1, false);// dark planks
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
