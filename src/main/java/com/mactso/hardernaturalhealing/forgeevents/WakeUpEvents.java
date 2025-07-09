package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class WakeUpEvents {
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {

		if (event.getEntity().level() instanceof ServerLevel sl) {
			if (sl.getDayTime() % 24000 < 40) {
				event.getEntity().heal((float) MyConfig.getWakeupHealingAmount());
			}
		}
	}
}
