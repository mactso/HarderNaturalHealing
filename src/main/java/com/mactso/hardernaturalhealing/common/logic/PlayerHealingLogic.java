package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodData;

/**
 * Applies periodic custom healing, exhaustion, and starvation logic for a single player.
 *
 * <p>This method is called every tick but internally limits
 * execution to once per second using player ticks to spread the load.</p>
 *
 * <p>The method enforces:</p>
 * <ul>
 *   <li>Extra exhaustion while injured</li>
 *   <li>Difficulty-based starvation damage</li>
 *   <li>Healing delays after combat</li>
 * </ul>
 *
 * @param player the server-side player to process
 */
public class PlayerHealingLogic {

	private static final int TICKS_PER_SECOND = 20;
	private static final int STARVATION_INTERVAL = 4 * TICKS_PER_SECOND;

	/* 
	 * Heal PLayer once per second
	 * 	If they are hurt,
	 *  Not "dead or dying",
	 *  add optional extra exhaustion
	 *	apply HNH starvation only after vanilla starvation stops
	 *  heal if allowed (food + combat delay)
	 * 
	 */
	
	public static void doPlayerHealing(ServerPlayer serverPlayer) {

		long gameTime = serverPlayer.level().getGameTime();

		applyCustomStarvationDamage(serverPlayer, gameTime);// this may go into hunger soon.
		
		// Exit if player is dying, not hurt, or it isn't time to check yet.
		if (serverPlayer.isDeadOrDying() || serverPlayer.tickCount % TICKS_PER_SECOND != 0 || !isHurt(serverPlayer)) {
			return;
		}

		MyUtilities.debugMsg(1, "Handling wounded Player " + serverPlayer.getName().getString());

		// we know the player is hurt, so apply configured extra exhaustion.
		applyExtraExhaustionWhenHurt(serverPlayer);
		
		// we know the player is hurt, so apply configured extra exhaustion.
		if (!canHealAgainAfterCombat(serverPlayer))
			return;
		
		// finally, heal the player the configured amount.
		healPlayer(serverPlayer);
		
	}

	private static void applyExtraExhaustionWhenHurt(ServerPlayer player) {
		double extraExhaustion = MyConfig.getExtraExhaustionWhenHurt();
		if (extraExhaustion > 0)
			player.getFoodData().addExhaustion((float) extraExhaustion);
	}


	
	private static void applyCustomStarvationDamage(ServerPlayer serverPlayer, long gameTime) {

		if (isPlayerStarving(serverPlayer)) {
			
			if ((isDoCustomStarvationDamage(serverPlayer))) {
				if (gameTime % STARVATION_INTERVAL == 0 && serverPlayer.getHealth() > MyConfig.getMinimumStarvationHealth()) {
					ServerLevel serverLevel = serverPlayer.level();
					serverPlayer.hurtServer(serverLevel, serverLevel.damageSources().starve(), 1.0F);
				}
			}
			
		}

	}

	// Vanilla stops starvation death for NORMAL and EASY at 0.5 and 5.0 hearts.
	// Vanilla stops starvation for PEACEFUL.
	// Do HNH custom starvation when Vanilla stops starvation.
	private static boolean isDoCustomStarvationDamage(ServerPlayer player) {

		Difficulty dif = player.level().getDifficulty();

		if (dif == Difficulty.HARD) {
			return false;
		}

		if ((dif == Difficulty.NORMAL) && (player.getHealth() <= 1.0F)){
			return true;
		}

		if ((dif == Difficulty.EASY) && (player.getHealth() <= 10.0F)){
			return true;
		}

		// PEACEFUL or any other future difficulty
		return true;
	}
	
	private static boolean isPlayerStarving(ServerPlayer player) {
		
		if (player.getFoodData().getFoodLevel() == 0)
			return true; 
		
		return false;
	}

	private static boolean isHurt (ServerPlayer player) {

		if (player.getHealth() < player.getMaxHealth())
			return true;
		
		return false;
	}
	


	// Block Healing if Delayed healing after Attacks is configured.	
	private static boolean canHealAgainAfterCombat(ServerPlayer player) {

		if (player.tickCount < player.getLastHurtByMobTimestamp() + MyConfig.getAttackHealingDelayTicks())
			return false;
		
		FoodData fd = player.getFoodData();
		if (fd.getFoodLevel() < MyConfig.getMinimumFoodHealingLevel()) {
			return false;
		}
		
		return true;
	}

	private static void healPlayer(ServerPlayer player) {
		float healingPerSecond = (float) MyConfig.getHealingPerSecond();
		if (healingPerSecond > 0.0) {
			FoodData foodData = player.getFoodData();			
			player.heal(healingPerSecond);
		    float healingExhaustionCost  = (float) MyConfig.getHealingExhaustionCost();
			foodData.addExhaustion(healingExhaustionCost);

		}
	}
}
