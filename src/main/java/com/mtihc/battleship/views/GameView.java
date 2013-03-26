package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Game;

public class GameView {

	private GameViewSide leftSide;
	private GameViewSide rightSide;

	public GameView(Game game, Player leftPlayer, Player rightPlayer) {
		// create boards (model)
		Board leftBoard = new Board(game.getWidth(), game.getHeight(), game.getShipTypes());
		Board rightBoard = new Board(game.getWidth(), game.getHeight(), game.getShipTypes());
		
		Location origin = game.getOrigin();
		
		// set facing directions
		BlockFace facing = BoardView.yawToFace(origin.getYaw());
		BlockFace facingLeft = BoardView.yawToFace(origin.getYaw() - 90);
		BlockFace facingRight = BoardView.yawToFace(origin.getYaw() + 90);
		
		//
		// set origin locations for the views
		// 
		// left side's origin is a little to the left and all the way forward
		Location leftOrigin = origin.getBlock().getRelative(facingLeft, 3).getRelative(facing, game.getWidth()).getLocation();
		// right side's origin is a little to the right and that's about it. 
		Location rightOrigin = origin.getBlock().getRelative(facingRight, 3).getRelative(facing, 1).getLocation();
		
		// create normal views that show your own board
		BoardView leftBoardView = new BoardView(leftBoard, leftOrigin, facingRight);
		BoardView rightBoardView = new BoardView(rightBoard, rightOrigin, facingLeft);
		
		// create views that show your enemy's board
		BoardView leftEnemyView = new BoardView(rightBoard, leftOrigin, facingRight);
		BoardView rightEnemyView = new BoardView(leftBoard, rightOrigin, facingLeft);
		// the enemy ships should be hidden
		leftEnemyView.setDrawStrategy(BoardDrawStrategy.HIDE_SHIPS);
		rightEnemyView.setDrawStrategy(BoardDrawStrategy.HIDE_SHIPS);
		
		leftSide = new GameViewSide(leftPlayer, leftBoard, leftBoardView, leftEnemyView);
		rightSide = new GameViewSide(rightPlayer, rightBoard, rightBoardView, rightEnemyView);
	}
	
	public GameViewSide getLeftSide() {
		return leftSide;
	}
	
	public GameViewSide getRightSide() {
		return rightSide;
	}
	
	public class GameViewSide {
		
		private Player player;
		private Board board;
		private BoardView interactiveView;
		private BoardView projectorView;

		public GameViewSide(Player player, Board board, BoardView interactiveView, BoardView projectorView) {
			this.player = player;
			this.board = board;
			this.interactiveView = interactiveView;
			this.projectorView = projectorView;
		}

		public Player getPlayer() {
			return player;
		}

		public Board getBoard() {
			return board;
		}

		public BoardView getInteractiveView() {
			return interactiveView;
		}

		public BoardView getProjectorView() {
			return projectorView;
		}

		public void draw() {
			interactiveView.draw();
			projectorView.draw();
		}
		
		
	}
	
	public void draw() {
		leftSide.draw();
		rightSide.draw();
	}
}
