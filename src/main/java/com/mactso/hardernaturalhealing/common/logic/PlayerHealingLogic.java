package com.mactso.hardernaturalhealing.common.logic;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;

public class PlayerHealingLogic {

    private static final int TICKS_PER_SECOND = 20;
    private static final int STARVATION_INTERVAL = 4 * TICKS_PER_SECOND;

    public static void doPlayerHealing(ServerPlayer player) {

        long gameTime = player.level().getGameTime();

        // Heal/starve only once per second
        if (player.isDeadOrDying() || gameTime % TICKS_PER_SECOND != 0 || player.getHealth() >= player.getMaxHealth()) {
            return;
        }

        MyUtilities.debugMsg(1, "Handling wounded Player " + player.getName().getString());

        applyExtraExhaustion(player);
        handleStarvation(player, gameTime);

        if (!canHealAgainAfterCombat(player)) return;

         healPlayer(player);
    }

    private static void applyExtraExhaustion(ServerPlayer player) {
        double extra = MyConfig.getExtraExhaustionWhenHurt();
        if (extra > 0) player.getFoodData().addExhaustion((float) extra);
    }

    private static void handleStarvation(ServerPlayer player, long gameTime) {
        if (player.getFoodData().getFoodLevel() > 0) return;

        Difficulty dif = player.level().getDifficulty();
        if (!shouldHandleStarving(player, dif)) return;

        if (gameTime % STARVATION_INTERVAL == 0 && player.getHealth() > MyConfig.getMinimumStarvationHealth()) {
            player.hurt(player.damageSources().starve(), 1.0F);
        }
    }

    private static boolean shouldHandleStarving(ServerPlayer player, Difficulty dif) {
        return switch (dif) {
            case HARD -> false;
            case NORMAL -> player.getHealth() <= 1.0F;
            case EASY -> player.getHealth() <= 10.0F;
            default -> true;
        };
    }

    private static boolean canHealAgainAfterCombat(ServerPlayer player) {
        return player.getFoodData().getFoodLevel() >= MyConfig.getMinimumFoodHealingLevel() &&
               player.tickCount >= player.getLastHurtMobTimestamp() + MyConfig.getAttackHealingDelayTicks();
    }

    private static void healPlayer(ServerPlayer player) {
        player.heal((float) MyConfig.getHealingPerSecond());
        player.getFoodData().addExhaustion((float) MyConfig.getHealingExhaustionCost());
    }
}
