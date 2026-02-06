package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.logic.PlayerDeathLogic;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone;

public class PlayerDeathEventHandler {


	@SubscribeEvent
	public void onPlayerDeath(Clone event) {
		if (!event.isWasDeath()) {
			return;
		}

		Player p = event.getEntity();
	    PlayerDeathLogic.setHealthAndHunger(p);

	}
}