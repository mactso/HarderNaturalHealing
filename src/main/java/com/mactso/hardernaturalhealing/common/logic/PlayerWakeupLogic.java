package com.mactso.hardernaturalhealing.common.logic;

import net.minecraft.world.entity.player.Player;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

public class PlayerWakeupLogic {

    /**
     * Handles player healing on wakeup.
     * Pure logic, decoupled from events.
     *
     * @param player the player entity
     */
    public static void handleWakeup(Player player) {
        // Skip client side
        if (player.level().isClientSide()) return;

        // Only heal if it's daytime
        if (player.level().getDayTime()%24000 > 20)
        	return;

        // Heal player using config value
        float healingAmount = (float) MyConfig.getWakeupHealingAmount();
        player.heal(healingAmount);

    }
}
