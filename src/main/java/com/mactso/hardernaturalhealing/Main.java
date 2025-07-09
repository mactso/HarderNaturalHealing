package com.mactso.hardernaturalhealing;

import com.mactso.hardernaturalhealing.commands.HarderNaturalHealingCommands;
import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.utility.Utility;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;



@Mod("hardernaturalhealing")
public class Main {

	public static final String MODID = "hardernaturalhealing";

    public Main(FMLJavaModLoadingContext context)
    {
		context.getModEventBus().register(this);
		context.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
		Utility.debugMsg (0, MODID + ": Registering Mod");
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
			System.out.println("hardernaturalhealing: Turn natural regeneration off.");
			((GameRules.BooleanValue) event.getServer().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false,
					event.getServer());
		}

		@SubscribeEvent
		public static void preInit(final ServerStoppingEvent event) {
			System.out.println("hardernaturalhealing: Turn natural regeneration rule on.");
			((GameRules.BooleanValue) event.getServer().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(true,
					event.getServer());
		}

		@SubscribeEvent 		
		public static void onCommandsRegistry(final RegisterCommandsEvent event) {
			System.out.println("Happy Trails: Registering Command Dispatcher");
			HarderNaturalHealingCommands.register(event.getDispatcher());			
		}
	}
	

}
