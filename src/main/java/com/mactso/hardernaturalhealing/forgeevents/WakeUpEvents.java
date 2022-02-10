package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class WakeUpEvents {
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {

		if (event.getPlayer().level instanceof ServerLevel sl) {
			long debug = sl.getDayTime()%24000;
			if (sl.getDayTime() % 24000 < 40) {
				event.getPlayer().heal((float) MyConfig.getWakeupHealingAmount());
			}
		}
	}
}
