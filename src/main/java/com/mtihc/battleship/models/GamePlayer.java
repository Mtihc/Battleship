package com.mtihc.battleship.models;

import org.bukkit.OfflinePlayer;

import com.mtihc.battleship.models.Game.GameBoard;

public class GamePlayer {

	private OfflinePlayer player;
	private GamePlayerInventory inventory;
	
	GameBoard board;

	public GamePlayer(OfflinePlayer player) {
		this.player = player;
	}

	public String getName() {
		return player.getName();
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	public GameBoard getBoard() {
		return board;
	}
	
	public Game getGame() {
		return board.getGame();
	}
	
	public GamePlayerInventory getOriginalInventory() {
		return inventory;
	}
	
	public void setOriginalInventory(GamePlayerInventory inventory) {
		this.inventory = inventory;
	}
	
}
