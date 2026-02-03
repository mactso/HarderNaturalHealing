package com.mactso.hardernaturalhealing.modloader.events;

import java.lang.reflect.Field;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;



public class PeacefulHealingEventHandler {

	static float cSat = 0;
	static float cExt = 0;
	static int cFod = 0;
	static int cTim = 0;

	static float sSat = 0;
	static float sExt = 0;
	static int sFod = 0;
	static int sTim = 0;

	static boolean cRegen;
	static boolean sRegen;

	private static volatile Field tickTimerField = null;


	/**
	 * Resolves FoodData.tickTimer exactly once.
	 * Hard-fails if reflection is not possible.
	 */
	private static void initTickTimerField() {
	    Field f = tickTimerField;
	    if ((f != null)) {
	        return;
	    }

	    try {
	        // Mojang-mapped name (NeoForge / 1.21.x)
	        f = FoodData.class.getDeclaredField("tickTimer");
	        f.setAccessible(true);
	        tickTimerField = f;

	        return;
	    } catch (ReflectiveOperationException e) {
	        throw new IllegalStateException(
	            "HarderNaturalHealing: Failed to reflect FoodData.tickTimer. Mod cannot function.",
	            e
	        );
	    }
	}

	@SubscribeEvent
	public void onPlayerTickPre(PlayerTickEvent.Pre event) {
	    Player player = event.getEntity();

	    if (!MyConfig.isPeacefulHunger()) return;

	    // Resolve reflection once, hard-fail inside

	    initTickTimerField();



	    FoodData fs = player.getFoodData();
	    
	    Level level = player.level();
	    if (level.isClientSide()) {
	        cRegen = player.level().getGameRules()
	                .getBoolean(GameRules.RULE_NATURAL_REGENERATION);
	        cSat = fs.getSaturationLevel();
	        cExt = fs.getExhaustionLevel();
	        cFod = fs.getFoodLevel();
	        try {
	            cTim = tickTimerField.getInt(fs);
	        } catch (IllegalAccessException e) {
	            throw new IllegalStateException(e);
	        }

	        MyUtilities.debugMsg(2,
	            "(" + player.tickCount + ") C START cTim:" + cTim +
	            " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod);
	    }
	    else {
	        sRegen = player.level().getGameRules()
	                .getBoolean(GameRules.RULE_NATURAL_REGENERATION);
	        sSat = fs.getSaturationLevel();
	        sExt = fs.getExhaustionLevel();
	        sFod = fs.getFoodLevel();
	        try {
	            sTim = tickTimerField.getInt(fs);
	        } catch (IllegalAccessException e) {
	            throw new IllegalStateException(e);
	        }

	        MyUtilities.debugMsg(2,
	            "(" + player.tickCount + ") S START sTim:" + sTim +
	            " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod);
	    }
	}
	@SubscribeEvent
	public void onPlayerTickPost(PlayerTickEvent.Post event) {
	    Player player = event.getEntity();

	    if (!MyConfig.isPeacefulHunger()) return;

	    	initTickTimerField();


	    if (player.level().getDifficulty() != Difficulty.PEACEFUL) return;

	    
	    FoodData fs = player.getFoodData();

	    Level level = player.level();
	    if (level.isClientSide()) {
	        MyUtilities.debugMsg(2,
	            "(" + player.tickCount + ") C END cTim:" + cTim +
	            " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod);

	        fs.setFoodLevel(cFod);
	        return;
	    }

	    // ---- SERVER LOGIC ----

	    MyUtilities.debugMsg(2,
	        "(" + player.tickCount + ") S END sTim:" + sTim +
	        " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod);

	    if ((sExt > fs.getExhaustionLevel()) && sSat == 0 && fs.getFoodLevel() > 0) {
	        fs.setFoodLevel(fs.getFoodLevel() - 1);
	    }

	    if (fs.getFoodLevel() == 0) {
	        if (++sTim > 80) {
	            if (player.getHealth() > MyConfig.getMinimumStarvationHealth()) {
	                player.hurt(player.damageSources().starve(), 1.0F);
	            }
	            sTim = 0;
	        }
	    }

	    try {
	        tickTimerField.setInt(fs, sTim);
	    } catch (IllegalAccessException e) {
	        throw new IllegalStateException(e);
	    }
	}

}
