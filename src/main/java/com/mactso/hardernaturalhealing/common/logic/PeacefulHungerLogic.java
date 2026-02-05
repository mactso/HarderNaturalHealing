package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;

/**
 * PeacefulHungerLogic This overrides behavior of lines 40-44 of FoodData so
 * that Peaceful difficulty also consumes food. called from PlayerTickEvent
 * Handler.
 */

public class PeacefulHungerLogic {

	public static void doPeacefulHunger(Player player) {

		if (!(MyConfig.isPeacefulHunger()))
			return;

		Level level = player.level();
		BlockPos pos = player.blockPosition();
		if (level.isClientSide())
			return;

		if (player.level().getDifficulty() != Difficulty.PEACEFUL)
			return;

		// Exit early if the chunk is not loaded during debugging.
		if (!level.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) {
		    return;
		}
		
		ServerPlayer sp = (ServerPlayer) player;

		addHungerEffectExhaustion(sp);
		
		FoodData fs = sp.getFoodData();
		float saturationLevel = fs.getSaturationLevel();
		float exhaustionLevel = ReflectionAdapters.getExhaustionLevel(fs);
		int foodLevel = fs.getFoodLevel();

		// line 40 of FoodData Class to line 44 in 1.21.11
		if (MyConfig.isDebug())
			System.out.println("afterHunger"+exhaustionLevel);
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

	// Minecraft disables HUNGER in easy and peaceful below 6 hunger.
    // this restores the effect of hunger between 2 and 6 hunger.	
	// the Hunger effect still will not starve the player to death
	// in peaceful or easy.
	private static void addHungerEffectExhaustion(ServerPlayer player) {

		if (!player.hasEffect(MobEffects.HUNGER))
			return;

		Difficulty difficulty = player.level().getDifficulty();
		if (difficulty != Difficulty.PEACEFUL && difficulty != Difficulty.EASY)
			return;

		FoodData fs = player.getFoodData();
		if (fs.getFoodLevel() >= 7)
			return;
		if (fs.getFoodLevel() <= 1)
			return;
		int amplifier = player.getEffect(MobEffects.HUNGER).getAmplifier();
		float exhaustion = 0.005F * (amplifier + 1);

		float current = ReflectionAdapters.getExhaustionLevel(fs);
		ReflectionAdapters.setExhaustionLevel(fs, Math.min(current + exhaustion, 40.0F));
	}

}
