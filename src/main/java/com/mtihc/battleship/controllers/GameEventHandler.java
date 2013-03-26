package com.mtihc.battleship.controllers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameEventHandler implements Listener {

	private GameManager mgr;

	GameEventHandler(GameManager mgr) {
		this.mgr = mgr;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		String name = event.getPlayer().getName();
		
		if(mgr.hasPlayer(name)) {
			GamePlayer player = mgr.getPlayer(name);
			player.onPlayerInteract(event);
		}
	}
	
}
