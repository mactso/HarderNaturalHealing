package com.mactso.hardernaturalhealing.modloader.events;

import java.lang.reflect.Field;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
	private static volatile Field exhaustionLevelField = null;
	
	/**
	 * Resolves FoodData.tickTimer exactly once. Hard-fails if reflection is not
	 * possible.
	 */


    /** Resolves FoodData.tickTimer exactly once. Hard-fails if reflection is not possible. */
    private static void initTickTimerField() {
        Field f = tickTimerField;
        if (f != null) return;

        try {
            f = FoodData.class.getDeclaredField("tickTimer");
            f.setAccessible(true);
            tickTimerField = f;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Failed to reflect FoodData.tickTimer. Mod cannot function.", e
            );
        }
    }

    /** Resolves FoodData.exhaustionLevel exactly once. Hard-fails if reflection is not possible. */
    private static void initExhaustionLevelField() {
        Field f = exhaustionLevelField;
        if (f != null) return;

        try {
            f = FoodData.class.getDeclaredField("exhaustionLevel");
            f.setAccessible(true);
            exhaustionLevelField = f;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Failed to reflect FoodData.exhaustionLevel. Mod cannot function.", e
            );
        }
    }
    
    /** Gets the exhaustion level via reflection */
    private static float getExhaustionLevel(FoodData fs) {
        initExhaustionLevelField();
        try {
            return exhaustionLevelField.getFloat(fs);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Unable to get FoodData.exhaustionLevel", e
            );
        }
    }

    /** Sets the exhaustion level via reflection */
    private static void setExhaustionLevel(FoodData fs, float value) {
        initExhaustionLevelField();
        try {
            exhaustionLevelField.setFloat(fs, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Unable to set FoodData.exhaustionLevel", e
            );
        }
    }
    
	@SubscribeEvent
	public void onPlayerTickPre(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();

		if (!MyConfig.isPeacefulHunger())
			return;

		// Resolve reflection once, hard-fail inside

		initTickTimerField();

		FoodData fs = player.getFoodData();

		Level level = player.level();
		if (level.isClientSide()) {
			// issue they have removed gamerules from the client level
			// it's also possible that the server is sending messages about food levels
			// constantly now.
//				cRegen = event.player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
			// however, it looks like cRegen isn't used.
			cSat = fs.getSaturationLevel();
            cExt = getExhaustionLevel(fs);
			cFod = fs.getFoodLevel();
			try {
				cTim = tickTimerField.getInt(fs);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}

			MyUtilities.debugMsg(2, "(" + player.tickCount + ") C START cTim:" + cTim + " cSat:" + cSat + " cExt:"
					+ cExt + " cFod:" + cFod);
		} else {
			if ((event.getEntity() instanceof ServerPlayer sp)) {

				ServerLevel slevel = sp.serverLevel();;
				sRegen = slevel.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
				sSat = fs.getSaturationLevel();
                sExt = getExhaustionLevel(fs);
				sFod = fs.getFoodLevel();
				try {
					sTim = tickTimerField.getInt(fs);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				}

				MyUtilities.debugMsg(2, "(" + player.tickCount + ") S START sTim:" + sTim + " sSat:" + sSat + " sExt:"
						+ sExt + " sFod:" + sFod);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTickPost(PlayerTickEvent.Post event) {
		Player player = event.getEntity();

		if (!MyConfig.isPeacefulHunger())
			return;

		initTickTimerField();

		if (player.level().getDifficulty() != Difficulty.PEACEFUL)
			return;

		FoodData fs = player.getFoodData();

		Level level = player.level();
		if (level.isClientSide()) {
			MyUtilities.debugMsg(2, "(" + player.tickCount + ") C END cTim:" + cTim + " cSat:" + cSat + " cExt:" + cExt
					+ " cFod:" + cFod);

			fs.setFoodLevel(cFod);
			return;
		}

		// ---- SERVER LOGIC ----

		MyUtilities.debugMsg(2,
				"(" + player.tickCount + ") S END sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod);

        if ((sExt > getExhaustionLevel(fs)) && sSat == 0 && fs.getFoodLevel() > 0) {
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
