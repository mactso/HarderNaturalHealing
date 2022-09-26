package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.utility.Utility;

import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
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

			// heal once per second if the player is wounded.
			// additionally skip dead players. Some mods don't like receiving events for dead players.
			if (!p.isDeadOrDying() || (event.phase == TickEvent.Phase.END) || (gameTime % 20 != 0) || (p.getHealth() >= p.getMaxHealth())) {
				return;
			}
			

			Utility.debugMsg(1, "Handling Player " + p.getName().getString() + ".  Player is wounded.");

			
			// if player is hurt, then use optional extra exhaustion if non-zero.
			if (MyConfig.getExtraExhaustionWhenHurt() > 0.0) {
				p.getFoodData().addExhaustion((float) MyConfig.getExtraExhaustionWhenHurt());
			}
			
			// Minecraft starves hard mode players to death.
			Difficulty dif = p.level.getDifficulty();
			boolean handleStarving = false;
			if ((p.getFoodData().getFoodLevel() <= 0)) {
				handleStarving = true;
				if (dif == Difficulty.HARD) handleStarving = false;  // minecraft will handle starving;
				if ((dif == Difficulty.NORMAL) && (p.getHealth() > 1.0)) handleStarving = false; // minecraft will handle starving;
				if ((dif == Difficulty.EASY) && (p.getHealth() > 10.0)) handleStarving = false;
			}
			
			if (handleStarving) {
				// HarderNaturalHealing will starve the player every 4 seconds until at the configured minimum health level.
				if ((gameTime%80 == 0) && (p.getHealth() > MyConfig.getMinimumStarvationHealth())) {
	                p.hurt(DamageSource.STARVE, 1.0F);
		        }
			}

			// block healing for 0 to 3600 ticks after player attacks.
			int nextHealingTime = p.getLastHurtMobTimestamp() + MyConfig.getAttackHealingDelayTicks();
			int sessionGameTime = p.tickCount;
			if (sessionGameTime < nextHealingTime) {
				return;
			}

			// block healing if the player under configured food level.
			if (p.getFoodData().getFoodLevel() < MyConfig.getMinimumFoodHealingLevel()) {
				return;
			}

			// heal player at the start of the tick if they qualify for healing.
			if (MyConfig.getHealingPerSecond() > 0.0) {
				p.heal((float) MyConfig.getHealingPerSecond());
				p.getFoodData().addExhaustion((float) (MyConfig.getHealingExhaustionCost()));
			}

		}

	}
}
