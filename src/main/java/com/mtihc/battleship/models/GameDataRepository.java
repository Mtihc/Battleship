package com.mtihc.battleship.models;

import java.io.IOException;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;

public interface GameDataRepository {
	GameData getGame(String id) throws IOException, InvalidConfigurationException;
	void setGame(String id, GameData game) throws IOException;
	void deleteGame(String id);
	boolean hasGame(String id);
	Set<String> getGameIds();
}
