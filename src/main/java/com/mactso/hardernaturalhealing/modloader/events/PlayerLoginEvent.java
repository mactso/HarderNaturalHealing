package com.mactso.hardernaturalhealing.modloader.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerLoginEvent {
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
	    if (event.getEntity() instanceof ServerPlayer sp) {
			System.out.println("loginevent called");
	        sp.getServer()
	          .getGameRules()
	          .getRule(GameRules.RULE_NATURAL_REGENERATION)
	          .set(false, sp.getServer());
	    }
	}

}
