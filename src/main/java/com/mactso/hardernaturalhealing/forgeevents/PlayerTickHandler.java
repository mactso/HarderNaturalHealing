package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber()
public class PlayerTickHandler {

	@SubscribeEvent
	public static void playerTickHandler(TickEvent.PlayerTickEvent event) {

		if (event.player.getCommandSenderWorld() instanceof ServerLevel) {

			Player p = event.player;
			Level w = p.level;
			long gameTime = w.getGameTime();

			// only heal once per second.
			if (event.phase == TickEvent.Phase.END) {
				return;
			}
			if (gameTime % 20 != 0) {
				return;
			}

			// if player is hurt, then use optional extra exhaustion
			if (event.player.getHealth() < event.player.getMaxHealth()) {
				event.player.getFoodData().addExhaustion((float) MyConfig.getExtraExhaustionWhenHurt());
				MyConfig.debugMsg(1, "Adding " + MyConfig.getExtraExhaustionWhenHurt() + " Exhaustion");
			}

			// block healing for 0 to 3600 ticks after player attacks.
			int nextHealingTime = p.getLastHurtMobTimestamp() + MyConfig.getAttackHealingDelayTicks();
			int sessionGameTime = p.tickCount;
			if (sessionGameTime < nextHealingTime) {
				MyConfig.debugMsg(1, "Healing Delayed By Attack :" + (sessionGameTime - nextHealingTime));
				return;
			}

			// block healing if player is at maximum hit points.
			if (event.player.getHealth() >= event.player.getMaxHealth()) {

				return;
			}

			// block healing if the player under configured food level.
			if (event.player.getFoodData().getFoodLevel() < MyConfig.getMinimumFoodHealingLevel()) {
				MyConfig.debugMsg(1, "Player too hungry to heal.");
				return;
			}

			// heal player at the start of the tick if they qualify for healing.
			MyConfig.debugMsg(1, "HarderNormalHealing: Healing Player " + MyConfig.getHealingPerSecond() + " hitpoints.");
			event.player.heal((float) MyConfig.getHealingPerSecond());
			event.player.getFoodData().addExhaustion((float) (MyConfig.getHealingExhaustionCost()));

		}

	}
}
