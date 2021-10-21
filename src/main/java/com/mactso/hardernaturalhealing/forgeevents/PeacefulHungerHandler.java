//package com.mactso.hardernaturalhealing.events;
//
//import com.mactso.hardernaturalhealing.config.MyConfig;
//
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.util.FoodStats;
//import net.minecraft.world.server.ServerWorld;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.event.TickEvent.PlayerTickEvent;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//public class PeacefulHungerHandler {
//	static float satLev = 0.0f;
//	static float extLev = 0.0f;
//	static int   fodLev = 0;
//	
//	@SubscribeEvent
//	public static void onPlayerHealing(PlayerTickEvent event) {
//
//		
//		if (event.player.world.isRemote) {
//			return;
//		}
//		ServerPlayerEntity p = (ServerPlayerEntity) event.player;
//		if (event.phase == TickEvent.Phase.START) {
//			satLev = p.getFoodStats().getSaturationLevel();
//			fodLev = p.getFoodStats().getFoodLevel();
//			if (event.player.world.getGameTime()%20 == 0) {
//				System.out.println ("Sat: " + satLev + "fodLev " + fodLev)
//			}
//		}
//		if (event.phase == TickEvent.Phase.END) {
//			FoodStats fs = p.getFoodStats();
//			// this should be "isMoving"
//			if (p.getMovementSpeed()) {
//				fs.setFoodSaturationLevel((fs.getSaturationLevel()-0.01f));
//			}
//			float SatLev = 0.0f;
//			int foodLev = 0;
//			float exhLev = 0.0f
//			if (fs.getFoodLevel())
//		}
//	
//
//	}
//	
//	@SubscribeEvent
//	public static void onPlayerStarving(PlayerWakeUpEvent event) {
//
//		if (event.getPlayer().world instanceof ServerWorld) {
//			event.getPlayer().heal((float) MyConfig.getWakeupHealingAmount());
//		}
//	}
//
//	@SubscribeEvent
//	public static void onPlayerExhausted(PlayerWakeUpEvent event) {
//
//		if (event.getPlayer().world instanceof ServerWorld) {
//			event.getPlayer().heal((float) MyConfig.getWakeupHealingAmount());
//		}
//	}
//}
