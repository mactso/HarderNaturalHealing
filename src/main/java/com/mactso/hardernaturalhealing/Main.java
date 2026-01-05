package com.mactso.hardernaturalhealing;

import com.mactso.hardernaturalhealing.commands.MyCommands;
import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.utility.MyUtilities;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;



@Mod("hardernaturalhealing")
public class Main {

	public static final String MODID = "hardernaturalhealing";

    public Main(FMLJavaModLoadingContext context)
    {
		context.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
        FMLCommonSetupEvent.getBus(context.getModBusGroup()).addListener(this::handleCommonSetup);
    	MyUtilities.debugMsg(0,MODID + ": Registering Mod.");
	}

    // Register ourselves for server and other game events we are interested in
	@SubscribeEvent 
	public void handleCommonSetup (final FMLCommonSetupEvent event) {
		// nothing happens in here any more.
	}       
	
	@SubscribeEvent
	public static void preInit(final FMLCommonSetupEvent event) {
		System.out.println("hardernaturalhealing: Registering Handler");
// remove these redundant registrations (since using @Mod.EventBusSubscriber() now);
//		MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());
//		MinecraftForge.EVENT_BUS.register(new PlayerWakeupEventHandler());

	}

	@Mod.EventBusSubscriber()
	public static class ForgeEvents {
		@SubscribeEvent
		public static void preInit(final ServerStartingEvent event) {
			MyUtilities.debugMsg(0,"hardernaturalhealing: Turn natural regeneration rule off.");
			
			MinecraftServer server = event.getServer();
		    for (ServerLevel level : server.getAllLevels()) {
		    	level.getGameRules().set(GameRules.NATURAL_HEALTH_REGENERATION, false, server);
		    }

		}

		@SubscribeEvent
		public static void preInit(final ServerStoppingEvent event) {
			MyUtilities.debugMsg(0,"hardernaturalhealing: Turn natural regeneration rule on.");
			MinecraftServer server = event.getServer();
		    for (ServerLevel level : server.getAllLevels()) {
		    	level.getGameRules().set(GameRules.NATURAL_HEALTH_REGENERATION, true, server);
		    }
		}

		@SubscribeEvent 		
		public static void onCommandsRegistry(final RegisterCommandsEvent event) {
			MyUtilities.debugMsg(1,"HarderNaturalHealing: Registering Command Dispatcher");
			MyCommands.register(event.getDispatcher());			
		}
	}
	

}
