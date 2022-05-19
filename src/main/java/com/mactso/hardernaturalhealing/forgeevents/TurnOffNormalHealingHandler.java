package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.utility.Utility;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class TurnOffNormalHealingHandler {
	@SubscribeEvent
	public static void turnOffNormalHealingHandler(ServerStartingEvent event) {
		
		Utility.debugMsg(1, "HarderNormalHealing: Turn off natural regeneration");
		((GameRules.BooleanValue) event.getServer().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false,
				event.getServer());
	}
}
