package com.mtihc.battleship.models;



public class Game extends GameData {
	
	private GameBoard leftBoard;
	private GameBoard rightBoard;

	public Game(GameData data, GamePlayer leftPlayer, GamePlayer rightPlayer) {
		super(data);
		leftBoard = new GameBoard(leftPlayer);
		rightBoard = new GameBoard(rightPlayer);
		leftBoard.enemy = rightBoard;
		rightBoard.enemy = leftBoard;
		leftPlayer.board = leftBoard;
		rightPlayer.board = rightBoard;
	}
	
	public GameBoard getLeftBoard() {
		return leftBoard;
	}
	
	public GameBoard getRightBoard() {
		return rightBoard;
	}

	public class GameBoard extends Board {

		private GamePlayer player;
		private GameBoard enemy;

		GameBoard(GamePlayer player) {
			super(
					Game.this.getWidth(), 
					Game.this.getHeight(), 
					Game.this.getShipTypes());
			
			this.player = player;
		}
		
		public Game getGame() {
			return Game.this;
		}
		
		public GamePlayer getGamePlayer() {
			return player;
		}

		public GameBoard getOtherBoard() {
			return enemy;
		}
		


		/**
		 * Returns whether all ships are placed on the board
		 * @return true if all ships are placed, false otherwise
		 */
		public boolean areAllShipsPlaced() {
			Ship[] ships = player.getBoard().getShips();
			for (Ship ship : ships) {
				if(!ship.isPlaced()) {
					return false;
				}
			}
			return true;
		}

		/**
		 * Returns wether all ships are destroyed on the board
		 * @return true if all ships are destroyed, false otherwise
		 */
		public boolean areAllShipsDestroyed() {
			Ship[] ships = player.getBoard().getShips();
			for (Ship ship : ships) {
				if(!ship.isDestroyed()) {
					return false;
				}
			}
			return true;
		}
	}
}