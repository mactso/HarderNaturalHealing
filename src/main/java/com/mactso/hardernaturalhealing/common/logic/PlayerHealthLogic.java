package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodData;

/**
 * Applies periodic custom healing, exhaustion, and starvation logic for a
 * single player.
 *
 * <p>
 * This method is called every tick but internally limits execution to once per
 * second using player ticks to spread the load.
 * </p>
 *
 * <p>
 * The method enforces:
 * </p>
 * <ul>
 * <li>Extra exhaustion while injured</li>
 * <li>Difficulty-based starvation damage</li>
 * <li>Configurable Healing delays after combat</li>
 * </ul>
 *
 * @param player the server-side player to process
 */
public class PlayerHealthLogic {

	private static final int TICKS_PER_SECOND = 20;
	private static final int STARVATION_INTERVAL = 4 * TICKS_PER_SECOND;

	/*
	 * Heal PLayer once per second If they are hurt, Not "dead or dying", add
	 * optional extra exhaustion apply HNH starvation only after vanilla starvation
	 * stops heal if allowed (food + combat delay)
	 * 
	 */

	public static void processPlayerHealth(ServerPlayer sp) {

		if (sp.isDeadOrDying())
			return;

		if (sp.tickCount % TICKS_PER_SECOND != 0)
			return;

		applyCustomStarvationDamage(sp);

		if (!isHurt(sp)) {
			return;
		}

		MyUtilities.debugMsg(1, "Handling wounded Player " + sp.getName().getString());

		// we know the player is hurt, so apply configured extra exhaustion.
		applyExtraExhaustionWhenHurt(sp);

		if (!(PlayerHungerLogic.hasEnoughFoodToHeal(sp)))
			return;

		// Healing may be delayed after the player attacks a monster.
		if (!canHealAgainAfterCombat(sp))
			return;

		// finally, heal the player the configured amount.
		healPlayer(sp);

	}

	private static void applyCustomStarvationDamage(ServerPlayer sp) {

		// Only check starvation damage at the configured interval
		if (sp.tickCount % STARVATION_INTERVAL != 0) {
			return;
		}

		if (!PlayerHungerLogic.isPlayerStarving(sp)) {
			return;
		}

		if (!isDoCustomStarvationDamage(sp)) {
			return;
		}

		if (sp.getHealth() <= MyConfig.getMinimumStarvationHealth()) {
			return;
		}

		ServerLevel serverLevel = (ServerLevel) sp.level();
		sp.hurtServer(serverLevel, serverLevel.damageSources().starve(), 1.0F);

	}

	private static void applyExtraExhaustionWhenHurt(ServerPlayer sp) {
		float extraExhaustion = (float) MyConfig.getExtraExhaustionWhenHurt();
		if (extraExhaustion > 0)
			sp.getFoodData().addExhaustion((float) extraExhaustion);
	}

	// Vanilla stops starvation death for NORMAL and EASY at 0.5 and 5.0 hearts.
	// Vanilla stops starvation for PEACEFUL.
	// Do HNH custom starvation when Vanilla stops starvation.
	private static boolean isDoCustomStarvationDamage(ServerPlayer sp) {

		Difficulty dif = sp.level().getDifficulty();

		if (dif == Difficulty.HARD) {
			return false;
		}

		if ((dif == Difficulty.NORMAL) && (sp.getHealth() <= 1.0F)) {
			return true;
		}

		if ((dif == Difficulty.EASY) && (sp.getHealth() <= 10.0F)) {
			return true;
		}

		// PEACEFUL or any other modded difficulty
		return true;
	}

	private static boolean isHurt(ServerPlayer sp) {

		if (sp.getHealth() < sp.getMaxHealth())
			return true;

		return false;
	}

	// Block Healing if Delayed healing after Attacks is configured.
	private static boolean canHealAgainAfterCombat(ServerPlayer player) {

		if (player.tickCount < player.getLastHurtByMobTimestamp() + MyConfig.getAttackHealingDelayTicks())
			return false;

		return true;
	}

	private static void healPlayer(ServerPlayer player) {
		float healingPerSecond = (float) MyConfig.getHealingPerSecond();
		if (healingPerSecond > 0.0) {
			FoodData foodData = player.getFoodData();
			player.heal(healingPerSecond);
			float healingExhaustionCost = (float) MyConfig.getHealingExhaustionCost();
			foodData.addExhaustion(healingExhaustionCost);

		}
	}
}
