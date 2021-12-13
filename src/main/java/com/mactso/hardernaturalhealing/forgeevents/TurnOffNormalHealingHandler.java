package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class TurnOffNormalHealingHandler {
	@SubscribeEvent
	public static void turnOffNormalHealingHandler(ServerStartingEvent event) {
		
		if (MyConfig.getDebugLevel() > 0) {
			System.out.println("HarderNormalHealing: Turn off natural regeneration");

		}
		((GameRules.BooleanValue) event.getServer().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false,
				event.getServer());
	}
}
