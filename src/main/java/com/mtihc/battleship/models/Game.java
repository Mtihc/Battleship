package com.mtihc.battleship.models;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Game implements ConfigurationSerializable {

	private int width;
	private int height;
	private Location origin;
	private ShipType[] shipTypes;
	
	public Game(int width, int height, Location origin, ShipType[] shipTypes) {
		this.width = width;
		this.height = height;
		this.origin = origin;
		this.shipTypes = shipTypes;
	}
	
	/**
	 * Constructor used for deserialization
	 * @param values the loaded values
	 */
	public Game(Map<String, Object> values) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, Object> serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public ShipType[] getShipTypes() {
		return shipTypes;
	}

	public void setShipTypes(ShipType[] shipTypes) {
		this.shipTypes = shipTypes;
	}

}
