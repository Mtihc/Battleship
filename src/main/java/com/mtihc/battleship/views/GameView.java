package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Game;

public class GameView {

	private GameViewSide leftSide;
	private GameViewSide rightSide;
	
	private BlockFace forward;
	private BlockFace left;
	private BlockFace right;

	public GameView(Game game, OfflinePlayer leftPlayer, OfflinePlayer rightPlayer) {
		// create boards (model)
		Board leftBoard = game.getLeftBoard();
		Board rightBoard = game.getRightBoard();
		
		Location origin = game.getOrigin();
		
		// set facing directions
		forward = BoardView.yawToFace(origin.getYaw());
		left = BoardView.yawToFace(origin.getYaw() - 90);
		right = BoardView.yawToFace(origin.getYaw() + 90);
		
		//
		// set origin locations for the views at the top-left (relative to their facing direction)
		// 
		// left side's origin is a little to the left and all the way forward
		Location leftOrigin = origin.getBlock().getRelative(left, 3).getRelative(forward, game.getWidth()).getLocation();
		// right side's origin is a little to the right and that's about it. 
		Location rightOrigin = origin.getBlock().getRelative(right, 3).getRelative(forward, 1).getLocation();
		
		leftSide = new GameViewSide(leftPlayer, leftBoard, rightBoard, leftOrigin, left);
		rightSide = new GameViewSide(rightPlayer, rightBoard, rightBoard, rightOrigin, right);
		rightSide.enemy = leftSide;
		leftSide.enemy = rightSide;
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
		private GameViewSide enemy;

		public GameViewSide(OfflinePlayer player, Board board, Board enemy, Location origin, BlockFace facing) {
			this.player = player;
			this.board = board;
			this.interactiveView = new BoardView(board, origin, facing);
			this.projectorView = new BoardView(enemy, origin, facing);
			this.projectorView.setDrawStrategy(BoardDrawStrategy.HIDE_SHIPS);
			this.projectorView.setLocationStrategy(BoardLocationStrategy.VERTICAL);
		}
		
		/**
		 * Returns the other GameViewSide
		 * @return the other side
		 */
		public GameViewSide getEnemy() {
			return enemy;
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
