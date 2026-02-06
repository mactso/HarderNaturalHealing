package com.mactso.hardernaturalhealing.modloader.config;

import org.apache.commons.lang3.tuple.Pair;

import com.mactso.hardernaturalhealing.modloader.main.Main;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

/**
 * Central configuration definition and runtime access point for Harder
 * Spawners.
 *
 * <p>
 * This class defines all NeoForge {@link ModConfigSpec} values used by the mod,
 * grouped into logical sections such as debug options, spawner behavior,
 * spawning mechanics, environmental effects, and spawner lifespan.
 * </p>
 *
 * <p>
 * Configuration values are baked into static runtime fields on load or reload
 * via {@link #bakeConfig()} to allow fast access during gameplay without
 * repeatedly querying the config system.
 * </p>
 */
@EventBusSubscriber(modid = Main.MODID)
public class MyConfig {

	public static final Common COMMON;
	public static final ModConfigSpec COMMON_SPEC;
	

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}
	

	/*
	 * -------------------------------------------------------------------------
	 * Config load handling
	 * ----------------------------------------------------------------------
	 */
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfigEvent configEvent) {
		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC) {
			bakeConfig();

		}
	}

	/*
	 * -------------------------------------------------------------------------
	 * Config specification
	 * ----------------------------------------------------------------------
	 */
	public static class Common {
		/*
		 * ----------------------------- Debug & Messaging --------------------------
		 */
		public final IntValue debugLevel;
		public final IntValue attackHealingDelayTicks;
		public final IntValue maxBonusHitPointTotems;
		public final IntValue healthAfterDeath;
		public final IntValue hungerAfterDeath;
		public final DoubleValue extraExhaustionWhenHurt;
		public final DoubleValue healingPerSecond;
		public final DoubleValue minimumFoodHealingLevel;
		public final DoubleValue healingExhaustionCost;
		public final DoubleValue wakeupHealingAmount;
		public final IntValue minimumStarvationHealth;
		public final BooleanValue peacefulHunger;

		public Common(ModConfigSpec.Builder builder) {
			builder.push("Harder Natural Healing Control Values");

			debugLevel = builder.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel")
					.defineInRange("debugLevel", () -> 0, 0, 2);

			attackHealingDelayTicks = builder.comment("Attack Healing Delay in Ticks")
					.translation(Main.MODID + ".config." + "attackHealingDelayTicks")
					.defineInRange("attackHealingDelayTicks", () -> 200, 0, 3600);

			maxBonusHitPointTotems = builder.comment("Max Bonus HitPoint Totems")
					.translation(Main.MODID + ".config." + "maxBonusHitPointTotems")
					.defineInRange("maxBonusHitPointTotems", () -> 300, 0, 1200);

			
			healingPerSecond = builder.comment("healingPerSecond")
					.translation(Main.MODID + ".config." + "healingPerSecond")
					.defineInRange("healingPerSecond", () -> 0.25, 0.0, 10.0);

			healthAfterDeath = builder.comment("healthAfterDeath")
					.translation(Main.MODID + ".config." + "healthAfterDeath")
					.defineInRange("healthAfterDeath", () -> 20, 0, 20);
			
			hungerAfterDeath = builder.comment("hungerAfterDeath")
					.translation(Main.MODID + ".config." + "hungerAfterDeath")
					.defineInRange("hungerAfterDeath", () -> 20, 0, 20);

			
			minimumFoodHealingLevel = builder.comment("minimumFoodHealingLevel")
					.translation(Main.MODID + ".config." + "minimumFoodHealingLevel")
					.defineInRange("minimumFoodHealingLevel", () -> 16.0, 0.0, 22.0);

			healingExhaustionCost = builder.comment("healingExhaustionCost - Hunger exhausted per healing event.")
					.translation(Main.MODID + ".config." + "healingExhaustionCost")
					.defineInRange("healingExhaustionCost", () -> 1.0, 0.0, 10.0);

			wakeupHealingAmount = builder.comment("wakeupHealingAmount")
					.translation(Main.MODID + ".config." + "wakeupHealingAmount")
					.defineInRange("wakeupHealingAmount", () -> 4.0, 0.0, 10.0);
			
			extraExhaustionWhenHurt = builder.comment("extraExhaustionWhenHurt")
					.translation(Main.MODID + ".config." + "extraExhaustionWhenHurt")
					.defineInRange("extraExhaustionWhenHurt", () -> 0.0125, 0.0, 1.0);
					
			minimumStarvationHealth = builder.comment("minimum hit points for peaceful mode starvation.")
					.translation(Main.MODID + ".config." + "minimumStarvationHealth")
					.defineInRange("minimumStarvationHealth", () -> 0, 0 , 20);

			peacefulHunger = builder
					.comment("Can the player get hungry and maybe even starve to death in peaceful mode.")
					.translation(Main.MODID + ".config." + "peacefulHunger")
					.define("peacefulHunger", true);
			
			builder.pop();
		}
	}
	
    // ----------------------------------------
    // Save config method
    // ----------------------------------------
    public static void saveConfig() {
        COMMON_SPEC.save(); // writes all current values in COMMON to disk
        if (debugLevel > 0) {
            System.out.println("HarderNaturalHealing config saved!");
        }
    }
	
	public static int debugLevel;
	private static double healingPerSecond;
	private static double maxBonusHitPointTotems;
	private static int attackHealingDelayTicks;
	
	private static int healthAfterDeath;
	private static int hungerAfterDeath;
	private static double minimumFoodHealingLevel;
	private static double healingExhaustionCost;
	private static double wakeupHealingAmount;
	private static int minimumStarvationHealth;
	private static boolean peacefulHunger;
	private static double extraExhaustionWhenHurt;

	public static void bakeConfig() {

		debugLevel = COMMON.debugLevel.get();
		attackHealingDelayTicks = COMMON.attackHealingDelayTicks.get();
		extraExhaustionWhenHurt = COMMON.extraExhaustionWhenHurt.get();
		maxBonusHitPointTotems = COMMON.maxBonusHitPointTotems.get();
		healingPerSecond = COMMON.healingPerSecond.get();
		minimumFoodHealingLevel = COMMON.minimumFoodHealingLevel.get();
		healingExhaustionCost = COMMON.healingExhaustionCost.get();
		wakeupHealingAmount = COMMON.wakeupHealingAmount.get();
		minimumStarvationHealth = COMMON.minimumStarvationHealth.get();
		peacefulHunger = COMMON.peacefulHunger.get();
		healthAfterDeath= COMMON.healthAfterDeath.get();
		hungerAfterDeath= COMMON.hungerAfterDeath.get();
		if (debugLevel > 0) {
			System.out.println("HarderNaturalHealing Debug: " + debugLevel);
		}
	}
	
	// Debugging methods
	public static int getDebugLevel() {
		return debugLevel;
	}
	
	public static void setDebugLevel(int debugLevel) {
		MyConfig.debugLevel = debugLevel;
	}

	public static boolean isDebug() {
		if (debugLevel > 0)
			return true;
		return false;
	}

	// Getters
	public static boolean isPeacefulHunger() {
		return peacefulHunger;
	}
	public static double getHealingPerSecond() {
		return healingPerSecond;
	}

	public static int getHealthAfterDeath() {
		return healthAfterDeath;
	}
	
	public static int getHungerAfterDeath() {
		return hungerAfterDeath;
	}
	
	public static double getMinimumFoodHealingLevel() {
		return minimumFoodHealingLevel;
	}

	public static double getHealingExhaustionCost() {
		return healingExhaustionCost;
	}

	public static double getWakeupHealingAmount() {
		return wakeupHealingAmount;
	}
	
	public static int getAttackHealingDelayTicks() {
		return attackHealingDelayTicks;
	}
	
	public static double getMaxBonusHitPointTotems() {
		return maxBonusHitPointTotems;
	}	
	
	public static double getExtraExhaustionWhenHurt() {
		return extraExhaustionWhenHurt;
	}

	public static int getMinimumStarvationHealth() {
		return minimumStarvationHealth;
	}

	
	
	// Setters
	public static void setHealingPerSecond(double healingPerSecond) {
	    MyConfig.healingPerSecond = healingPerSecond;
	    COMMON.healingPerSecond.set(healingPerSecond);
	}

	public static void setMaxBonusHitPointTotems(int maxBonusHitPointTotems) {
	    MyConfig.maxBonusHitPointTotems = maxBonusHitPointTotems;
	    COMMON.maxBonusHitPointTotems.set(maxBonusHitPointTotems);
	}

	public static void setAttackHealingDelayTicks(int attackHealingDelayTicks) {
	    MyConfig.attackHealingDelayTicks = attackHealingDelayTicks;
	    COMMON.attackHealingDelayTicks.set(attackHealingDelayTicks);
	}

	public static void setMinimumFoodHealingLevel(double minimumFoodHealingLevel) {
	    MyConfig.minimumFoodHealingLevel = minimumFoodHealingLevel;
	    COMMON.minimumFoodHealingLevel.set(minimumFoodHealingLevel);
	}

	public static void setHealingExhaustionCost(double healingExhaustionCost) {
	    MyConfig.healingExhaustionCost = healingExhaustionCost;
	    COMMON.healingExhaustionCost.set(healingExhaustionCost);
	}

	public static void setWakeupHealingAmount(double wakeupHealingAmount) {
	    MyConfig.wakeupHealingAmount = wakeupHealingAmount;
	    COMMON.wakeupHealingAmount.set(wakeupHealingAmount);
	}

	public static void setMinimumStarvationHealth(int minimumStarvationHealth) {
	    MyConfig.minimumStarvationHealth = minimumStarvationHealth;
	    COMMON.minimumStarvationHealth.set(minimumStarvationHealth);
	}

	public static void setPeacefulHunger(boolean peacefulHunger) {
	    MyConfig.peacefulHunger = peacefulHunger;
	    COMMON.peacefulHunger.set(peacefulHunger);
	}

	public static void setExtraExhaustionWhenHurt(double extraExhaustionWhenHurt) {
	    MyConfig.extraExhaustionWhenHurt = extraExhaustionWhenHurt;
	    COMMON.extraExhaustionWhenHurt.set(extraExhaustionWhenHurt);
	}

	public static void setHealthAfterDeath(int healthAfterDeath) {
	    MyConfig.healthAfterDeath = healthAfterDeath;
	    COMMON.healthAfterDeath.set(healthAfterDeath);
	}

	public static void setHungerAfterDeath(int hungerAfterDeath) {
	    MyConfig.hungerAfterDeath = hungerAfterDeath;
	    COMMON.hungerAfterDeath.set(hungerAfterDeath);
	}


}
