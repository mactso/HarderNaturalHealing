package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerWakeupEventHandler {

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
		// TODO: confirm wake immediately is the new updateworld()
		// TODO: this confuses eclipse so it thinks there is a memory leak.
        if (!event.getEntity().level().isClientSide && !event.wakeImmediately()) {
			event.getEntity().heal((float) MyConfig.getWakeupHealingAmount());
        }
	}
}
