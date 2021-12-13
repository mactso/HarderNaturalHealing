package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
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

	@SubscribeEvent
	public static void onPlayerHealing(PlayerTickEvent event) {

		if (MyConfig.isPeacefulHunger()) {
			FoodData fs = event.player.getFoodData();
			Difficulty difficulty = event.player.level.getDifficulty();
//			boolean secondTimer = (0 == event.player.level.getGameTime() % 20);
//			int secondTimerint = (int) event.player.level.getGameTime() % 20;
			if (event.phase == TickEvent.Phase.START) {
				MyConfig.setDebugLevel(0);
				if (event.side == LogicalSide.CLIENT) {
					cRegen = event.player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
					cSat = fs.saturationLevel;
					cExt = fs.exhaustionLevel;
					cFod = fs.foodLevel;
					cTim = fs.tickTimer;
					MyConfig.debugMsg(2, "(" + event.player.tickCount + ") C START cTim:" + cTim + " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod + ".");
				} else {
					sRegen = event.player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
					sSat = fs.saturationLevel;
					sExt = fs.exhaustionLevel;
					sFod = fs.foodLevel;
					sTim = fs.tickTimer;
					MyConfig.debugMsg(2, "(" + event.player.tickCount + ") S START sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod + ".");
				}
				MyConfig.setDebugLevel(0);
			}

			if (event.phase == TickEvent.Phase.END) {
				if (difficulty == Difficulty.PEACEFUL) {
					MyConfig.setDebugLevel(0);
					if (event.side == LogicalSide.CLIENT) {
						MyConfig.debugMsg(2, "(" + event.player.tickCount + ") C xENDx cTim:" + cTim + " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod + ".");
						fs.foodLevel = cFod;
					} else {
						MyConfig.debugMsg(2, "(" + event.player.tickCount + ") S xENDx sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod + ".");
//						fs.foodLevel = sFod;
						if ((sExt > fs.exhaustionLevel) && (sSat == 0) && (fs.foodLevel > 0)) {
							fs.foodLevel -= 1;
						}
						if (fs.foodLevel == 0) {
							if (++sTim > 80) {
								if (event.player.getHealth() > MyConfig.getMinimumStarvationHealth()) {
									event.player.hurt(DamageSource.STARVE, 1.0F);
								}
								sTim = 0;
							}
						}
						fs.tickTimer = sTim;
					}
				}
				MyConfig.setDebugLevel(0);

			}

		}
	}
}
