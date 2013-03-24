package com.mtihc.battleship.models;

public class Tile {

	private Board board;
	private int x;
	private int y;
	private Ship ship;
	private boolean hit;

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
	
	public boolean isHit() {
		return hit;
	}
	
	public boolean hit() throws Exception {
		if(isHit()) {
			throw new Exception("The tile is already hit.");
		}
		else {
			hit = true;
			if(hasShip()) {
				board.onHit(this);
				return true;
			}
			else {
				board.onMiss(this);
				return false;
			}
		}
	}
}
