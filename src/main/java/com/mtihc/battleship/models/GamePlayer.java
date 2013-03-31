package com.mtihc.battleship.models;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.mtihc.battleship.models.Game.GameBoard;

public class GamePlayer {

	private OfflinePlayer player;
	private Inventory inventory;
	
	GameBoard board;

	public GamePlayer(OfflinePlayer player) {
		this.player = player;
	}

	public String getName() {
		return player.getName();
	}
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	public GameBoard getBoard() {
		return board;
	}
	
	public Game getGame() {
		return board.getGame();
	}
	
	public Inventory getOriginalInventory() {
		return inventory;
	}
	
	public void setOriginalInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public class Inventory {
		
		private ItemStack[] contents;
		private ItemStack[] armorContents;
		
		public Inventory(org.bukkit.inventory.PlayerInventory inventory) {
			this(inventory.getContents(), inventory.getArmorContents());
		}

		public Inventory(ItemStack[] contents, ItemStack[] armorContents) {
			this.contents = contents;
			this.armorContents = armorContents;
		}
		
		public ItemStack[] getContents() {
			return contents;
		}
		
		public ItemStack[] getArmorContents() {
			return armorContents;
		}
		
		public void setToBukkitInventory(org.bukkit.inventory.PlayerInventory inventory) {
			inventory.setContents(contents);
			inventory.setArmorContents(armorContents);
		}
	}
	
}
