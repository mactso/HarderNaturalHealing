package com.mactso.hardernaturalhealing.events;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanValue;
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
			FoodStats fs = event.player.getFoodStats();
			Difficulty difficulty = event.player.world.getDifficulty();
			boolean secondTimer = (0 == event.player.world.getGameTime() % 20);
			int secondTimerint = (int) event.player.world.getGameTime() % 20;
			if (event.phase == TickEvent.Phase.START) {
				MyConfig.setDebugLevel(0);
				if (event.side == LogicalSide.CLIENT) {
					cRegen = event.player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
					cSat = fs.foodSaturationLevel;
					cExt = fs.foodExhaustionLevel;
					cFod = fs.foodLevel;
					cTim = fs.foodTimer;
					MyConfig.debugMsg(2, "(" + event.player.ticksExisted + ") C START cTim:" + cTim + " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod + ".");
				} else {
					sRegen = event.player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
					sSat = fs.foodSaturationLevel;
					sExt = fs.foodExhaustionLevel;
					sFod = fs.foodLevel;
					sTim = fs.foodTimer;
					MyConfig.debugMsg(2, "(" + event.player.ticksExisted + ") S START sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod + ".");
				}
				MyConfig.setDebugLevel(0);
			}

			if (event.phase == TickEvent.Phase.END) {
				if (difficulty == Difficulty.PEACEFUL) {
					MyConfig.setDebugLevel(0);
					if (event.side == LogicalSide.CLIENT) {
						MyConfig.debugMsg(2, "(" + event.player.ticksExisted + ") C xENDx cTim:" + cTim + " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod + ".");
						fs.foodLevel = cFod;
					} else {
						MyConfig.debugMsg(2, "(" + event.player.ticksExisted + ") S xENDx sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod + ".");
//						fs.foodLevel = sFod;
						if ((sExt > fs.foodExhaustionLevel) && (sSat == 0) && (fs.foodLevel > 0)) {
							fs.foodLevel -= 1;
						}
						if (fs.foodLevel == 0) {
							if (++sTim > 80) {
								if (event.player.getHealth() > MyConfig.getMinimumStarvationHealth()) {
									event.player.attackEntityFrom(DamageSource.STARVE, 1.0F);
								}
								sTim = 0;
							}
						}
						fs.foodTimer = sTim;
					}
				}
				MyConfig.setDebugLevel(0);

			}

		}
	}
}
