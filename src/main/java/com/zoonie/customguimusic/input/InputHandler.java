package com.zoonie.customguimusic.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.input.Keyboard;

import com.zoonie.customguimusic.config.MappingsConfigManager;
import com.zoonie.customguimusic.gui.GuiSetMusic;
import com.zoonie.customguimusic.sound.Sound;
import com.zoonie.customguimusic.sound.SoundPlayer;

public class InputHandler
{
	private String sourcename;
	private GuiScreen lastGui;
	private Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onGuiOpenEvent(GuiOpenEvent event)
	{
		if(!GuiSetMusic.open)
		{
			SoundPlayer.getSoundPlayer().pauseSound(sourcename);
		}

		if(event.gui != null)
		{
			Sound sound = MappingsConfigManager.mappings.get(event.gui.getClass().getSimpleName());
			if(sound != null)
			{
				MovingObjectPosition mop = mc.objectMouseOver;
				BlockPos pos = mc.theWorld != null ? mop.getBlockPos() : new BlockPos(0, 0, 0);

				sourcename = sound.getName();
				if(!sound.resumeOnGuiOpen())
					SoundPlayer.getSoundPlayer().rewind(sourcename);

				SoundPlayer.getSoundPlayer().playSound(sourcename, pos);

				if(!SoundPlayer.getSoundPlayer().isPlaying(sourcename))
				{
					sourcename = SoundPlayer.getSoundPlayer().playNewSound(sound.getLocation(), pos, sound.looping(), mc.theWorld != null, sound.getVolume());
				}
			}

		}
		else if(lastGui != null)
		{
			Sound sound = MappingsConfigManager.mappings.get(lastGui.getClass().getSimpleName() + "Close");
			if(sound != null && !GuiSetMusic.open)
			{
				MovingObjectPosition mop = mc.objectMouseOver;
				BlockPos pos = mc.theWorld != null ? mop.getBlockPos() : new BlockPos(0, 0, 0);

				String name = sound.getName();

				if(SoundPlayer.getSoundPlayer().isPlaying(name))
				{
					SoundPlayer.getSoundPlayer().rewind(name);
					SoundPlayer.getSoundPlayer().playSound(name, pos);
				}
				else
					SoundPlayer.getSoundPlayer().playNewSound(sound.getLocation(), pos, false, mc.theWorld != null, sound.getVolume());
			}
		}

		lastGui = event.gui;
	}

	@SubscribeEvent
	public void onGuiScreenEvent(GuiScreenEvent event)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && !GuiSetMusic.open && event.gui != null)
		{
			mc.displayGuiScreen(new GuiSetMusic(mc.currentScreen));
		}
	}
}
