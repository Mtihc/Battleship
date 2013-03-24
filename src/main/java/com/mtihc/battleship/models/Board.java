package com.mtihc.battleship.models;

public class Board {

	private Tile[][] board;

	public Board(int width, int height) {
		
		// create tiles
		this.board = new Tile[width][height];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				board[x][y] = new Tile(this, x, y);
			}
		}
		
		
	}
	
	public int getWidth() {
		return board.length;
	}
	
	public int getHeight() {
		return board[0].length;
	}
	
	public Tile getTile(int x, int y) {
		return board[x][y];
	}

}
