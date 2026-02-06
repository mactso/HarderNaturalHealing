package com.mactso.hardernaturalhealing.modloader.events;

import com.mactso.hardernaturalhealing.common.logic.PlayerHungerLogic;
import com.mactso.hardernaturalhealing.common.logic.PlayerHealthLogic;
import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class PlayerTickHandler {

	// this intercepts the player tick at the top right before it
	// resolves exhaustion, saturation, and hunger.
	@SubscribeEvent
	public void onPlayerTick(EntityTickEvent.Pre event) {

		// Client Side Code  Sometimes in 1.21.1 the client natural regeneration rule desyncs with the server in Peaceful Mode.
		// Starting in 1.21.1 the serverlevel is authoritative for hunger level so this doesn't happen.
		if (!(event.getEntity() instanceof ServerPlayer sp)) {
			Entity player = event.getEntity();
			boolean clientNaturalRegen = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
			if (clientNaturalRegen) {
				GameRules.BooleanValue rule = (GameRules.BooleanValue) player.level().getGameRules()
						.getRule(GameRules.RULE_NATURAL_REGENERATION);
				rule.set(false, null);
				if (MyConfig.isDebug())
					MyUtilities.debugMsg(1, "PlayerTickPre Client Natural Regeneration Reset = " + clientNaturalRegen);
			}
			return;

		}

		// Delegate all logic to the logic class
		FoodData foodData = sp.getFoodData();
		if (MyConfig.isDebug())
			MyUtilities.debugMsg(1, "PreTickBefore Player :" + sp.getName().toString() + " Food = "
					+ foodData.getFoodLevel() + " Saturation = " + foodData.getSaturationLevel());

		// hunger from Hunger Effect.
		PlayerHungerLogic.processHungerPotionEffect(sp);

		// hunger from activities
		PlayerHungerLogic.processPeacefulHunger(sp);

		// heal wounded players if conditions met.
		PlayerHealthLogic.processPlayerHealth(sp);

		// hunger from healing
		PlayerHungerLogic.processPeacefulHunger(sp);

		if (MyConfig.isDebug())
			MyUtilities.debugMsg(1, "PreTickAfter Player :" + sp.getName().toString() + " Food = "
					+ foodData.getFoodLevel() + " Saturation = " + foodData.getSaturationLevel());
	}

	@SubscribeEvent
	public void onPlayerTick(EntityTickEvent.Post event) {

		// Client Side Code  Sometimes in 1.21.1 the client natural regeneration rule desyncs with the server in Peaceful Mode.
		if (!(event.getEntity() instanceof ServerPlayer sp)) {
			Entity player = event.getEntity();
			boolean clientNaturalRegen = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
			if (clientNaturalRegen) {
				MyUtilities.debugMsg(0, "PlayerTickPost Client Natural Regeneration = " + clientNaturalRegen);
				GameRules.BooleanValue rule = (GameRules.BooleanValue) player.level().getGameRules()
						.getRule(GameRules.RULE_NATURAL_REGENERATION);
				rule.set(false, null);
				MyUtilities.debugMsg(0, "PlayerTickPre Client Natural Regeneration Reset = " + clientNaturalRegen);
			}
			return;

		}
	}
}
