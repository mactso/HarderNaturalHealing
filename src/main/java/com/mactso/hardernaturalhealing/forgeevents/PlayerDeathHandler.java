package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerDeathHandler {

	@SubscribeEvent
	public static void onPlayerDeath(Clone event) {
		Player p = event.getEntity();
		int v = MyConfig.getHealthAfterDeath();
		if (event.isWasDeath()) {
			if (MyConfig.getHealthAfterDeath() < 20) {
				p.setHealth(MyConfig.getHealthAfterDeath());
			}
			if (MyConfig.getHungerAfterDeath() < 20) {
				p.getFoodData().setFoodLevel((MyConfig.getHungerAfterDeath()));
				p.getFoodData().setSaturation(0);
				p.getFoodData().setExhaustion(3.9f);

			}
		}
	}
}
