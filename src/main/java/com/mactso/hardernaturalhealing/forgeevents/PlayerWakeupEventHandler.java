package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerWakeupEventHandler {

	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
		// TODO: confirm wake immediately is the new updateworld()
        if (!event.getEntity().level.isClientSide && !event.wakeImmediately()) {
			event.getEntity().heal((float) MyConfig.getWakeupHealingAmount());
        }
	}
}
