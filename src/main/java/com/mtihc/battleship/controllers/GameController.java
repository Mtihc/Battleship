package com.mtihc.battleship.controllers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.views.GameView;

public class GameController {

	private JavaPlugin plugin;
	private Game game;
	private GameView view;
	
	public GameController(JavaPlugin plugin, Player leftPlayer, Player rightPlayer, Game game) {
		this.plugin = plugin;
		this.game = game;
		this.view = new GameView(game, leftPlayer, rightPlayer);
	}
	
	
	
	public void initialize() {
		view.draw();
		// TODO 
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Game getGame() {
		return game;
	}
	
	public GameView getView() {
		return view;
	}

	public Player getLeftPlayer() {
		return view.getLeftSide().getPlayer();
	}

	public Player getRightPlayer() {
		return view.getRightSide().getPlayer();
	}

}
