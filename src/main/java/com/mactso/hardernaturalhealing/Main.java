package com.mactso.hardernaturalhealing;

import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.events.PlayerTickHandler;
import com.mactso.hardernaturalhealing.events.PlayerWakeupEventHandler;
import com.mactso.hardernaturalhealing.events.TurnOffNormalHealingHandler;

import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("hardernaturalhealing")
public class Main {

	public static final String MODID = "hardernaturalhealing";

	public Main() {
		System.out.println("hardernaturalhealing: Registering Mod.");
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
	}

	@SubscribeEvent
	public static void preInit(final FMLCommonSetupEvent event) {
		System.out.println("hardernaturalhealing: Registering Handler");
		MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerWakeupEventHandler());

	}

	@Mod.EventBusSubscriber()
	public static class ForgeEvents {
		@SubscribeEvent
		public static void preInit(final FMLServerStartingEvent event) {
			System.out.println("hardernaturalhealing: Turn normal healing Off");
			((GameRules.BooleanValue) event.getServer().getGameRules().get(GameRules.NATURAL_REGENERATION)).set(false,
					event.getServer());
		}

	}

}
