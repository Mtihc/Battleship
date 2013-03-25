package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public abstract class TileLocationStrategy {

	public static final TileLocationStrategy VERTICAL = new TileLocationStrategy() {
		
		@Override
		public Location getTileLocation(BoardView board, int x, int y) {
			Location origin = board.getOrigin();
			World world = origin.getWorld();
			origin = origin.getBlock().getRelative(board.getFacingDirection(), 1).getRelative(BlockFace.UP, board.getBoard().getHeight()).getLocation();

			switch (board.getFacingDirection()) {
			case NORTH:
				return new Location(world, origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ());
			case SOUTH:
				return new Location(world, origin.getBlockX() - x, origin.getBlockY() - y, origin.getBlockZ());
			case EAST:
				return new Location(world, origin.getBlockX() + y, origin.getBlockY() + x, origin.getBlockZ());
			case WEST:
				return new Location(world, origin.getBlockX() - y, origin.getBlockY() - x, origin.getBlockZ());
			default:
				return null;
			}
		}
	};
	
	public static final TileLocationStrategy HORIZONTAL = new TileLocationStrategy() {
		
		@Override
		public Location getTileLocation(BoardView board, int x, int y) {
			Location origin = board.getOrigin();
			World world = origin.getWorld();

			switch (board.getFacingDirection()) {
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
	};
	
	protected TileLocationStrategy() {
		
	}
	
	public abstract Location getTileLocation(BoardView board, int x, int y);

}
