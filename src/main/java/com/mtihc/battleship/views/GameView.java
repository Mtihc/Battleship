package com.mtihc.battleship.views;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Board.Ship;
import com.mtihc.battleship.models.Board.Tile;
import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.Game.GameBoard;

public class GameView {
	
	private Game game;
	private GameSideView leftSide;
	private GameSideView rightSide;
	
	private BlockFace face;
	
	private BoardObserver observer = new BoardObserver();

	public GameView(Game game) {
		this.game = game;
		
		game.getLeftBoard().addObserver(observer);
		game.getRightBoard().addObserver(observer);
		
		Location origin = game.getOrigin();
		
		// set facing directions
		face = yawToFace(origin.getYaw());
		BlockFace left = yawToFace(origin.getYaw() - 90);
		BlockFace right = yawToFace(origin.getYaw() + 90);
		
		//
		// set origin locations for the views at the top-left (relative to their facing direction)
		// 
		// left side's origin is a little to the left and all the way forward
		Location leftOrigin = origin.getBlock().getRelative(left, 3).getRelative(face, game.getWidth()).getLocation();
		leftOrigin.setYaw(origin.getYaw() + 90);
		// right side's origin is a little to the right and that's about it. 
		Location rightOrigin = origin.getBlock().getRelative(right, 3).getRelative(face, 1).getLocation();
		rightOrigin.setYaw(origin.getYaw() - 90);
		
		// create GameSideViews
		leftSide = new GameSideView(game.getLeftBoard(), leftOrigin, right);
		rightSide = new GameSideView(game.getRightBoard(), rightOrigin, left);
		// GameSideViews know their enemy
		leftSide.otherSide = rightSide;
		rightSide.otherSide = leftSide;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Location getOrigin() {
		return game.getOrigin();
	}
	
	public BlockFace getFace() {
		return face;
	}
	
	public GameSideView getLeftSideView() {
		return leftSide;
	}
	
	public GameSideView getRightSideView() {
		return rightSide;
	}

	
	
	
	
	
	public class GameSideView {

		private GameSideView otherSide;
		
		private GameBoard board;
		private Location origin;
		private BlockFace face;
		
		private BoardView interactiveView;
		private BoardView projectorView;

		GameSideView(GameBoard board, Location origin, BlockFace face) {
			this.board = board;
			this.origin = origin;
			this.face = face;
			
			this.interactiveView = new BoardView(board, origin, face);
			this.interactiveView.setDrawStrategy(BoardDrawStrategy.NORMAL);
			this.interactiveView.setLocationStrategy(BoardLocationStrategy.HORIZONTAL);
			
			this.projectorView = new BoardView(board.getOtherBoard(), origin, face);
			this.projectorView.setDrawStrategy(BoardDrawStrategy.HIDE_SHIPS);
			this.projectorView.setLocationStrategy(BoardLocationStrategy.VERTICAL);
		}
		
		public GameSideView getOtherSideView() {
			return otherSide;
		}
		
		public GameBoard getGameBoard() {
			return board;
		}
		
		public Location getOrigin() {
			return origin.clone();
		}
		
		public BlockFace getFace() {
			return face;
		}

		public BoardView getInteractiveView() {
			return interactiveView;
		}
		
		public BoardView getProjectorView() {
			return projectorView;
		}
		
		/**
		 * Let the projector view switch models and draw strategy with the projector view.
		 */
		public void switchViews() {
			// switch draw strategy
			BoardDrawStrategy s = interactiveView.getDrawStrategy();
			interactiveView.setDrawStrategy(projectorView.getDrawStrategy());
			projectorView.setDrawStrategy(s);
			
			// switch boards
			Board b = interactiveView.getBoard();
			interactiveView.setBoard(projectorView.getBoard());
			projectorView.setBoard(b);
			
			// redraw
			interactiveView.draw();
			projectorView.draw();
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
	
	


	// only using SOUTH, WEST, NORTH, EAST
	public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };

	/**
	 * Convert a yaw value to one of the following BlockFace enum values: NORTH, EAST, SOUTH or WEST.
	 * @param yaw the yaw value
	 * @return the BlockFace enum value
	 */
	public static BlockFace yawToFace(float yaw) {
		return axis[Math.round(yaw / 90f) & 0x3];
	}
	
	/**
	 * Convert a BlockFace enum value, to a yaw value. Only accepts: NORTH, EAST, SOUTH, WEST.
	 * Other values will result in 0, which happens to be south.
	 * @param face the BlockFace enum value
	 * @return the yaw value
	 */
	public static float faceToYaw(BlockFace face) {
		switch(face) {
		case EAST:
			return -90;
		case NORTH:
			return 180;
		case WEST:
			return 90;
		case SOUTH:
			return 0;
		default:
			return 0;
		}
	}
	

	
	/**
	 * Get the center location of this board view. (assuming the horizontal location strategy is used)
	 * 
	 * @return the center location
	 */
	public static Location getCenterLocation(BoardView boardView) {
		Location result = boardView.tileToLocation(boardView.getBoard().getWidth() / 2, boardView.getBoard().getHeight() / 2);
		result.setYaw(faceToYaw(boardView.getFace()));
		result.setPitch(0);
		return result;
	}
	

	class BoardObserver implements Board.Observer {

		@Override
		public void onMiss(Board board, Tile tile) {
			
		}

		@Override
		public void onHit(Board board, Tile tile) {
			
		}

		@Override
		public void onShipPlace(Board board, Ship ship) {
			
		}

		@Override
		public void onAllShipsPlaced(Board board) {
			if(board == game.getLeftBoard()) {
				leftSide.switchViews();
			}
			else if(board == game.getRightBoard()) {
				rightSide.switchViews();
			}
		}

		@Override
		public void onShipRemove(Board board, Ship ship) {
			
		}

		@Override
		public void onShipDestroyed(Board board, Ship ship) {
			
		}

		@Override
		public void onAllShipsDestroyed(Board board) {
			
		}
		
	}
}
