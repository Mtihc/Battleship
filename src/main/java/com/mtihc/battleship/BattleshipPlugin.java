package com.mtihc.battleship;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.ShipType;
import com.mtihc.battleship.views.GameView;

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
					ShipType[] shipTypes = new ShipType[] {
							ShipType.PATROL_BOAT,
							ShipType.PATROL_BOAT,
							ShipType.PATROL_BOAT,
							ShipType.PATROL_BOAT,
							ShipType.DESTROYER,
							ShipType.DESTROYER,
							ShipType.DESTROYER,
							ShipType.SUBMARINE,
							ShipType.SUBMARINE,
							ShipType.SUBMARINE,
							ShipType.BATTLESHIP,
							ShipType.AIRCRAFT_CARRIER,
					};
					Game game = new Game(10, 10, ((Player) sender).getLocation(), shipTypes);
					new GameView(game).initialize();
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
