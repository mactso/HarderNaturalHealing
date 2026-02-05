package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;

import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

public class ServerEvents {

		@SubscribeEvent
	public void serverStarting(final ServerStartingEvent event) {
					MyUtilities.debugMsg(0,"hardernaturalhealing: Turn natural regeneration rule off.");

			((GameRules.BooleanValue) event.getServer().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false,
					event.getServer());
		}

		@SubscribeEvent
		public void preInit(final ServerStoppingEvent event) {
					MyUtilities.debugMsg(0,"hardernaturalhealing: Turn natural regeneration rule on.");

			((GameRules.BooleanValue) event.getServer().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)).set(true,
					event.getServer());
		}

}
