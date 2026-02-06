package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

/**
 * PeacefulHungerLogic This overrides behavior of lines 40-44 of FoodData so
 * that Peaceful difficulty also consumes food. called from PlayerTickEvent
 * Handler.
 * Applies Mob Effect Hunger in cases that Minecraft Blocks it.
 */

public class PlayerHungerLogic {
	
	// Minecraft skips processing hunger from activities by difficulty level.
	// This restores hunger processing.

	public static void processPeacefulHunger(Player player) {

		if (!(player instanceof ServerPlayer sp))
			return;

		if (sp.level().getDifficulty() != Difficulty.PEACEFUL)
			return;
		
		ServerLevel serverLevel = (ServerLevel) player.level();

		// Exit early if the chunk is not loaded during debugging.
		BlockPos pos = player.blockPosition();
		if (!serverLevel.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) {
		    return;
		}

		if ((MyConfig.isDebug() && (sp.tickCount%3 == 0))) {
			FoodData fdebug = sp.getFoodData();
			float exhaustionLevel = ReflectionAdapters.getExhaustionLevel(fdebug);
			MyUtilities.debugMsg(2, "FoodLevel: " + fdebug.getFoodLevel() + " Exhaustion Level: " + exhaustionLevel);
		}

		
		if (!(MyConfig.isPeacefulHunger()))
			return;

		FoodData fs = sp.getFoodData();
		float saturationLevel = fs.getSaturationLevel();
		float exhaustionLevel = ReflectionAdapters.getExhaustionLevel(fs);
		int foodLevel = fs.getFoodLevel();

		// custom code runs before FoodData Class tick() in 1.21.X
		if (MyConfig.isDebug())
			MyUtilities.debugMsg(1,"afterHunger"+exhaustionLevel);
		if (exhaustionLevel > 4.0) {
			exhaustionLevel -= 4.0;
			ReflectionAdapters.setExhaustionLevel(fs, exhaustionLevel);
			if (saturationLevel > 0.0F) {
				saturationLevel = Math.max(saturationLevel - 1.0F, 0.0F);
				fs.setSaturation(saturationLevel);
			} else {
				if (foodLevel > 0) {
					foodLevel = foodLevel-1;
					fs.setFoodLevel(foodLevel);
				}
			}
		}

	}

	// This adds HUNGER effect exhaustion that Minecraft prevents in difficulty PEACEFUL.
	
	public static void processHungerPotionEffect(ServerPlayer sp) {

		if (sp.level().getDifficulty() != Difficulty.PEACEFUL)
			return;

		if (!sp.hasEffect(MobEffects.HUNGER))
			return;
		
		if ((MyConfig.isDebug() && (sp.tickCount%3 == 0))) {
			FoodData fdebug = sp.getFoodData();
			float exhaustionLevel = ReflectionAdapters.getExhaustionLevel(fdebug);
			MyUtilities.debugMsg(2, "FoodLevel: " + fdebug.getFoodLevel() + " Exhaustion Level: " + exhaustionLevel);
		}

		FoodData fs = sp.getFoodData();
		int amplifier = sp.getEffect(MobEffects.HUNGER).getAmplifier();
		float exhaustion = 0.005F * (amplifier + 1);
		float current = ReflectionAdapters.getExhaustionLevel(fs);
		ReflectionAdapters.setExhaustionLevel(fs, Math.min(current + exhaustion, 40.0F));
		
	}

	public static boolean isPlayerStarving(ServerPlayer sp) {
	
		if (sp.getFoodData().getFoodLevel() < 1)
			return true;
	
		return false;
	}

	public static boolean hasEnoughFoodToHeal(ServerPlayer sp) {
	
		FoodData fd = sp.getFoodData();
		if (fd.getFoodLevel() >= MyConfig.getMinimumFoodHealingLevel())
			return true;
		return false;
	
	}

}
