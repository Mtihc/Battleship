package com.mtihc.battleship;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.controllers.GameController;
import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.GameRepository;

public class GameManager {

	private static GameManager instance;

	/**
	 * Returns the singleton GameManager. 
	 * @return the game manager
	 */
	public static GameManager getInstance() {
		return instance;
	}
	
	
	
	
	
	private JavaPlugin plugin;
	private GameRepository repo;
	private HashMap<String, GameController> games = new HashMap<String, GameController>();

	/**
	 * Constructor.
	 * 
	 * @param plugin
	 *            The plugin
	 * @throws Exception
	 *             when the GameManager is already created.
	 */
	public GameManager(JavaPlugin plugin, GameRepository repository) throws Exception {
		
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
	}
	
	/**
	 * Returns the plugin
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Returns the GameRepository.
	 * @return the game repository.
	 */
	public GameRepository getGameRepository() {
		return repo;
	}
	
	/**
	 * Returns whether a game with the specified id is currently running.
	 * @param gameId the game id
	 * @return whether the game is running
	 */
	public boolean hasRunningGame(String gameId) {
		return games.containsKey(gameId);
	}

	/**
	 * Initialize a game. 
	 * 
	 * @param game the game to initialize
	 * @param leftPlayer the left player
	 * @param rightPlayer the right player
	 */
	public void initialize(Game game, Player leftPlayer, Player rightPlayer) {
		
		GameController controller = new GameController(plugin, leftPlayer, rightPlayer, game);
		games.put(game.getId(), controller);
		
		controller.initialize();
	}
	
	

}
