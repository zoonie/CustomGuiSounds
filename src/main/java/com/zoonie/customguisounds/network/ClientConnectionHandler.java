package com.zoonie.customguisounds.network;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

import com.zoonie.customguisounds.sound.SoundPlayer;

public class ClientConnectionHandler
{
	@SubscribeEvent
	public void onDisconnect(ClientDisconnectionFromServerEvent event)
	{
		SoundPlayer.getSoundPlayer().pauseLastSound();
	}
}
