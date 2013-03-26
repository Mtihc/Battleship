package com.mtihc.battleship;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.battleship.controllers.GameManager;
import com.mtihc.battleship.controllers.InvitationManager;
import com.mtihc.battleship.models.Game;
import com.mtihc.battleship.models.GameRepository;
import com.mtihc.battleship.models.GameYamlRepository;
import com.mtihc.battleship.models.ShipType;

public class BattleshipPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		// create game yml repository
		String directory = getDataFolder() + "/games";
		GameRepository repository = new GameYamlRepository(directory);
		
		// create managers
		try {
			new InvitationManager(this);
			new GameManager(this, repository);
		}
		catch(Exception e) {
			// already exist
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
					Game game = new Game("test", 10, 10, player.getLocation(), shipTypes);
					// TODO invite/challenge system
					GameManager.getInstance().initialize(game, player, player);
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
