package com.mactso.hardernaturalhealing.events;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerTickHandler {

	@SubscribeEvent
	public static void playerTickHandler(TickEvent.PlayerTickEvent event) {
		if (event.player.getEntityWorld() instanceof ServerWorld) {
			if (event.player.getEntityWorld().getGameTime() % 20 != 0) {
				return;
			}
			if (event.player.getHealth() >= event.player.getMaxHealth()) {
				return;
			}
			if (event.player.getFoodStats().getFoodLevel() < MyConfig.getMinimumFoodHealingLevel()) {
				return;
			}
			if (event.phase == TickEvent.Phase.START) {
				if (MyConfig.getDebugLevel() > 0) {
					System.out.println(
							"HarderNormalHealing: Healing Player " + MyConfig.getHealingPerSecond() + " hitpoints.");

				}
//				System.out.println("prehealing health:"+event.player.getHealth() + " " +MyConfig.getHealingPerSecond() + " hitpoints.");
				event.player.heal((float) MyConfig.getHealingPerSecond());
				event.player.getFoodStats().addExhaustion((float) (MyConfig.getHealingExhaustionCost()));
//				System.out.println("psthealing health:"+event.player.getHealth() + " "+ MyConfig.getHealingPerSecond() + " hitpoints.");
			}
		}

	}
}
