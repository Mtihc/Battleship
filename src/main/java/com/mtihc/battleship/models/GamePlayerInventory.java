package com.mtihc.battleship.models;

import org.bukkit.inventory.ItemStack;

public class GamePlayerInventory {
	
	private ItemStack[] contents;
	private ItemStack[] armorContents;
	
	public GamePlayerInventory(org.bukkit.inventory.PlayerInventory inventory) {
		this(inventory.getContents(), inventory.getArmorContents());
	}

	public GamePlayerInventory(ItemStack[] contents, ItemStack[] armorContents) {
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
