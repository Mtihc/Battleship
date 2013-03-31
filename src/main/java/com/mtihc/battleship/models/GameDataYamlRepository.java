package com.mtihc.battleship.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class GameDataYamlRepository implements GameDataRepository {

	private static final String KEY = "game";
	
	private File directory;

	public GameDataYamlRepository(File directory) {
		this.directory = directory;
	}
	
	public GameDataYamlRepository(String directory) {
		this.directory = new File(directory);
	}
	
	public File getDirectory() {
		return directory;
	}
	
	@Override
	public GameData getGame(String id) throws IOException, InvalidConfigurationException {
		File file = new File(directory, id + ".yml");
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch(FileNotFoundException e) {
			return null;
		}
		return (GameData) config.get(KEY);
	}

	@Override
	public void setGame(String id, GameData game) throws IOException {
		if(game == null) {
			deleteGame(id);
			return;
		}
		File file = new File(directory, id + ".yml");
		YamlConfiguration config = new YamlConfiguration();
		config.set(KEY, game);
		config.save(file);
		
	}

	@Override
	public void deleteGame(String id) {
		File file = new File(directory, id + ".yml");
		file.delete();
	}

	@Override
	public boolean hasGame(String id) {
		File file = new File(directory, id + ".yml");
		return file.exists();
	}

	@Override
	public Set<String> getGameIds() {
		final Set<String> result = new HashSet<String>();
		
		directory.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File file, String name) {
				int n = name.length();
				if(name.endsWith(".yml") && n > 4) {
					result.add( name.substring(0, n - 4) );
				}
				return false;
			}
		});
		
		return result;
	}

}
