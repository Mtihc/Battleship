package com.mtihc.battleship.models;

public class Tile {

	private Board board;
	private int x;
	private int y;
	private Ship ship;

	Tile(Board board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean hasShip() {
		return ship != null;
	}

	public Ship getShip() {
		return ship;
	}
	
	void setShip(Ship ship) {
		this.ship = ship;
	}
}
