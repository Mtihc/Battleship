package com.mtihc.battleship;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.controllers.GameController;
import com.mtihc.battleship.controllers.GameException;
import com.mtihc.battleship.controllers.GameInvitation;
import com.mtihc.battleship.controllers.GameManager;
import com.mtihc.battleship.controllers.InvitationException;
import com.mtihc.battleship.controllers.InvitationManager;
import com.mtihc.battleship.models.GameData;
import com.mtihc.battleship.models.GameDataRepository;
import com.mtihc.battleship.models.GameDataYamlRepository;
import com.mtihc.battleship.models.ShipType;

public class BattleshipPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		try {
			new GameManager(this, new GameDataYamlRepository(getDataFolder() + "/games"));
		} catch (Exception e) {
			// game manager already exists
		}
		
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(label.equalsIgnoreCase("battleship") || label.equalsIgnoreCase("bs")) {
			
			String subcommand;
			if(args.length == 0) {
				sendHelp(label, sender);
				return true;
			}
			
			subcommand = args[0];
			
			if(!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can execute battleship commands.");
				return true;
			}
			
			Player player = (Player) sender;
			
			// 
			// CREATE <id>
			// 
			if(subcommand.equalsIgnoreCase("create")) {
				if(!Permission.CREATE.testPermission(sender)) {
					return true;
				}
				
				String id;
				try {
					id = args[1];
				} catch(IndexOutOfBoundsException e) {
					sender.sendMessage(ChatColor.RED + "Expected a game id.");
					return true;
				}
				GameDataRepository repo = GameManager.getInstance().getGameRepository();
				if(repo.hasGame(id)) {
					sender.sendMessage(ChatColor.RED + "There's already a game called " + id + ".");
					return true;
				}
				GameData data = new GameData(id, 10, 10, player.getLocation(), ShipType.getDefaultShipTypes());
				
				try {
					repo.setGame(id, data);
				} catch (IOException e) {
					sender.sendMessage(ChatColor.RED + "Failed to save game " + id + ".");
					sender.sendMessage(ChatColor.RED + e.getClass().getName() + ": " + e.getMessage());
					return true;
				}
				sender.sendMessage(ChatColor.GREEN + "Game " + id + " saved.");
				return true;
			}
			else if(subcommand.equalsIgnoreCase("delete")) {
				if(!Permission.DELETE.testPermission(sender)) {
					return true;
				}
				String id;
				try {
					id = args[1];
				} catch(IndexOutOfBoundsException e) {
					sender.sendMessage(ChatColor.RED + "Expected a game id.");
					return true;
				}
				GameDataRepository repo = GameManager.getInstance().getGameRepository();
				if(!repo.hasGame(id)) {
					sender.sendMessage(ChatColor.RED + "Game " + id + " does not exist.");
					return true;
				}
				repo.deleteGame(id);
				sender.sendMessage(ChatColor.GREEN + "Game " + id + " deleted.");
				
			}
			// 
			// INVITE <player> <id>
			// 
			else if(subcommand.equalsIgnoreCase("invite")) {
				if(!Permission.INVITER.testPermission(sender)) {
					return true;
				}
				
				String id;
				String playerName;
				try {
					id = args[2];
					playerName = args[1];
				} catch(IndexOutOfBoundsException e) {
					sender.sendMessage(ChatColor.RED + "Expected a game id and player name.");
					return true;
				}
				GameDataRepository repo = GameManager.getInstance().getGameRepository();
				if(!repo.hasGame(id)) {
					sender.sendMessage(ChatColor.RED + "Game " + id + " does not exist.");
					return true;
				}
				OfflinePlayer otherPlayer = Bukkit.getOfflinePlayer(playerName);
				if(otherPlayer == null || !otherPlayer.hasPlayedBefore()) {
					sender.sendMessage(ChatColor.RED + "Player " + playerName + " does not exist.");
					return true;
				}
				if(!otherPlayer.isOnline()) {
					sender.sendMessage(ChatColor.RED + "Player " + playerName + " is offline.");
					return true;
				}
				if(!otherPlayer.getPlayer().hasPermission(Permission.INVITABLE.getPermision())) {
					sender.sendMessage(ChatColor.RED + "Player " + playerName + " does not have permission \'" + Permission.INVITABLE.getPermision() + "\'.'");
					return true;
				}
				try {
					GameInvitation invite = new GameInvitation(player, playerName, id);
					invite.setAcceptCommandUsage("/bs accept");
					invite.setDenyCommandUsage("/bs deny");
					InvitationManager.getInstance().invite(invite);
				} catch (InvitationException e) {
					sender.sendMessage(ChatColor.RED + e.getMessage());
					return true;
				}
				return true;
			}
			// 
			// ACCEPT
			// 
			else if(subcommand.equalsIgnoreCase("accept")) {
				try {
					InvitationManager.getInstance().accept(player);
				} catch (InvitationException e) {
					player.sendMessage(ChatColor.RED + e.getMessage());
				}
				return true;
			}
			// 
			// DENY
			// 
			else if(subcommand.equalsIgnoreCase("deny")) {
				try {
					InvitationManager.getInstance().deny(player);
				} catch (InvitationException e) {
					player.sendMessage(ChatColor.RED + e.getMessage());
				}
				return true;
			}
			// 
			// TEST
			// 
			// TODO remove test command!!
			else if(subcommand.equalsIgnoreCase("test")) {
				if(!sender.isOp()) {
					sender.sendMessage(ChatColor.RED + "You're not OP.");
					return true;
				}
				String id;
				try {
					id = args[1];
				} catch(IndexOutOfBoundsException e) {
					sender.sendMessage(ChatColor.RED + "Expected a game id.");
					return true;
				}
				GameDataRepository repo = GameManager.getInstance().getGameRepository();
				GameData data;
				try {
					data = repo.getGame(id);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Failed to load game " + id + ".");
					sender.sendMessage(ChatColor.RED + e.getClass().getName() + ": " + e.getMessage());
					return true;
				}
				if(data == null) {
					sender.sendMessage(ChatColor.RED + "Game " + id + " does not exist.");
					return true;
				}
				try {
					GameController controller = GameManager.getInstance().createController(data, player, player);
					controller.start();
				} catch (GameException e) {
					player.sendMessage(ChatColor.RED + e.getMessage());
					return true;
				}
				
			}
			else {
				sender.sendMessage("Unknown command: /battleship " + subcommand);
				sender.sendMessage("For command help, execute /battleship");
				return true;
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	
	private void sendHelp(String label, CommandSender sender) {
		
		sender.sendMessage("/" + label + " create <id> " + " Create a new game at your location.");
		sender.sendMessage("/" + label + " invite <player> <id> " + " Invite a player to a game.");
		sender.sendMessage("/" + label + " accept " + " Accept a received invite.");
		sender.sendMessage("/" + label + " deny " + " Deny a received invite.");
	}
	
}
