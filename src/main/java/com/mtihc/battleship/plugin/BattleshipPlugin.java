package com.mtihc.battleship.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.controllers.GameController;

public class BattleshipPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(label.equalsIgnoreCase("battleship") || label.equalsIgnoreCase("bs")) {
			
			String subcommand;
			if(args.length > 0) {
				subcommand = args[0];
				
				if(subcommand.equalsIgnoreCase("start")) {
					// TODO add commands
					new GameController(10, 10, ((Player) sender).getLocation()).initialize();
				}
				else {
					sender.sendMessage("Unknown command: /battleship " + subcommand);
					sender.sendMessage("For command help, execute /battleship");
					return true;
				}
				
			}
			else {
				sendHelp(sender);
				return true;
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	
	private void sendHelp(CommandSender sender) {
		// TODO help messages
		sender.sendMessage("TODO: Add help messages");
	}
	
}
