package com.mactso.hardernaturalhealing.common.logic;

import net.minecraft.core.Holder.Reference;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.timeline.Timeline;
import net.minecraft.world.timeline.Timelines;

import java.util.Optional;

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
        ServerLevel serverLevel = (ServerLevel) player.level();
        // note that this is 'kludgy' since it doesn't use the new TimeLine approach.
        // Only heal if it's daytime
        long t1 = player.level().getDefaultClockTime();  // 0 to 24000 
        long t2 = player.level().getOverworldClockTime();  // 0 to 24000
        long t3 = player.level().getGameTime();  // this is unadjusted ticks since minecraft started.

        if (t1%24000 > 20)
        	return;
        
        // this is more along the proper lines of using hte Timelines code.  I'm just not going ot use
        // it yet.
        
        ResourceKey<Timeline> tod = Timelines.OVERWORLD_DAY;
        
        Optional<Reference<Timeline>> todra = serverLevel.registryAccess().get(Timelines.OVERWORLD_DAY);
        if (todra.isPresent()) {
             Reference<Timeline> dayTimeline = todra.get();
             if (dayTimeline.value() != null) {
                 int currentDay = dayTimeline.value().getPeriodCount(serverLevel.clockManager());
                 long currentTick = dayTimeline.value().getCurrentTicks(serverLevel.clockManager());
                 long currentTotalTick = dayTimeline.value().getTotalTicks(serverLevel.clockManager());
                 int debug = 5;
                		 
             }
        }

        // Heal player using config value
        float healingAmount = (float) MyConfig.getWakeupHealingAmount();
        player.heal(healingAmount);

    }
}
