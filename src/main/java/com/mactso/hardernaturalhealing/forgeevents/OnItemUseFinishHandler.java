package com.mactso.hardernaturalhealing.forgeevents;

import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class OnItemUseFinishHandler {

		@SubscribeEvent
		public static void onPlayerWakeUp(LivingEntityUseItemEvent.Finish event) {

//			System.out.println("item use finish");
		}
	}
