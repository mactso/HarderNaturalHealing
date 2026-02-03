package com.mactso.hardernaturalhealing.modloader.events;

import java.lang.reflect.Field;

import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.Clone;

public class PlayerDeathEventHandler {

	private static Field exhaustionLevelField;

	/**
	 * Resolves FoodData.exhaustionLevel exactly once.
	 * Hard-fails if reflection is not possible.
	 */
	private static void initExhaustionLevelField() {
		Field f = exhaustionLevelField;
		if (f != null) {
			return;
		}

		try {
			// Mojang-mapped name (NeoForge / 1.21.x)
			f = FoodData.class.getDeclaredField("exhaustionLevel");
			f.setAccessible(true);
			exhaustionLevelField = f;
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(
				"HarderNaturalHealing: Failed to reflect FoodData.exhaustionLevel. Mod cannot function.",
				e
			);
		}
	}

	private static void setExhaustionLevel(FoodData foodData, float value) {
		initExhaustionLevelField();
		try {
			exhaustionLevelField.setFloat(foodData, value);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(
				"HarderNaturalHealing: Unable to set FoodData.exhaustionLevel",
				e
			);
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(Clone event) {
		if (!event.isWasDeath()) {
			return;
		}

		Player p = event.getEntity();

		int healthAfterDeath = MyConfig.getHealthAfterDeath();
		if (healthAfterDeath < 20) {
			p.setHealth(healthAfterDeath);
		}

		int hungerAfterDeath = MyConfig.getHungerAfterDeath();
		if (hungerAfterDeath < 20) {
			p.getFoodData().setFoodLevel(hungerAfterDeath);
			p.getFoodData().setSaturation(0.0F);
			setExhaustionLevel(p.getFoodData(), 3.9F);
		}
	}
}