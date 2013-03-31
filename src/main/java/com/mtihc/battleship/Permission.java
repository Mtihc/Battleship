package com.mtihc.battleship;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum Permission {

	CREATE("battleship.create"),
	DELETE("battleship.delete"),
	INVITER("battleship.inviter"),
	INVITABLE("battleship.invitable");
	
	private String permission;

	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermision() {
		return permission;
	}
	
	public boolean testPermission(CommandSender sender) {
		if(!sender.hasPermission(permission)) {
			sender.sendMessage(ChatColor.RED + "You don't have permission \'" + permission + "\'.");
			return false;
		}
		else {
			return true;
		}
	}
}
