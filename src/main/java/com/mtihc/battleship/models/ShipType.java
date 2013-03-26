package com.mtihc.battleship.models;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public enum ShipType {

	PATROL_BOAT		 (2, "Patrol Boat",		 Material.WOOL.getNewData((byte) 15), 			Material.COAL_ORE.getNewData((byte) 0)),
	DESTROYER		 (3, "Destroyer",		 Material.IRON_BLOCK.getNewData((byte) 0), 		Material.IRON_ORE.getNewData((byte) 0)),
	SUBMARINE		 (3, "Submarine",		 Material.GOLD_BLOCK.getNewData((byte) 0), 		Material.GOLD_ORE.getNewData((byte) 0)),
	BATTLESHIP		 (4, "Battleship",		 Material.DIAMOND_BLOCK.getNewData((byte) 0), 	Material.DIAMOND_ORE.getNewData((byte) 0)),
	AIRCRAFT_CARRIER (5, "Aircraft Carrier", Material.EMERALD_BLOCK.getNewData((byte) 0), 	Material.EMERALD_ORE.getNewData((byte) 0));
	
	
	
	private String name;
	private int size;
	private MaterialData normal;
	private MaterialData damaged;
	
	private ShipType(int size, String name, MaterialData normal, MaterialData damaged) {
		this.name = name;
		this.size = size;
		this.normal = normal;
		this.damaged = damaged;
	}

	public String getShipName() {
		return name;
	}

	public int getShipSize() {
		return size;
	}
	
	public boolean isSimilar(ItemStack item) {
		return item != null && normal.getItemTypeId() == item.getTypeId() && normal.getData() == item.getData().getData();
	}

	public MaterialData getNormal() {
		return normal;
	}

	public MaterialData getDamaged() {
		return damaged;
	}
}
