package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.logic.ReflectionAdapters;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

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

		int healthAfterDeath = MyConfig.getHealthAfterDeath();
		if (healthAfterDeath < 20) {
			p.setHealth(healthAfterDeath);
		}

		int hungerAfterDeath = MyConfig.getHungerAfterDeath();
		if (hungerAfterDeath < 20) {
			p.getFoodData().setFoodLevel(hungerAfterDeath);
			p.getFoodData().setSaturation(0.0F);
			ReflectionAdapters.setExhaustionLevel(p.getFoodData(), 3.9F);
		}
	}
}