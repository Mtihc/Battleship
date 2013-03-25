package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Tile;

public class BoardEnemyView extends BoardView {

	public BoardEnemyView(Board board, Location origin, BlockFace facing) {
		super(board, origin, facing);
		setTileLocationStrategy(TileLocationStrategy.VERTICAL);
	}

	@Override
	protected void drawShip(Tile tile, Block block) {
		// non-damaged ships are hidden to the enemy
		drawBoard(tile, block);
	}

	
}
