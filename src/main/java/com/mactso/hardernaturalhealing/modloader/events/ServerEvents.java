package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

public class ServerEvents {
	
	@SubscribeEvent
	public void serverStarting(final ServerStartingEvent event) {
		MyUtilities.debugMsg(0,"hardernaturalhealing: Turn natural regeneration rule off.");
		
		MinecraftServer server = event.getServer();
	    for (ServerLevel level : server.getAllLevels()) {
	    	level.getGameRules().set(GameRules.NATURAL_HEALTH_REGENERATION, false, server);
	    }
	    
	}

	@SubscribeEvent
	public void serverStopping(final ServerStoppingEvent event) {
		MyUtilities.debugMsg(0,"hardernaturalhealing: Turn natural regeneration rule on.");
		MinecraftServer server = event.getServer();
	    for (ServerLevel level : server.getAllLevels()) {
	    	level.getGameRules().set(GameRules.NATURAL_HEALTH_REGENERATION, true, server);
	    }
	}



}
