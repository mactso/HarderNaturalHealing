package com.mactso.hardernaturalhealing.config;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.hardernaturalhealing.Main;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setDebugLevel(int debugLevel) {
		MyConfig.debugLevel = debugLevel;
	}

	public static double getHealingPerSecond() {
		return healingPerSecond;
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

	public static void debugMsg (int level, String dMsg) {
		if (debugLevel > level-1) {
			System.out.println("L"+level + ":" + dMsg);
		}
	}

	public static void debugMsg (int level, BlockPos pos, String dMsg) {
		if (debugLevel > level-1) {
			System.out.println("L"+level+" ("+pos.getX()+","+pos.getY()+","+pos.getZ()+"): " + dMsg);
		}
	}

	public static int debugLevel;
	private static double healingPerSecond;
	private static double maxBonusHitPointTotems;
	private static int attackHealingDelayTicks;
	private static double minimumFoodHealingLevel;
	private static double healingExhaustionCost;
	private static double wakeupHealingAmount;
	private static double minimumStarvationHealth;
	private static boolean peacefulHunger;
	private static double extraExhaustionWhenHurt;
	
	public static double getExtraExhaustionWhenHurt() {
		return extraExhaustionWhenHurt;
	}

	public static double getMinimumStarvationHealth() {
		return minimumStarvationHealth;
	}

	public static boolean isPeacefulHunger() {
		return peacefulHunger;
	}

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC) {
			bakeConfig();
		}
	}

	public static void pushDebugValue() {
		if (debugLevel > 0) {
			System.out.println("hardernaturalhealing debugLevel:" + MyConfig.debugLevel);
		}
		COMMON.debugLevel.set(MyConfig.debugLevel);
	}

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
		if (debugLevel > 0) {
			System.out.println("HarderNaturalHealing Debug: " + debugLevel);
		}
	}

	public static class Common {

		public final IntValue debugLevel;
		public final IntValue attackHealingDelayTicks;
		public final IntValue maxBonusHitPointTotems;
		public final DoubleValue extraExhaustionWhenHurt;
		public final DoubleValue healingPerSecond;
		public final DoubleValue minimumFoodHealingLevel;
		public final DoubleValue healingExhaustionCost;
		public final DoubleValue wakeupHealingAmount;
		public final IntValue minimumStarvationHealth;
		public final BooleanValue peacefulHunger;

		public Common(ForgeConfigSpec.Builder builder) {
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

	// support for any color chattext
	public static void sendChat(PlayerEntity p, String chatMessage, Color color) {
		StringTextComponent component = new StringTextComponent(chatMessage);
		component.getStyle().setColor(color);
		p.sendMessage(component, p.getUniqueID());
	}

	// support for any color, optionally bold text.
	public static void sendBoldChat(PlayerEntity p, String chatMessage, Color color) {
		StringTextComponent component = new StringTextComponent(chatMessage);

		component.getStyle().setBold(true);
		component.getStyle().setColor(color);

		p.sendMessage(component, p.getUniqueID());
	}
}
