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

			if (event.phase == TickEvent.Phase.START) {
				if (event.side == LogicalSide.CLIENT) {
					cRegen = event.player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
					cSat = fs.foodSaturationLevel;
					cExt = fs.foodExhaustionLevel;
					cFod = fs.foodLevel;
					cTim = fs.foodTimer;
				} else {
					sRegen = event.player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
					sSat = fs.foodSaturationLevel;
					sExt = fs.foodExhaustionLevel;
					sFod = fs.foodLevel;
					sTim = fs.foodTimer;
				}
				if (secondTimer) {
					if (MyConfig.getDebugLevel() > 1) {
						System.out
						.println("START cTim:" + cTim + " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod + ".");
						System.out
						.println("START sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod + ".");
						System.out.println("cRegen:" + cRegen + " sRegen:" + sRegen);
					}
				}
			}

			if (event.phase == TickEvent.Phase.END) {
				if (difficulty == Difficulty.PEACEFUL) {
					if (event.side == LogicalSide.CLIENT) {
						fs.foodLevel = cFod;
						if ((cExt > fs.foodExhaustionLevel) && (cSat == 0) && (fs.foodLevel > 0)) {
							fs.foodLevel -= 1;
						}
						if (fs.foodLevel == 0) {
							if (++cTim > 80) {
								event.player.attackEntityFrom(DamageSource.STARVE, 1.0F);
								cTim = 0;
							}
						}
						fs.foodTimer = cTim;
					} else {
						fs.foodLevel = sFod;
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
				if (secondTimer) {
					if (MyConfig.getDebugLevel() > 1) {
						System.out.println("END cTim:" + cTim + " cSat:" + cSat + " cExt:" + cExt + " cFod:" + cFod + ".");
						System.out.println("END sTim:" + sTim + " sSat:" + sSat + " sExt:" + sExt + " sFod:" + sFod + ".");
						System.out.println("cRegen:" + cRegen + " sRegen:" + sRegen);
					}
				}

			}

		}
	}
}
