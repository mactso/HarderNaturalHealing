package com.mactso.hardernaturalhealing.events;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerWakeupEventHandler {

	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {

		if (event.getPlayer().world instanceof ServerWorld) {
			event.getPlayer().heal((float) MyConfig.getWakeupHealingAmount());
		}
	}
}
