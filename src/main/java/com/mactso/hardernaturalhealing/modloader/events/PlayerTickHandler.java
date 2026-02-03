package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.logic.PlayerHealingLogic;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PlayerTickHandler {

    @SubscribeEvent
    public void onPlayerTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Delegate all logic to the logic class
        PlayerHealingLogic.doPlayerHealing(player);
    }
}
