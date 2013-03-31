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
		
	}
}