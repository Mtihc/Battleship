package com.mtihc.battleship.controllers;

import org.bukkit.OfflinePlayer;

public class GamePlayer {

	private OfflinePlayer player;
	GameController controller;

	public GamePlayer(OfflinePlayer player) {
		this.player = player;
	}

	public String getName() {
		return player.getName();
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	public GameController getController() {
		return controller;
	}

}
