package com.mactso.hardernaturalhealing.events;

import java.beans.EventSetDescriptor;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerTickHandler {

	@SubscribeEvent
	public static void playerTickHandler(TickEvent.PlayerTickEvent event) {


		if (event.player.getEntityWorld() instanceof ServerWorld) {

			PlayerEntity p 	= event.player;
			World w = p.world;
			long gameTime = w.getGameTime();
			
			

			// only heal once per second.
			if (gameTime % 20 != 0) {
				return;
			}

			// block healing for 0 to 3600 ticks after player attacks.
			int nextHealingTime = p.getLastAttackedEntityTime()+MyConfig.getAttackHealingDelayTicks();
			int sessionGameTime = p.ticksExisted;
			if ( sessionGameTime < nextHealingTime ) {
				if (MyConfig.getDebugLevel() > 0) {
					System.out.println(
							"Healing Delayed By Attack :" + (sessionGameTime - nextHealingTime));

				}
				return;
			}

			// block healing if player is at maximum hit points.
			if (event.player.getHealth() >= event.player.getMaxHealth()) {

				return;
			}
			
			// block healing if the player unde configured food level.
			if (event.player.getFoodStats().getFoodLevel() < MyConfig.getMinimumFoodHealingLevel()) {
				return;
			}
			
			// heal player at the start of the tick if they qualify for healing.
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
