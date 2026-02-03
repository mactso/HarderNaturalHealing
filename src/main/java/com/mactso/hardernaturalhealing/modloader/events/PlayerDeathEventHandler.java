package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone;


public class PlayerDeathEventHandler {

	@SubscribeEvent
	public void onPlayerDeath(Clone event) {
		Player p = event.getEntity();
		int healthAfterDeath = MyConfig.getHealthAfterDeath();
		if (event.isWasDeath()) {
			if (healthAfterDeath < 20) {
				p.setHealth(healthAfterDeath);
			}
			int hungerAfterDeath = MyConfig.getHungerAfterDeath();
			if (hungerAfterDeath < 20) {
				p.getFoodData().setFoodLevel((hungerAfterDeath));
				p.getFoodData().setSaturation(0);
				p.getFoodData().setExhaustion(3.9f);

			}
		}
	}
}
