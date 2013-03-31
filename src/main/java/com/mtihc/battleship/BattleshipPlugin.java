package com.mtihc.battleship;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.controllers.GameController;
import com.mtihc.battleship.controllers.GameException;
import com.mtihc.battleship.controllers.GameManager;
import com.mtihc.battleship.models.GameData;
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
			if(args.length > 0) {
				subcommand = args[0];
				// TODO add commands
				if(subcommand.equalsIgnoreCase("start")) {
					Bukkit.getLogger().info("start command!");
					Player player = (Player) sender;
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
					GameData data = new GameData("test", 10, 10, player.getLocation(), shipTypes);
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
