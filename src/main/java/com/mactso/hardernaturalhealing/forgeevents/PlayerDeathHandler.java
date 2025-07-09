package com.mactso.hardernaturalhealing.forgeevents;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.coremod.api.ASMAPI;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class PlayerDeathHandler {
	private static final Logger LOGGER = LogManager.getLogger();
	private static Field fieldExhaustionLevel = null;
	static boolean exhaustionLevelAvailable = false;
	
	static {
		try {
			String name = ASMAPI.mapField("exhaustionLevel");
			fieldExhaustionLevel = FoodData.class.getDeclaredField(name);
			fieldExhaustionLevel.setAccessible(true);
			exhaustionLevelAvailable = true;
		} catch (Exception e) {
			LOGGER.error("XXX Unexpected Reflection Failure: exhaustionLevel ");
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(Clone event) {
		Player p = event.getEntity();
		int v = MyConfig.getHealthAfterDeath();
		if (event.isWasDeath()) {
			if (MyConfig.getHealthAfterDeath() < 20) {
				p.setHealth(MyConfig.getHealthAfterDeath());
			}
			if (MyConfig.getHungerAfterDeath() < 20) {
				p.getFoodData().setFoodLevel((MyConfig.getHungerAfterDeath()));
				p.getFoodData().setSaturation(0);
				setExhaustionLevel(p.getFoodData(), 3.9f);
			}
		}
	}
	
    public static void setExhaustionLevel(FoodData foodData, float newValue) {
        try {
            fieldExhaustionLevel.setFloat(foodData, newValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
