package com.mtihc.battleship.controllers;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mtihc.battleship.views.GameView;
import com.mtihc.battleship.views.GameView.GameViewSide;

public class GamePlayer {

	private OfflinePlayer player;
	GameController controller;
	GameViewSide view;

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
	
	public GameView.GameViewSide getView() {
		return view;
	}

	protected void onPlayerInteract(PlayerInteractEvent event) {
		// TODO Auto-generated method stub
		
	}

}
