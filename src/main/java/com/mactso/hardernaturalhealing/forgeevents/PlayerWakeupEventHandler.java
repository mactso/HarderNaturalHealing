package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerWakeupEventHandler {

//	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {

		Player e = event.getEntity();
		
		if (e.level().isClientSide()) 
			return;
		
		if (!e.level().isDay())
			return;

		event.getEntity().heal((float) MyConfig.getWakeupHealingAmount());

	}
}
