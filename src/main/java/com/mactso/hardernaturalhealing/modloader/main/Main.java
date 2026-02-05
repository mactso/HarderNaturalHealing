package com.mactso.hardernaturalhealing.modloader.main;

import com.mactso.hardernaturalhealing.modloader.config.MyConfig;
import com.mactso.hardernaturalhealing.modloader.events.MyCommandsRegisterEvent;
import com.mactso.hardernaturalhealing.modloader.events.PlayerDeathEventHandler;
import com.mactso.hardernaturalhealing.modloader.events.PlayerTickHandler;
import com.mactso.hardernaturalhealing.modloader.events.PlayerWakeupEventHandler;
import com.mactso.hardernaturalhealing.modloader.events.ServerEvents;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Main entry point for the HarderNaturalHealing mod. Handles mod setup, config
 * registration, and event subscriptions.
 */

@Mod("hardernaturalhealing")
public class Main {

	public static final String MODID = "hardernaturalhealing";
	public static final String MOD_Version = "v1.16 Neo 1.21.1";
	
	public Main(IEventBus modEventBus, ModContainer modContainer) {

		NeoForge.EVENT_BUS.register(new MyCommandsRegisterEvent());
		NeoForge.EVENT_BUS.register(new PlayerDeathEventHandler());
		NeoForge.EVENT_BUS.register(new PlayerTickHandler());
		NeoForge.EVENT_BUS.register(new PlayerWakeupEventHandler());
		NeoForge.EVENT_BUS.register(new ServerEvents());

		modContainer.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
	}

        
}
