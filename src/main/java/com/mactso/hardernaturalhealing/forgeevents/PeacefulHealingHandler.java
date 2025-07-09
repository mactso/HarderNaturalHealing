package com.mactso.hardernaturalhealing.forgeevents;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.utility.Utility;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PeacefulHealingHandler {

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

	private static Field tickTimerField = null;
	static boolean tickTimerAvailable = false;
	static boolean tickTimerErrorReported = false;
	private static Field exhaustionLevelField = null;
	static boolean exhaustionLevelAvailable = false;
	static boolean exhaustionLevelErrorReported = false;

	private static final Logger LOGGER = LogManager.getLogger();

	// FD: net/minecraft/world/food/FoodData/f_38699_
	// net/minecraft/world/food/FoodData/tickTimer
	static {
		// possible ticktimer line goes here.
		try {
			String name = ASMAPI.mapField("tickTimer"); // f_38699
			tickTimerField = FoodData.class.getDeclaredField(name);
			tickTimerField.setAccessible(true);
			tickTimerAvailable = true;
		} catch (Exception e) {
			LOGGER.error("XXX Unexpected Reflection Failure: ticktimer ");
		}

		try {
			String name = ASMAPI.mapField("exhaustionLevel");
			exhaustionLevelField = FoodData.class.getDeclaredField(name);
			exhaustionLevelField.setAccessible(true);
			exhaustionLevelAvailable = true;
		} catch (Exception e) {
			LOGGER.error("XXX Unexpected Reflection Failure: exhaustionLevel ");
		}
	}

	@SubscribeEvent
	public static void onPlayerHealing(PlayerTickEvent event) {

		if (MyConfig.isPeacefulHunger()) {
			FoodData foodData = event.player.getFoodData();
			Difficulty difficulty = event.player.level().getDifficulty();
			if (event.phase == TickEvent.Phase.START) {
				MyConfig.setDebugLevel(0);
				if (event.side == LogicalSide.CLIENT) {
					// issue they have removed gamerules from the client level
//					cRegen = event.player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
					// however, it looks like cRegen isn't used.
					cSat = foodData.getSaturationLevel();
					cFod = foodData.getFoodLevel();
					cExt = getExhaustionLevel(foodData, " client ");
					cTim = getTickTimer(foodData);
					Utility.debugMsg(2, "(" + event.player.tickCount + ") C START cTim:" + cTim + " cSat:" + cSat
							+ " cExt:" + cExt + " cFod:" + cFod + ".");
				} else {
					ServerLevel slevel = (ServerLevel) event.player.level();
					sRegen = slevel.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
					sSat = foodData.getSaturationLevel();
					sFod = foodData.getFoodLevel();
//					sTim = fs.tickTimer;
					sExt = getExhaustionLevel(foodData, " server ");
					sTim = getTickTimer(foodData);
					try {
						sTim = (int) tickTimerField.get(foodData);
					} catch (IllegalArgumentException e) {
						LOGGER.error("Illegal Argument: failed to get server FoodData tickTimer.");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						LOGGER.error("Illegal Access: failed to get server FoodData tickTimer.");
						e.printStackTrace();
					}

					Utility.debugMsg(2, "(" + event.player.tickCount + ") S START sTim:" + sTim + " sSat:" + sSat
							+ " sExt:" + sExt + " sFod:" + sFod + ".");
				}
			}

			if (event.phase == TickEvent.Phase.END) {
				if (difficulty == Difficulty.PEACEFUL) {
					MyConfig.setDebugLevel(0);
					if (event.side == LogicalSide.CLIENT) {
						Utility.debugMsg(2, "(" + event.player.tickCount + ") C xENDx cTim:" + cTim + " cSat:" + cSat
								+ " cExt:" + cExt + " cFod:" + cFod + ".");
						foodData.setFoodLevel(cFod);
					} else {
						Utility.debugMsg(2, "(" + event.player.tickCount + ") S xENDx sTim:" + sTim + " sSat:" + sSat
								+ " sExt:" + sExt + " sFod:" + sFod + ".");
//						fs.foodLevel = sFod;

						if ((sExt > getExhaustionLevel(foodData, "")) && (sSat == 0) && (foodData.getFoodLevel() > 0)) {
							foodData.setFoodLevel(foodData.getFoodLevel() - 1);
						}
						if (foodData.getFoodLevel() == 0) {
							if (++sTim > 80) {
								if (event.player.getHealth() > MyConfig.getMinimumStarvationHealth()) {
									event.player.hurt(event.player.damageSources().starve(), 1.0F);
								}
								sTim = 0;
							}
						}
//						fs.tickTimer = sTim;
						try {
							tickTimerField.setInt(foodData, sTim);
						} catch (IllegalArgumentException e) {
							LOGGER.error("Illegal Argument: failed to update FoodData tickTimer.");
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							LOGGER.error("Illegal Access: failed to update FoodData tickTimer.");
							e.printStackTrace();
						}
					}
				}

			}

		}
	}

	private static int getTickTimer(FoodData fs) {
		if (!tickTimerAvailable) {
			if (!tickTimerErrorReported) {
				tickTimerErrorReported = true;
				LOGGER.error("HarderNaturalHealing: FoodData tickTimer not available.");
			}
			return 0;
		}

		int i = 0;
		try {
			i = (int) tickTimerField.get(fs);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Illegal Argument: failed to get client FoodData tickTimer.");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOGGER.error("Illegal Access: failed to get client FoodData tickTimer.");
			e.printStackTrace();
		}
		return i;
	}

	private static float getExhaustionLevel(FoodData fs, String side) {
		if (!exhaustionLevelAvailable) {
			if (!exhaustionLevelErrorReported) {
				exhaustionLevelErrorReported = true;
				LOGGER.error("HarderNaturalHealing: FoodData exhaustionLevel not available.");
			}
		}

		float f = 0.0f;
		try {
			f = (float) exhaustionLevelField.get(fs);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Illegal Argument: failed to get " + side + " FoodData exhaustionLevel.");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOGGER.error("Illegal Access: failed to get " + side + " FoodData exhaustionLevel value.");
			e.printStackTrace();
		}
		return f;
	}
	
    public static void setExhaustionLevel(FoodData foodData, float newValue, String side) {
        try {
            exhaustionLevelField.setFloat(foodData, newValue);
        } catch (IllegalAccessException e) {
			LOGGER.error("Illegal Access: failed to set " + side + " FoodData exhaustionLevel value.");
            e.printStackTrace();
        }
    }
}
