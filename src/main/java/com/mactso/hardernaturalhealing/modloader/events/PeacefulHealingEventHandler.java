package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.logic.PeacefulHealingLogic;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/* 
 * Thin PlayerTickEvent Handler
 */
public class PeacefulHealingEventHandler {

	@SubscribeEvent
	public void onPlayerTickPre(PlayerTickEvent.Pre event) {

		if (MyConfig.isPeacefulHunger()) {
			Player player = event.getEntity();
			PeacefulHealingLogic.peacefulHealingPreLogic(player);
		}
		
		// other possible playertickevent.pre handling.
	}

	@SubscribeEvent
	public void onPlayerTickPost(PlayerTickEvent.Post event) {

		if (MyConfig.isPeacefulHunger()) {
			Player player = event.getEntity();
			PeacefulHealingLogic.peacefulHealingPostLogic(player);
		}

		// other possible playertickevent.post handling.
		
	}

}
