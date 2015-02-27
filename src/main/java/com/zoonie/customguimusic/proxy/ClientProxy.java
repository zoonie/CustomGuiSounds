package com.zoonie.customguimusic.proxy;

import java.io.File;

import javax.swing.UIManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.zoonie.customguimusic.config.MappingsConfigManager;
import com.zoonie.customguimusic.input.InputHandler;
import com.zoonie.customguimusic.network.ClientConnectionHandler;
import com.zoonie.customguimusic.sound.SoundEventHandler;
import com.zoonie.customguimusic.sound.SoundFileHandler;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		soundSetup();
		UISetup();
		configSetup(event.getSuggestedConfigurationFile());
		InputHandler inputHandler = new InputHandler();

		FMLCommonHandler.instance().bus().register(inputHandler);
		FMLCommonHandler.instance().bus().register(new ClientConnectionHandler());

		MinecraftForge.EVENT_BUS.register(inputHandler);
		MinecraftForge.EVENT_BUS.register(new SoundEventHandler());
	}

	private void soundSetup()
	{
		SoundFileHandler.findSounds();
	}

	private void UISetup()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void configSetup(File config)
	{
		new MappingsConfigManager(config);
	}
}
