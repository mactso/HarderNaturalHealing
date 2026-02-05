package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.logic.PeacefulHungerLogic;
import com.mactso.hardernaturalhealing.common.logic.PlayerHealingLogic;
import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PlayerTickHandler {

	// this intercepts the player tick at the top right before it 
	// resolves exhaustion, saturation, and hunger.
    @SubscribeEvent
    public void onPlayerTick(EntityTickEvent.Pre event) {
		if (!(event.getEntity() instanceof ServerPlayer player))
			return;

        // Delegate all logic to the logic class
		FoodData foodData = player.getFoodData();
		if (MyConfig.isDebug())
			MyUtilities.debugMsg(0, "PreTickTop Player :" + player.getName().toString() + " Food = " + foodData.getFoodLevel()
					+ " Saturation = " + foodData.getSaturationLevel());
		// hunger from activities
		PeacefulHungerLogic.doPeacefulHunger(player);
        PlayerHealingLogic.doPlayerHealing(player);
		// hunger from healing
		PeacefulHungerLogic.doPeacefulHunger(player);

		if (MyConfig.isDebug())
			MyUtilities.debugMsg(0, "PostTickTop Player :" + player.getName().toString() + " Food = " + foodData.getFoodLevel()
					+ " Saturation = " + foodData.getSaturationLevel());
    }
}
