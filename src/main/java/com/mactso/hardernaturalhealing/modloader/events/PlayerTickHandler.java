package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.logic.PlayerHungerLogic;
import com.mactso.hardernaturalhealing.common.logic.PlayerHealthLogic;
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
		if (!(event.getEntity() instanceof ServerPlayer sp))
			return;

        // Delegate all logic to the logic class
		FoodData foodData = sp.getFoodData();
		if (MyConfig.isDebug())
			MyUtilities.debugMsg(0, "PreTickTop Player :" + sp.getName().toString() + " Food = " + foodData.getFoodLevel()
					+ " Saturation = " + foodData.getSaturationLevel());
		
		// hunger from Hunger Effect.
		PlayerHungerLogic.processHungerPotionEffect(sp);
		
		// hunger from activities
		PlayerHungerLogic.processPeacefulHunger(sp);
		
		// heal wounded players if conditions met.
        PlayerHealthLogic.processPlayerHealth(sp);
        
		// hunger from healing
		PlayerHungerLogic.processPeacefulHunger(sp);
		

		if (MyConfig.isDebug())
			MyUtilities.debugMsg(0, "PostTickTop Player :" + sp.getName().toString() + " Food = " + foodData.getFoodLevel()
					+ " Saturation = " + foodData.getSaturationLevel());
    }
}
