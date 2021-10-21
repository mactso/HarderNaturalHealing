package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;


public class TurnOffNormalHealingHandler {
	@SubscribeEvent
	public static void turnOffNormalHealingHandler(FMLServerStartingEvent event) {
		
		if (MyConfig.getDebugLevel() > 0) {
			System.out.println("HarderNormalHealing: Turn off natural regeneration");

		}
		((GameRules.BooleanValue) event.getServer().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false,
				event.getServer());
	}
}
