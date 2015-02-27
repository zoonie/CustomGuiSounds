package com.zoonie.customguimusic.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zoonie.customguimusic.CustomGuiMusic;
import com.zoonie.customguimusic.sound.Sound;
import com.zoonie.customguimusic.sound.SoundFileHandler;

public class MappingsConfigManager
{
	private static File config;
	public static TreeMap<String, Sound> mappings = new TreeMap<String, Sound>();
	private static TreeMap<String, Sound> soundlessMappings = new TreeMap<String, Sound>();

	public MappingsConfigManager(File config)
	{
		this.config = config;
		read();
	}

	public static void read()
	{
		Gson gson = new GsonBuilder().create();
		try
		{
			if(!config.exists())
			{
				config.createNewFile();
			}
			BufferedReader br = new BufferedReader(new FileReader(config));
			Type type = new TypeToken<TreeMap<String, Sound>>() {
			}.getType();
			TreeMap<String, Sound> mappingsFromFile = gson.fromJson(br, type);

			mappings = new TreeMap<String, Sound>();
			if(mappingsFromFile != null)
			{
				for(Entry<String, Sound> entry : mappingsFromFile.entrySet())
				{
					Sound sound = entry.getValue();
					if(SoundFileHandler.soundLoaded(sound))
					{
						mappings.put(entry.getKey(), sound);
					}
					else
					{
						CustomGuiMusic.logger.error("Could not find sound: " + sound.getName() + " within the sounds folder of the Minecraft installation directory.");
						soundlessMappings.put(entry.getKey(), sound);
					}
				}
			}

			br.close();
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	public static void write()
	{
		try
		{
			if(!config.exists())
			{
				config.getParentFile().mkdirs();
				config.createNewFile();
			}

			TreeMap<String, Sound> mappingsToWrite = new TreeMap<String, Sound>();
			mappingsToWrite.putAll(soundlessMappings);
			mappingsToWrite.putAll(mappings);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(mappingsToWrite);

			FileWriter writer = new FileWriter(config.getAbsoluteFile());
			writer.write(json);
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
