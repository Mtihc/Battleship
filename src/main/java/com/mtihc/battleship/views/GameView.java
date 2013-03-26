package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Game;

public class GameView {

	private GameViewSide leftSide;
	private GameViewSide rightSide;

	public GameView(Game game, OfflinePlayer leftPlayer, OfflinePlayer rightPlayer) {
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
		leftEnemyView.setLocationStrategy(BoardLocationStrategy.VERTICAL);
		rightEnemyView.setLocationStrategy(BoardLocationStrategy.VERTICAL);
		
		leftSide = new GameViewSide(leftPlayer, leftBoard, leftBoardView, leftEnemyView);
		rightSide = new GameViewSide(rightPlayer, rightBoard, rightBoardView, rightEnemyView);
	}
	
	/**
	 * The left GameViewSide
	 * @return left side of the view
	 */
	public GameViewSide getLeftSide() {
		return leftSide;
	}
	
	/**
	 * The right GameViewSide
	 * @return right side of the view
	 */
	public GameViewSide getRightSide() {
		return rightSide;
	}
	
	/**
	 * Inner class of GameView. Represents one side of the view. 
	 * 
	 * @author Mitch
	 *
	 */
	public class GameViewSide {
		
		private OfflinePlayer player;
		private Board board;
		private BoardView interactiveView;
		private BoardView projectorView;

		public GameViewSide(OfflinePlayer player, Board board, BoardView interactiveView, BoardView projectorView) {
			this.player = player;
			this.board = board;
			this.interactiveView = interactiveView;
			this.projectorView = projectorView;
		}

		/**
		 * The player that is playing on this side of the view
		 * @return the player
		 */
		public OfflinePlayer getPlayer() {
			return player;
		}

		/**
		 * The board model, that is being observed and displayed by this view
		 * @return the board model
		 */
		public Board getBoard() {
			return board;
		}

		/**
		 * The interactive board view. 
		 * 
		 * <p>This is the board to place your ships on. It will displaying the enemy's board later on, 
		 * so you can place bombs on it.</p>
		 * 
		 * @return the interactive board view.
		 */
		public BoardView getInteractiveView() {
			return interactiveView;
		}

		/**
		 * The projector board view.
		 * 
		 * <p>This is the board that will be displaying your ships, 
		 * exactly as you placed them on the interactive board view before. </p>
		 * 
		 * @return the projector board view
		 */
		public BoardView getProjectorView() {
			return projectorView;
		}

		/**
		 * Draws both board views, on this side of the game view.
		 */
		public void draw() {
			interactiveView.draw();
			projectorView.draw();
		}
		
		
	}
	
	/**
	 * Draws both sides of this game view.
	 */
	public void draw() {
		leftSide.draw();
		rightSide.draw();
	}
}
