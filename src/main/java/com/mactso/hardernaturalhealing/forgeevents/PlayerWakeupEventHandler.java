package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerWakeupEventHandler {

	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (!event.getEntityLiving().level.isClientSide && !event.wakeImmediately() && !event.updateWorld()) {
			event.getPlayer().heal((float) MyConfig.getWakeupHealingAmount());
        }
	}
}
