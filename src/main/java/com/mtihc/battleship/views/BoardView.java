package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.models.Tile;

public class BoardView implements Board.Observer {
	
	public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
	
	private Board board;
	private Location origin;
	private BlockFace facing;
	
	public BoardView(Board board, Location origin) {
		this.board = board;
		this.board.addObserver(this);
		this.origin = origin;
		this.facing = yawToFace(origin.getYaw());
	}
	
	public static BlockFace yawToFace(float yaw) {
		return axis[Math.round(yaw / 90f) & 0x3];
    }
	
	public Location getTileLocation(int x, int y) {
		World world = origin.getWorld();

		switch (facing) {
		case NORTH:
			return new Location(world, origin.getBlockX() + x, origin.getBlockY(), origin.getBlockZ() + y);
		case SOUTH:
			return new Location(world, origin.getBlockX() - x, origin.getBlockY(), origin.getBlockZ() - y);
		case EAST:
			return new Location(world, origin.getBlockX() + y, origin.getBlockY(), origin.getBlockZ() + x);
		case WEST:
			return new Location(world, origin.getBlockX() - y, origin.getBlockY(), origin.getBlockZ() - x);
		default:
			return null;
		}
	}
	
	public void draw() {
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				draw(x, y);
			}
		}
	}
	
	public void draw(int x, int y) {
		Block block = getTileLocation(x, y).getBlock();
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
		block.setTypeIdAndData(90, (byte)0, false);// bricks
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
