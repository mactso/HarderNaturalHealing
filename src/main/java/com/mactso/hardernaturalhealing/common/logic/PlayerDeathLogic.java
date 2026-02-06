package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.world.entity.player.Player;

/**
 * Adjusts a player's health and hunger after death on the server side.
 * Ensures health does not exceed configured post-death value.
 * Sets food level, resets saturation, and applies exhaustion.
 * Skips execution on the client side.
 */
public class PlayerDeathLogic {

	public static void setHealthAndHunger(Player p) {
		// REQUIRED by NeoForge: prevent client execution
	    if (p.level().isClientSide()) {
	        return;
	    }
		int healthAfterDeath = MyConfig.getHealthAfterDeath();
		if (healthAfterDeath < 20) {
			p.setHealth(healthAfterDeath);
		}
	
		int hungerAfterDeath = MyConfig.getHungerAfterDeath();
		if (hungerAfterDeath < 20) {
			p.getFoodData().setFoodLevel(hungerAfterDeath);
			p.getFoodData().setSaturation(0.0F);
			ReflectionAdapters.setExhaustionLevel(p.getFoodData(), 3.9F);
		}
	}

}
