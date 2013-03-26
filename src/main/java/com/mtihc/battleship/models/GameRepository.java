package com.mtihc.battleship.models;

import java.io.IOException;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;

public interface GameRepository<T extends Game> {
	T getGame(String id) throws IOException, InvalidConfigurationException;
	void setGame(String id, T game) throws IOException;
	void deleteGame(String id);
	boolean hasGame(String id);
	Set<String> getGameIds();
}
