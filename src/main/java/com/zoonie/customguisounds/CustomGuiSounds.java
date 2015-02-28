package com.zoonie.customguisounds;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zoonie.customguisounds.proxy.CommonProxy;

@Mod(modid = CustomGuiSounds.MOD_ID, name = CustomGuiSounds.MOD_NAME, version = CustomGuiSounds.VERSION)
public class CustomGuiSounds
{
	public final static String MOD_ID = "CustomGuiSounds";
	public final static String MOD_NAME = "Custom Gui Sounds";
	public final static String VERSION = "1.0.0";

	public static final Logger logger = LogManager.getLogger(MOD_NAME);

	@Instance(MOD_ID)
	public static CustomGuiSounds instance;

	@SidedProxy(clientSide = "com.zoonie.customguisounds.proxy.ClientProxy", serverSide = "com.zoonie.customguisounds.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
}
