package com.mtihc.battleship.models;

import org.bukkit.Material;

public enum ShipType {

	PATROL_BOAT		 (2, "Patrol Boat",		 Material.WOOD),
	DESTROYER		 (3, "Destroyer",		 Material.WOOD, (byte) 2),
	SUBMARINE		 (3, "Submarine",		 Material.IRON_BLOCK),
	BATTLESHIP		 (4, "Battleship",		 Material.GOLD_BLOCK),
	AIRCRAFT_CARRIER (5, "Aircraft Carrier", Material.DIAMOND_BLOCK);
	
	
	
	private String name;
	private int size;
	private Material material;
	private byte materialData;
	
	private ShipType(int size, String name, Material material) {
		this(size, name, material, (byte) 0);
	}

	private ShipType(int size, String name, Material material, byte materialData) {
		this.name = name;
		this.size = size;
		this.material = material;
		this.materialData = materialData;
	}

	public String getShipName() {
		return name;
	}

	public int getShipSize() {
		return size;
	}

	public Material getShipMaterial() {
		return material;
	}

	public byte getShipMaterialData() {
		return materialData;
	}
}
