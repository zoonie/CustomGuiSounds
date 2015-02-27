package com.zoonie.customguimusic.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.google.common.io.Files;

public class SoundFileHandler
{
	private static File soundsFolder = new File("sounds");;
	private static ArrayList<Sound> sounds;

	public static void findSounds()
	{
		if(!soundsFolder.exists())
		{
			soundsFolder.mkdir();
		}

		sounds = new ArrayList<Sound>();

		addSoundsFromDirectory(soundsFolder);

		Collections.sort(sounds);
	}

	private static void addSoundsFromDirectory(File directory)
	{
		for(File file : directory.listFiles())
		{
			if(file.isFile())
			{
				if(file.getName().endsWith(".ogg") || file.getName().endsWith(".wav") || file.getName().endsWith(".mp3"))
				{
					Sound sound = new Sound(file);
					sounds.add(sound);
				}
			}
			else if(file.isDirectory())
			{
				addSoundsFromDirectory(file);
			}
		}
	}

	public static ArrayList<Sound> getSounds()
	{
		findSounds();
		return sounds;
	}

	public static boolean soundLoaded(Sound sound)
	{
		if(sounds.contains(sound))
			return true;
		return false;
	}

	private static void addSound(Sound sound)
	{
		sounds.add(sound);
		Collections.sort(sounds);
	}

	public static Sound copyFileToSoundsFolder(File file)
	{
		File newFile = new File(soundsFolder + File.separator + file.getName());
		try
		{
			Files.copy(file, newFile);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		Sound sound = new Sound(newFile);
		addSound(sound);
		return sound;
	}
}
