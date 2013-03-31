package com.mtihc.battleship.controllers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.GameData;
import com.mtihc.battleship.models.GameDataRepository;
import com.mtihc.battleship.models.GamePlayer;

public class GameManager {

	// singleton
	private static GameManager instance;

	/**
	 * Returns the singleton GameManager. 
	 * @return the game manager
	 */
	public static GameManager getInstance() {
		return instance;
	}
	
	
	
	
	
	private JavaPlugin plugin;
	private GameDataRepository repo;
	private HashMap<String, GameController> games = new HashMap<String, GameController>();
	private HashMap<String, GamePlayerController> players = new HashMap<String, GamePlayerController>();

	/**
	 * Constructor.
	 * 
	 * @param plugin
	 *            The plugin
	 * @throws Exception
	 *             when the GameManager is already created.
	 */
	public GameManager(JavaPlugin plugin, GameDataRepository repository) throws Exception {
		
		// singleton, can only be created once
		if (instance != null) {
			throw new Exception(
					"GameManager is already created. Use static method getInstance() instead.");
		}
		instance = this;

		// create invitation manager, just in case
		try {
			new InvitationManager(plugin);
		} catch(Exception e) {
			// already exists
		}
		
		this.plugin = plugin;
		this.repo = repository;
		
		GameEventHandler listener = new GameEventHandler(this);
		Bukkit.getPluginManager().registerEvents(listener, plugin);
	}
	
	/**
	 * Returns the plugin
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Returns the GameDataRepository.
	 * @return the game repository.
	 */
	public GameDataRepository getGameRepository() {
		return repo;
	}
	
	/**
	 * Returns whether a game with the specified id is currently running.
	 * @param gameId the game id
	 * @return whether the game is running
	 */
	public boolean hasGame(String gameId) {
		return games.containsKey(gameId);
	}

	public boolean hasPlayer(String name) {
		return players.containsKey(name);
	}
	
	public GamePlayerController getPlayer(String name) {
		return players.get(name);
	}

	/**
	 * Initialize a game. 
	 * 
	 * @param settings the game settings to initialize
	 * @param leftPlayer the left player
	 * @param rightPlayer the right player
	 * @throws GameException when game is already being played
	 */
	public GameController createController(GameData settings, Player leftPlayer, Player rightPlayer) throws GameException {
		if(games.containsKey(settings.getId())) {
			throw new GameException("Somebody is already playing at \"" + settings.getId() + "\".");
		}
		if(hasPlayer(leftPlayer.getName())) {
			throw new GameException(leftPlayer.getName() + " is already playing a game.");
		}
		if(hasPlayer(rightPlayer.getName())) {
			throw new GameException(rightPlayer + " is already playing a game.");
		}
		
		GamePlayer left = new GamePlayer(leftPlayer);
		GamePlayer right = new GamePlayer(rightPlayer);
		Game game = new Game(settings, left, right);
		
		GameController controller = new GameController(plugin, game);
		// TODO remove from the map. at some point
		games.put(settings.getId(), controller);
		// TODO remove both from the map, at some point
		players.put(left.getName(), controller.getLeftPlayerController());
		players.put(right.getName(), controller.getRightPlayerController());
		
		return controller;
	}
	
	

}
