package com.mtihc.battleship.controllers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.views.GameView;

public class GameController {

	private JavaPlugin plugin;
	private Player leftPlayer;
	private Player rightPlayer;
	private Game game;
	private GameView view;
	
	public GameController(JavaPlugin plugin, Player leftPlayer, Player rightPlayer, Game game) {
		this.plugin = plugin;
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
		this.game = game;
		this.view = new GameView(game, leftPlayer, rightPlayer);
	}
	
	
	
	public void initialize() {
		view.draw();
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public GameView getView() {
		return view;
	}

	public Player getLeftPlayer() {
		return leftPlayer;
	}

	public Player getRightPlayer() {
		return rightPlayer;
	}

}
