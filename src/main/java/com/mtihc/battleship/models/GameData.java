package com.mtihc.battleship.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

public class GameData implements ConfigurationSerializable {

	private String id;
	private int width;
	private int height;
	private Location origin;
	private ShipType[] shipTypes;
	
	public GameData(GameData data) {
		this(data.id, data.width, data.height, data.origin, data.shipTypes);
	}
	
	public GameData(String id, int width, int height, Location origin, ShipType[] shipTypes) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.origin = origin;
		this.shipTypes = shipTypes;
	}
	
	/**
	 * Constructor used for deserialization
	 * @param values the loaded values
	 */
	public GameData(Map<String, Object> values) {
		this.id = (String) values.get("id");
		this.width = (Integer) values.get("width");
		this.height = (Integer) values.get("height");
		
		World world = Bukkit.getWorld((String) values.get("world"));
		Vector vec = (Vector) values.get("location");
		this.origin = vec.toLocation(world);
		
		List<?> shipTypeList = (List<?>) values.get("ship-types");
		this.shipTypes = new ShipType[shipTypeList.size()];
		int i = 0;
		for (Object object : shipTypeList) {
			ShipType shipType = ShipType.valueOf(object.toString());
			this.shipTypes[i] = shipType;
			i++;
		}
	}

	@Override
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();
		values.put("id", id);
		values.put("width", width);
		values.put("height", height);
		values.put("world", origin.getWorld().getName());
		values.put("location", origin.toVector());

		ArrayList<String> shipTypeList = new ArrayList<String>();
		for (ShipType shipType : shipTypes) {
			shipTypeList.add(shipType.name());
		}
		
		values.put("ship-types", shipTypeList);
		
		return values;
	}
	
	

	
	public String getId() {
		return id;
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
