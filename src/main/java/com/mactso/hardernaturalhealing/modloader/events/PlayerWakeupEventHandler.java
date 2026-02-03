package com.mactso.hardernaturalhealing.modloader.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraft.world.entity.player.Player;

import com.mactso.hardernaturalhealing.common.logic.PlayerWakeupLogic;

public class PlayerWakeupEventHandler {

    @SubscribeEvent
    public  void onPlayerWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        PlayerWakeupLogic.handleWakeup(player);
    }
}
