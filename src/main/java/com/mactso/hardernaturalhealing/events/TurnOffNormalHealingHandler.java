package com.mactso.hardernaturalhealing.events;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.GameRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class TurnOffNormalHealingHandler {
	@SubscribeEvent
	public static void turnOffNormalHealingHandler(FMLServerStartingEvent event) {
		
		if (MyConfig.getDebugLevel() > 0) {
			System.out.println("HarderNormalHealing: Turn off natural regeneration");

		}
		((GameRules.BooleanValue) event.getServer().getGameRules().get(GameRules.NATURAL_REGENERATION)).set(false,
				event.getServer());
	}
}
