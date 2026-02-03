package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class PeacefulHealingLogic {

	public static float cSat = 0;
	public static float cExt = 0;
	public static int cFod = 0;
	public static int cTim = 0;

	public static float sSat = 0;
	public static float sExt = 0;
	public static int sFod = 0;
	public static int sTim = 0;

	static boolean cRegen;
	public static boolean sRegen;
	

	public static void peacefulHealingPreLogic(Player player) {
			ReflectionAdapters.initTickTimerField(); // Reflect into private FoodData.ticktimer
		
			FoodData fs = player.getFoodData();
	
			Level level = player.level();
			if (level.isClientSide()) {
				// issue they have removed gamerules from the client level
				// it's also possible that the server is sending messages about food levels
				// constantly now.
	//				cRegen = event.player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
				// however, it looks like cRegen isn't used.
				cSat = fs.getSaturationLevel();
	            cExt = ReflectionAdapters.getExhaustionLevel(fs);
				cFod = fs.getFoodLevel();
				try {
					cTim = ReflectionAdapters.tickTimerField.getInt(fs);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
	
				MyUtilities.debugMsg(2, "(" + player.tickCount + ") C START cTim:" + cTim + " cSat:" + cSat + " cExt:"
						+ cExt + " cFod:" + cFod);
			} else {
				if ((player instanceof ServerPlayer sp)) {
	
					ServerLevel slevel = sp.level();;
					sRegen = slevel.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
					sSat = fs.getSaturationLevel();
	                sExt = ReflectionAdapters.getExhaustionLevel(fs);
					sFod = fs.getFoodLevel();
					try {
						sTim = ReflectionAdapters.tickTimerField.getInt(fs);
					} catch (IllegalAccessException e) {
						throw new IllegalStateException(e);
					}
	
					MyUtilities.debugMsg(2, "(" + player.tickCount + ") S START sTim:" + sTim + " sSat:" + sSat + " sExt:"
							+ sExt + " sFod:" + sFod);
				}
			}
		}

	public static void peacefulHealingPostLogic(Player player) {
		
		ReflectionAdapters.initTickTimerField(); // Reflect into private FoodData.ticktimer
		
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
		ServerPlayer sp = (ServerPlayer) player;
		ServerLevel serverLevel = (ServerLevel) sp.level();
		
		MyUtilities.debugMsg(2,
				"(" + player.tickCount + ") S END sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod);
	
	    if ((sExt > ReflectionAdapters.getExhaustionLevel(fs)) && sSat == 0 && fs.getFoodLevel() > 0) {
	        fs.setFoodLevel(fs.getFoodLevel() - 1);
	    }
	
		if (fs.getFoodLevel() == 0) {
			if (++sTim > 80) {
				if (player.getHealth() > MyConfig.getMinimumStarvationHealth()) {
					player.hurtServer(serverLevel, serverLevel.damageSources().starve(), cExt);
				}
				sTim = 0;
			}
		}
	
		try {
			ReflectionAdapters.tickTimerField.setInt(fs, sTim);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
