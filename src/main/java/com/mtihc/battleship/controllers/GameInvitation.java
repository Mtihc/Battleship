package com.mtihc.battleship.controllers;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import com.mtihc.battleship.models.Game;

public class GameInvitation extends Invitation {

	private String gameId;
	private String acceptCommandUsage;
	private String denyCommandUsage;
	private Game game;

	public GameInvitation(Player sender, String receiverName, String gameId) {
		this(sender, receiverName, gameId, 1200L);
	}

	public GameInvitation(Player sender, String receiverName, String gameId, long cancelDelay) {
		super(sender, receiverName, cancelDelay);
		this.gameId = gameId;
	}

	@Override
	protected void onSend() throws InvitationException {
		super.onSend();

		GameManager mgr = GameManager.getInstance();

		OfflinePlayer sender = getSender();
		OfflinePlayer receiver = getReceiver();
		
		if (mgr.hasGame(gameId)) {
			throw new InvitationException("Somebody is already playing \"" + gameId + "\".");
		}

		Game game;
		try {
			game = mgr.getGameRepository().getGame(gameId);
		} catch (IOException e) {
			String message = "Failed to load game \"" + gameId + "\" due to IOException.";
			mgr.getPlugin().getLogger().log(Level.WARNING, message, e);
			throw new InvitationException(message);
		} catch (InvalidConfigurationException e) {
			String message = "Failed to load game \"" + gameId + "\" due to InvalidConfigurationException.";
			mgr.getPlugin().getLogger().log(Level.WARNING, message, e);
			throw new InvitationException(message);
		}

		if (game == null) {
			throw new InvitationException("Game \"" + gameId + "\" does not exist.");
		}

		setMessages(new String[] {
				ChatColor.WHITE + sender.getName() + ChatColor.GOLD + " has invited you to play a game of Battleships at " + ChatColor.WHITE + gameId + ChatColor.GOLD + ".",
				ChatColor.GOLD + "  To accept the invitation, type " + ChatColor.WHITE + getAcceptCommandUsage(),
				ChatColor.GOLD + "  To deny the invitation, type " + ChatColor.WHITE + getDenyCommandUsage() });

		this.game = game;
		
		if (sender.getPlayer() != null)
			sender.getPlayer().sendMessage(ChatColor.GOLD + "The invite was sent to " + ChatColor.WHITE + receiver.getName());
	}

	@Override
	protected void onAccept() throws InvitationException {
		super.onAccept();

		GameManager mgr = GameManager.getInstance();
		Player sender = getSender().getPlayer();
		Player receiver = getReceiver().getPlayer();
		
		sender.sendMessage(ChatColor.WHITE + receiver.getName() + ChatColor.GOLD + " accepted your request.");
		receiver.sendMessage(ChatColor.GOLD + "You accepted the request of " + ChatColor.WHITE + sender.getName() + ChatColor.GOLD + ".");

		// TODO initialize game
		try {
			mgr.createController(game, sender, receiver).initialize();
		} catch (GameException e) {
			throw new InvitationException("Game could not be initialized due to a GameException: " + e.getMessage(), e);
		}
	}

	@Override
	protected void onDeny() throws InvitationException {
		super.onDeny();

		Player sender = getSender().getPlayer();
		Player receiver = getReceiver().getPlayer();

		if (sender != null)
			sender.sendMessage(ChatColor.WHITE + receiver.getName() + ChatColor.RED + " denied your request.");
		if (receiver != null)
			receiver.sendMessage(ChatColor.YELLOW + "You denied the request of " + ChatColor.WHITE + sender.getName() + ChatColor.YELLOW + ".");
	}

	@Override
	protected void onCancel() {
		super.onCancel();

		Player sender = getSender().getPlayer();
		Player receiver = getReceiver().getPlayer();

		if (sender != null)
			sender.sendMessage(ChatColor.YELLOW + "Cancelled the invite that was sent to " + ChatColor.WHITE + receiver.getName());
		if (receiver != null)
			receiver.sendMessage(ChatColor.WHITE + sender.getName() + ChatColor.YELLOW + " cancelled the invite.");

	}

	@Override
	protected void onCancelByTimeout() {
		super.onCancelByTimeout();

		Player sender = getSender().getPlayer();
		Player receiver = getReceiver().getPlayer();

		if (sender != null)
			sender.sendMessage(ChatColor.YELLOW + "The invite was cancelled. "
					+ ChatColor.WHITE + receiver.getDisplayName()
					+ ChatColor.YELLOW + " waited too long to respond.");
		if (receiver != null)
			receiver.sendMessage(ChatColor.YELLOW + "The invite from "
					+ ChatColor.WHITE + sender.getDisplayName()
					+ ChatColor.YELLOW
					+ " was cancelled. You waited too long to respond.");

	}

	public String getAcceptCommandUsage() {
		return acceptCommandUsage;
	}

	public void setAcceptCommandUsage(String acceptCommandUsage) {
		this.acceptCommandUsage = acceptCommandUsage;
	}

	public String getDenyCommandUsage() {
		return denyCommandUsage;
	}

	public void setDenyCommandUsage(String denyCommandUsage) {
		this.denyCommandUsage = denyCommandUsage;
	}

}
