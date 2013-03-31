package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board.Tile;

public abstract class BoardLocationStrategy {

	public static final BoardLocationStrategy VERTICAL = new BoardLocationStrategy() {
		
		@Override
		public Location tileToLocation(BoardView board, int x, int y) {
			Location origin = board.getOrigin();
			World world = origin.getWorld();
			origin = origin.getBlock().getRelative(board.getFace(), 2).getRelative(BlockFace.UP, board.getBoard().getHeight() + 1).getLocation();

			switch (board.getFace()) {
			case NORTH:
				return new Location(world, origin.getBlockX() + x, origin.getBlockY() - y, origin.getBlockZ());
			case SOUTH:
				return new Location(world, origin.getBlockX() - x, origin.getBlockY() - y, origin.getBlockZ());
			case EAST:
				return new Location(world, origin.getBlockX(), origin.getBlockY() - y, origin.getBlockZ() + x);
			case WEST:
				return new Location(world, origin.getBlockX(), origin.getBlockY() - y, origin.getBlockZ() - x);
			default:
				return null;
			}
		}

		@Override
		public Tile locationToTile(BoardView board, Location location) {
			Location origin = board.getOrigin();

			try {
				switch (board.getFace()) {
				case NORTH:
					return board.getBoard().getTile(
							Math.abs(location.getBlockX() - origin.getBlockX()), 
							Math.abs(location.getBlockY() - origin.getBlockY()));
				case SOUTH:
					return board.getBoard().getTile(
							Math.abs(location.getBlockX() - origin.getBlockX()), 
							Math.abs(location.getBlockY() - origin.getBlockY()));
				case EAST:
					return board.getBoard().getTile(
							Math.abs(location.getBlockZ() - origin.getBlockZ()), 
							Math.abs(location.getBlockY() - origin.getBlockY()));
				case WEST:
					return board.getBoard().getTile(
							Math.abs(location.getBlockZ() - origin.getBlockZ()), 
							Math.abs(location.getBlockY() - origin.getBlockY()));
				default:
					return null;
				}
			} 
			catch(IndexOutOfBoundsException e) {
				return null;
			}
		}
	};
	
	public static final BoardLocationStrategy HORIZONTAL = new BoardLocationStrategy() {
		
		@Override
		public Location tileToLocation(BoardView board, int x, int y) {
			Location origin = board.getOrigin();
			World world = origin.getWorld();

			switch (board.getFace()) {
			case NORTH:
				return new Location(world, 
						origin.getBlockX() + x, 
						origin.getBlockY(), 
						origin.getBlockZ() + y);
			case SOUTH:
				return new Location(world, 
						origin.getBlockX() - x, 
						origin.getBlockY(), 
						origin.getBlockZ() - y);
			case EAST:
				return new Location(world, 
						origin.getBlockX() - y, 
						origin.getBlockY(), 
						origin.getBlockZ() + x);
			case WEST:
				return new Location(world, 
						origin.getBlockX() + y, 
						origin.getBlockY(), 
						origin.getBlockZ() - x);
			default:
				return null;
			}
		}

		@Override
		public Tile locationToTile(BoardView board, Location location) {
			Location origin = board.getOrigin();

			try {
				switch (board.getFace()) {
				case NORTH:
					return board.getBoard().getTile(
							Math.abs(location.getBlockX() - origin.getBlockX()), 
							Math.abs(location.getBlockZ() - origin.getBlockZ()));
				case SOUTH:
					return board.getBoard().getTile(
							Math.abs(location.getBlockX() - origin.getBlockX()), 
							Math.abs(location.getBlockZ() - origin.getBlockZ()));
				case EAST:
					return board.getBoard().getTile(
							Math.abs(location.getBlockZ() - origin.getBlockZ()), 
							Math.abs(location.getBlockX() - origin.getBlockX()));
				case WEST:
					return board.getBoard().getTile(
							Math.abs(location.getBlockZ() - origin.getBlockZ()), 
							Math.abs(location.getBlockX() - origin.getBlockX()));
				default:
					return null;
				}
			} 
			catch(IndexOutOfBoundsException e) {
				return null;
			}
		}
	};
	
	protected BoardLocationStrategy() {
		
	}
	
	public abstract Location tileToLocation(BoardView board, int x, int y);

	public abstract Tile locationToTile(BoardView board, Location location);

}
