package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;

/**
 * PeacefulHungerLogic
 * This overrides behavior of lines 40-44 of FoodData so that Peaceful 
 * difficulty also consumes food.
 * called from PlayerTickEvent Handler.
 */

public class PeacefulHungerLogic {

	public static void doPeacefulHunger(Player player) {

		if (!(MyConfig.isPeacefulHunger()))
			return;

		Level level = player.level();

		if (level.isClientSide())
			return;

		if (player.level().getDifficulty() != Difficulty.PEACEFUL)
			return;

		ServerPlayer sp = (ServerPlayer) player;
		FoodData fs = sp.getFoodData();

		float saturationLevel = fs.getSaturationLevel();
		float exhaustionLevel = ReflectionAdapters.getExhaustionLevel(fs);
		int foodLevel = fs.getFoodLevel();

		// line 40 of FoodData Class to line 44 in 1.21.11
		if (exhaustionLevel > 4.0) {
			exhaustionLevel -= 4.0;
			ReflectionAdapters.setExhaustionLevel(fs, exhaustionLevel);
			if (saturationLevel > 0.0F) {
				saturationLevel = Math.max(saturationLevel - 1.0F, 0.0F);
				fs.setSaturation(saturationLevel);
			} else {
				if (foodLevel > 0) {
					fs.setFoodLevel(foodLevel - 1);
				}
			}
		}

	}

}
