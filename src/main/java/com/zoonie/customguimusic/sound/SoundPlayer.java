package com.zoonie.customguimusic.sound;

import java.io.File;
import java.net.MalformedURLException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.SoundSystem;

@SideOnly(Side.CLIENT)
public class SoundPlayer
{
	private static SoundPlayer instance = new SoundPlayer();

	private static SoundSystem soundSystem;
	private static String lastSourcename;

	private static void init()
	{
		if(soundSystem == null || soundSystem.randomNumberGenerator == null)
		{
			SoundManager soundManager = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.client.audio.SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), "sndManager",
					"field_147694_f", "V");
			soundSystem = ObfuscationReflectionHelper.getPrivateValue(SoundManager.class, soundManager, "sndSystem", "field_148620_e", "e");
		}
	}

	public String playNewSound(File sound, BlockPos pos, boolean loop, boolean fade, float volume)
	{
		try
		{
			String sourcename = sound.getName();

			if(sound.length() > 500000)
				soundSystem.newStreamingSource(false, sourcename, sound.toURI().toURL(), sound.getName(), loop, pos.getX(), pos.getY(), pos.getZ(), fade ? 2 : 0, 16);
			else
				soundSystem.newSource(false, sourcename, sound.toURI().toURL(), sound.getName(), loop, pos.getX(), pos.getY(), pos.getZ(), fade ? 2 : 0, 16);
			soundSystem.setVolume(sourcename, volume);
			soundSystem.play(sourcename);

			lastSourcename = sourcename;

			return sourcename;
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void playSound(String sourcename, BlockPos pos)
	{
		soundSystem.setPosition(sourcename, pos.getX(), pos.getY(), pos.getZ());
		soundSystem.play(sourcename);
	}

	public void pauseLastSound()
	{
		soundSystem.pause(lastSourcename);
	}

	public void stopSound(String sourcename)
	{
		soundSystem.stop(sourcename);
		if(soundSystem.playing(sourcename))
			soundSystem.removeSource(sourcename);
	}

	public void pauseSound(String sourcename)
	{
		soundSystem.pause(sourcename);
	}

	public boolean isPlaying(String sourcename)
	{
		return soundSystem.playing(sourcename);
	}

	public void adjustVolume(String sourcename, float volume)
	{
		soundSystem.setVolume(sourcename, volume);
	}

	public void setLooping(String sourcename, boolean looping)
	{
		soundSystem.setLooping(sourcename, looping);
	}

	public void rewind(String sourcename)
	{
		soundSystem.rewind(sourcename);
	}

	public static SoundPlayer getSoundPlayer()
	{
		init();
		return instance;
	}
}
