package com.zoonie.customguimusic;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zoonie.customguimusic.proxy.CommonProxy;

@Mod(modid = CustomGuiMusic.MOD_ID, name = CustomGuiMusic.MOD_NAME, version = CustomGuiMusic.VERSION)
public class CustomGuiMusic
{
	public final static String MOD_ID = "CustomGuiMusic";
	public final static String MOD_NAME = "Custom Gui Music";
	public final static String VERSION = "1.0.0";

	public static final Logger logger = LogManager.getLogger(MOD_NAME);

	@Instance(MOD_ID)
	public static CustomGuiMusic instance;

	@SidedProxy(clientSide = "com.zoonie.customguimusic.proxy.ClientProxy", serverSide = "com.zoonie.customguimusic.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
}
