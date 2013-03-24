package com.mtihc.battleship.views;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.models.Tile;

public class BoardView implements Board.Observer {

	public BoardView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMiss(Tile tile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHit(Tile tile) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShipRemove(Ship ship) {
		// TODO Auto-generated method stub
		
	}
}
