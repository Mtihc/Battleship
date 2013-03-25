package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public abstract class BoardLocationStrategy {

	public static final BoardLocationStrategy VERTICAL = new BoardLocationStrategy() {
		
		@Override
		public Location getTileLocation(BoardView board, int x, int y) {
			Location origin = board.getOrigin();
			World world = origin.getWorld();
			origin = origin.getBlock().getRelative(board.getFacingDirection(), 2).getRelative(BlockFace.UP, board.getBoard().getHeight() + 1).getLocation();

			switch (board.getFacingDirection()) {
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
	};
	
	public static final BoardLocationStrategy HORIZONTAL = new BoardLocationStrategy() {
		
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
				return new Location(world, origin.getBlockX() - y, origin.getBlockY(), origin.getBlockZ() + x);
			case WEST:
				return new Location(world, origin.getBlockX() + y, origin.getBlockY(), origin.getBlockZ() - x);
			default:
				return null;
			}
		}
	};
	
	protected BoardLocationStrategy() {
		
	}
	
	public abstract Location getTileLocation(BoardView board, int x, int y);

}
