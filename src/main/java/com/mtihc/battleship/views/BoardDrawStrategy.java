package com.mtihc.battleship.views;

import org.bukkit.block.Block;

import com.mtihc.battleship.models.Board.Ship;
import com.mtihc.battleship.models.Board.Tile;

public class BoardDrawStrategy {

	public static final BoardDrawStrategy NORMAL = new BoardDrawStrategy();
	public static final BoardDrawStrategy HIDE_SHIPS = new BoardDrawStrategy() {

		@Override
		protected void drawShip(Tile tile, Block block) {
			// non-damaged ships are hidden to the enemy
			drawBoard(tile, block);
		}
	};



	
	protected BoardDrawStrategy() {
		
	}
	
	public void draw(BoardView view) {
		for (int x = 0; x < view.getBoard().getWidth(); x++) {
			for (int y = 0; y < view.getBoard().getHeight(); y++) {
				draw(view, x, y);
			}
		}
	}
	
	public void draw(BoardView view, int x, int y) {
		Block block = view.getLocationStrategy().tileToLocation(view, x, y).getBlock();
		Tile tile = view.getBoard().getTile(x, y);
		
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
		Ship ship = tile.getShip();
		int typeId = ship.getType().getNormal().getItemTypeId();
		byte data = ship.getType().getNormal().getData();
		block.setTypeIdAndData(typeId, data, false);
	}

	protected void drawMiss(Tile tile, Block block) {
		block.setTypeIdAndData(98, (byte) 2, false);// cracked bricks
	}

	protected void drawDamagedShip(Tile tile, Block block) {
		Ship ship = tile.getShip();
		int typeId = ship.getType().getDamaged().getItemTypeId();
		byte data = ship.getType().getDamaged().getData();
		block.setTypeIdAndData(typeId, data, false);
	}

}
