package com.mtihc.battleship.controllers;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Board.Ship;
import com.mtihc.battleship.models.Board.Tile;
import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.GamePlayer;
import com.mtihc.battleship.models.GamePlayerInventory;
import com.mtihc.battleship.models.ShipType;
import com.mtihc.battleship.views.GameView;

public class GameController {

	private JavaPlugin plugin;
	private Game game;
	private GameView view;
	private GamePlayerController leftPlayer;
	private GamePlayerController rightPlayer;
	
	private GamePlayerController currentPlayer = null;
	private GamePlayerController winner = null;
	private boolean started = false;
	private boolean stopped = false;
	private String state = null;

	GameController(JavaPlugin plugin, Game game) {
		this.plugin = plugin;
		this.game = game;
		this.view = new GameView(game);
		
		GamePlayer leftPlayer = game.getLeftBoard().getGamePlayer();
		GamePlayer rightPlayer = game.getRightBoard().getGamePlayer();
		
		this.leftPlayer = new GamePlayerController(this, leftPlayer, view.getLeftSideView());
		this.rightPlayer = new GamePlayerController(this, rightPlayer, view.getRightSideView());
		this.leftPlayer.enemy = this.rightPlayer;
		this.rightPlayer.enemy = this.leftPlayer;
		
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Game getGame() {
		return game;
	}

	public GameView getGameView() {
		return view;
	}

	public GamePlayerController getLeftPlayerController() {
		return leftPlayer;
	}

	public GamePlayerController getRightPlayerController() {
		return rightPlayer;
	}
	
	public GamePlayerController getCurrentPlayerController() {
		return currentPlayer;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean isStopped() {
		return stopped;
	}
	
	public boolean isRunning() {
		return started && !stopped && winner == null;
	}
	
	public boolean hasWinner() {
		return winner != null;
	}
	
	public GamePlayerController getWinner() {
		return winner;
	}
	
	public String getState() {
		return state;
	}
	
	private void switchTurns() {
		if(currentPlayer == leftPlayer) {
			currentPlayer = rightPlayer;
		}
		else if(currentPlayer == rightPlayer) {
			currentPlayer = leftPlayer;
		}
		else {
			currentPlayer = rightPlayer;
		}
		updateState();
	}
	
	protected void updateState() {
		if(started) {
			if(!stopped) {
				if(!game.areAllShipsPlaced()) {
					state = "started";
				}
				else {
					if(currentPlayer == leftPlayer) {
						state = "leftPlayer";
					}
					else if(currentPlayer == rightPlayer) {
						state = "rightPlayer";
					}
					else {
						currentPlayer = rightPlayer;
						state = "rightPlayer";
					}
				}
				
			}
			else {
				if(winner != null) {
					state = "ended";
				}
				else {
					state = "stopped";
				}
			}
		}
		else {
			state = null;
		}
		
		
		
	}

	/**
	 * Throws an exception with appropriate text, when any of the players are offline.
	 * @throws GameException when both players are offline, or either one of them
	 */
	public void checkOnline() throws GameException {
		OfflinePlayer left = leftPlayer.getPlayer();
		OfflinePlayer right = rightPlayer.getPlayer();
		
		if(!left.isOnline() && !right.isOnline()) {
			throw new GameException(left.getName() + " and " + right.getName() + " are both offline.");
		}
		else if(!left.isOnline()) {
			throw new GameException(left.getName() + " is offline.");
		}
		else if(!right.isOnline()) {
			throw new GameException(right.getName() + " is offline.");
		}
	}
	
	/**
	 * Start the game. 
	 * 
	 * <p>Draws the board. Teleports players. </p>
	 * <p>And prepares to start placing ships.</p>
	 * 
	 * @throws GameException
	 */
	public void start() throws GameException {
		view.draw();
		
		checkOnline();
		
		Player leftPlayer = getLeftPlayerController().getPlayer().getPlayer();
		Player rightPlayer = getRightPlayerController().getPlayer().getPlayer();
		

		
		// save players' inventories and locations
		this.leftPlayer.getGamePlayer().setOriginalInventory(new GamePlayerInventory(leftPlayer.getInventory()));
		this.leftPlayer.getGamePlayer().setOriginalLocation(leftPlayer.getLocation());
		this.rightPlayer.getGamePlayer().setOriginalInventory(new GamePlayerInventory(rightPlayer.getInventory()));
		this.rightPlayer.getGamePlayer().setOriginalLocation(rightPlayer.getLocation());
		
		
		// 
		// Teleport players
		// 
		
		Location loc = GameView.getCenterLocation(view.getLeftSideView().getInteractiveView());
		loc.setY(loc.getY() + 1);
		leftPlayer.teleport(loc);
		
		loc = GameView.getCenterLocation(view.getRightSideView().getInteractiveView());
		loc.setY(loc.getY() + 1);
		rightPlayer.teleport(loc);
		
		
		// clear players' inventories
		leftPlayer.getInventory().clear();
		rightPlayer.getInventory().clear();
		
		// give ship items
		ShipType[] ships = game.getShipTypes();
		for (ShipType shipType : ships) {
			leftPlayer.getInventory().addItem(shipType.getNormal().toItemStack(shipType.getShipSize()));
			rightPlayer.getInventory().addItem(shipType.getNormal().toItemStack(shipType.getShipSize()));
		}
		leftPlayer.updateInventory();
		rightPlayer.updateInventory();
		
		started = true;
		updateState();
	}
	
	public void stop() {
		stop(null);
	}
	
	public void stop(GamePlayerController winner) {
		this.winner = winner;
		
		stopped = true;
		updateState();
		
		Player left = leftPlayer.getPlayer().getPlayer();
		Player right = rightPlayer.getPlayer().getPlayer();
		
		// give original inventory back
		leftPlayer.getGamePlayer().getOriginalInventory().setToBukkitInventory(left.getInventory());
		rightPlayer.getGamePlayer().getOriginalInventory().setToBukkitInventory(right.getInventory());
		left.updateInventory();
		right.updateInventory();
		
		// teleport to original location
		left.teleport(leftPlayer.getGamePlayer().getOriginalLocation());
		right.teleport(rightPlayer.getGamePlayer().getOriginalLocation());
		
		// remove from manager's lists
		GameManager.getInstance().games.remove(game.getId());
		GameManager.getInstance().players.remove(left.getName());
		GameManager.getInstance().players.remove(right.getName());
	}

	protected void onShipPlace(GamePlayerController gamePlayerController, Ship ship) {
		// TODO Auto-generated method stub
		
	}

	protected void onPlayerReady(GamePlayerController gamePlayerController) {
		updateState();
	}

	protected void onShipRemove(Ship ship, Tile[] shipTiles) {
		// TODO Auto-generated method stub
		
	}

	protected void onHit(GamePlayerController gamePlayerController, Tile tile) {
		switchTurns();
	}

	protected void onShipDestroyed(GamePlayerController gamePlayerController,
			Ship ship) {
		// TODO Auto-generated method stub
		
	}

	protected void onAllShipsDestroyed(GamePlayerController gamePlayerController) {
		stop(gamePlayerController.getEnemyController());
	}

	protected void onMiss(GamePlayerController gamePlayerController, Tile tile) {
		switchTurns();
	}

}
