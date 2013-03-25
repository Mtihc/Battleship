package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Game;

public class GameView {

	private Board leftBoard;
	private Board rightBoard;
	
	private Location leftOrigin;
	private Location rightOrigin;
	
	private BoardView leftBoardView;
	private BoardView rightBoardView;
	
	private BoardView leftEnemyView;
	private BoardView rightEnemyView;

	public GameView(Game game) {
		// create boards (model)
		leftBoard = new Board(game.getWidth(), game.getHeight(), game.getShipTypes());
		rightBoard = new Board(game.getWidth(), game.getHeight(), game.getShipTypes());
		
		Location origin = game.getOrigin();
		
		// set facing directions
		BlockFace facing = BoardView.yawToFace(origin.getYaw());
		BlockFace facingLeft = BoardView.yawToFace(origin.getYaw() - 90);
		BlockFace facingRight = BoardView.yawToFace(origin.getYaw() + 90);
		
		//
		// set origin locations for the views
		// 
		// left side's origin is a little to the left and all the way forward
		leftOrigin = origin.getBlock().getRelative(facingLeft, 3).getRelative(facing, game.getWidth()).getLocation();
		// right side's origin is a little to the right and that's about it. 
		rightOrigin = origin.getBlock().getRelative(facingRight, 3).getRelative(facing, 1).getLocation();
		
		// create normal views that show your own board
		leftBoardView = new BoardView(leftBoard, leftOrigin, facingRight);
		rightBoardView = new BoardView(rightBoard, rightOrigin, facingLeft);
		
		// create views that show your enemy's board
		leftEnemyView = new BoardView(rightBoard, leftOrigin, facingRight);
		rightEnemyView = new BoardView(leftBoard, rightOrigin, facingLeft);
		// the enemy ships should be hidden
		leftEnemyView.setDrawStrategy(BoardDrawStrategy.HIDE_SHIPS);
		rightEnemyView.setDrawStrategy(BoardDrawStrategy.HIDE_SHIPS);
	}

	public void initialize() {
		leftBoardView.draw();
		leftEnemyView.draw();
		
		rightBoardView.draw();
		rightEnemyView.draw();
	}
}
