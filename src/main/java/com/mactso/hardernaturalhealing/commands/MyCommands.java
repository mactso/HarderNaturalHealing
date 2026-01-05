package com.mactso.hardernaturalhealing.commands;

import com.mactso.hardernaturalhealing.Main;
import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.utility.MyUtilities;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class MyCommands {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(Commands.literal(Main.MODID).requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.literal("debugLevel")
						.then(Commands.argument("debugLevel", IntegerArgumentType.integer(0, 2)).executes(ctx -> {
							return setDebugLevel(IntegerArgumentType.getInteger(ctx, "debugLevel"));
						})))
				.then(Commands.literal("0-ShowCurrentSettings").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					showPresets(p);
					return 1;
				})).then(Commands.literal("1-SetPreset-(1to5)")
						.then(Commands.argument("newPreset", IntegerArgumentType.integer(1, 5)).executes(ctx -> {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							return setNewSetting(p, IntegerArgumentType.getInteger(ctx, "newPreset"));
						})))
				.then(Commands.literal("2-PeacefulHunger").then(

						Commands.literal("true").executes(ctx -> {
							// ServerPlayer p = ctx.getSource().getPlayerOrException();
							return MyConfig.setPeacefulHunger(true);
						})).then(Commands.literal("false").executes(ctx -> {
							// ServerPlayer p = ctx.getSource().getPlayerOrException();
							return MyConfig.setPeacefulHunger(false);
						})))
				.then(Commands.literal("2-MinimumStarvationHealth").then(Commands
						.argument("minimumStarvationHealth", IntegerArgumentType.integer(0, 20)).executes(ctx -> {
							return setMinimumStarvationHealth(
									IntegerArgumentType.getInteger(ctx, "minimumStarvationHealth"));
						})))
				.then(Commands.literal("3-HealthAfterDeath").then(
						Commands.argument("healthafterdeath", IntegerArgumentType.integer(0, 20)).executes(ctx -> {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							return setHealthAfterDeath(p, IntegerArgumentType.getInteger(ctx, "healthafterdeath"));
						})))
				.then(Commands.literal("3-HungerAfterDeath").then(
						Commands.argument("hungerafterdeath", IntegerArgumentType.integer(0, 20)).executes(ctx -> {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							return setHungerAfterDeath(p, IntegerArgumentType.getInteger(ctx, "hungerafterdeath"));
						})))
				.then(Commands.literal("4-healingPerSecond").then(Commands
						.argument("healingPerSecond", DoubleArgumentType.doubleArg(0.25, 10.0)).executes(ctx -> {
							double d = DoubleArgumentType.getDouble(ctx, "healingPerSecond");
							return setHealingPerSecond(d);
						})))
				.then(Commands.literal("4-combatHealingDelayTicks").then(Commands
						.argument("combatHealingDelayTicks", IntegerArgumentType.integer(0, 3000)).executes(ctx -> {
							return setCombatHealingDelayTicks(
									IntegerArgumentType.getInteger(ctx, "combatHealingDelayTicks"));
						})))
				.then(Commands.literal("4-minimumFoodHealingLevel").then(Commands
						.argument("minimumFoodHealingLevel", DoubleArgumentType.doubleArg(0.0, 22.0)).executes(ctx -> {
							return setMinimumFoodHealingLevel(
									DoubleArgumentType.getDouble(ctx, "minimumFoodHealingLevel"));
						})))
				.then(Commands.literal("4-wakeupHealingAmount").then(Commands
						.argument("wakeupHealingAmount", DoubleArgumentType.doubleArg(0.0, 10.0)).executes(ctx -> {
							return setWakeupHealingAmount(DoubleArgumentType.getDouble(ctx, "wakeupHealingAmount"));
						})))
				.then(Commands.literal("5-healingExhaustionCost").then(Commands
						.argument("healingExhaustionCost", DoubleArgumentType.doubleArg(0.0, 10.0)).executes(ctx -> {
							return setHealingExhaustionCost(DoubleArgumentType.getDouble(ctx, "healingExhaustionCost"));
						})))
				.then(Commands.literal("5-extraExhaustionWhenHurt").then(Commands
						.argument("extraExhaustionWhenHurt", DoubleArgumentType.doubleArg(0.0, 1.0)).executes(ctx -> {
							return setExtraExhaustionWhenHurt(
									DoubleArgumentType.getDouble(ctx, "extraExhaustionWhenHurt"));
						}))) // end of commands
		); // end of dispatch.register()

	}

	private static void showPreset(ServerPlayer p, String preset) {

		String chatMessage = "\nChanged to Preset: " + preset;
		MyUtilities.sendChat(p, chatMessage, ChatFormatting.YELLOW);

	}

	private static void showPresets(ServerPlayer p) {
		String chatMessage = "Current Settings";
		MyUtilities.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);
		chatMessage = " Debug Level.: "
				+ MyConfig.getDebugLevel() + "\n Health when respawning .: "
				+ MyConfig.getHealthAfterDeath() + "\n Use Peaceful Difficulty Hunger.: "
				+ MyConfig.isPeacefulHunger() + "\n New Hunger Level when Respawning .: "
				+ MyConfig.getHungerAfterDeath() + "\n Minimum Health from Starvation.: "
				+ MyConfig.getMinimumStarvationHealth()
				+ "\n Healing Per Second.: "
				+ MyConfig.getHealingPerSecond() + "\n Ticks Combat delays Healing.: "
				+ MyConfig.getAttackHealingDelayTicks()
				+ "\n Minimum Food Healing Level.: "
				+ MyConfig.getMinimumFoodHealingLevel() + "\n Healing Food Exhaustion Cost.: "
				+ MyConfig.getHealingExhaustionCost()
				+ "\n Sleep Healing Amount.: "
				+ MyConfig.getWakeupHealingAmount() + "\n Extra Food Exhaustion When Hurt.: "
				+ MyConfig.getExtraExhaustionWhenHurt();

		MyUtilities.sendChat(p, chatMessage, ChatFormatting.GREEN);
	}

	public static int setNewSetting(ServerPlayer p, int newSetting) {
		double[][] presets = new double[][] { { 0.75, 40.0, 12.0, 1.0, 6.0, 0.0 }, // easy
				{ 0.5, 100.0, 16.0, 2.0, 0.0, 0.0125 }, // normal
				{ 0.25, 200.0, 16.0, 2.0, 2.0, 0.03 }, // harder
				{ 0.016, 300.0, 16.0, 2.0, 0.5, 0.0 }, // superhard
				{ 0.0, 0.0, 16.0, 2.0, 6.0, 0.0 } // wakeup only
		};

		String[] presetNames = new String[] { "Easy Healing", "Original Healing", "Harder Healing", "Superhard Healing",
				"Healing From Sleep Only" };

		int index = newSetting - 1; // convert 1-based -> 0-based
		double[] row = presets[index];

		// set the config values
		MyConfig.setHealingPerSecond(row[0]);
		MyConfig.pushHealingPerSecond();
		MyConfig.setAttackHealingDelayTicks((int) row[1]);
		MyConfig.pushAttackHealingDelayTicks();
		MyConfig.setMinimumFoodHealingLevel(row[2]);
		MyConfig.pushMinimumFoodHealingLevel();
		MyConfig.setHealingExhaustionCost(row[3]);
		MyConfig.pushHealingExhaustionCost();
		MyConfig.setWakeupHealingAmount(row[4]);
		MyConfig.pushWakeupHealingAmount();
		MyConfig.setExtraExhaustionWhenHurt(row[5]);
		MyConfig.pushExtraExhaustionWhenHurt();

		showPreset(p, presetNames[index]);
		showPresets(p);
		showPreset(p, presetNames[index]);
		return 1;

	}

	public static int setHealingPerSecond(double newValue) {
		MyConfig.setHealingPerSecond(newValue);
		MyConfig.pushHealingPerSecond();
		return 1;
	}

	public static int setCombatHealingDelayTicks(double newValue) {
		MyConfig.setAttackHealingDelayTicks((int) newValue);
		MyConfig.pushAttackHealingDelayTicks();
		return 1;
	}

	public static int setMinimumFoodHealingLevel(double newValue) {
		MyConfig.setMinimumFoodHealingLevel(newValue);
		MyConfig.pushMinimumFoodHealingLevel();
		return 1;
	}

	public static int setHealingExhaustionCost(double newValue) {
		MyConfig.setHealingExhaustionCost(newValue);
		MyConfig.pushHealingExhaustionCost();
		return 1;
	}

	public static int setWakeupHealingAmount(double newValue) {
		MyConfig.setWakeupHealingAmount(newValue);
		MyConfig.pushWakeupHealingAmount();
		return 1;
	}

	public static int setExtraExhaustionWhenHurt(double d) {
		MyConfig.setExtraExhaustionWhenHurt(d);
		MyConfig.pushExtraExhaustionWhenHurt();
		return 1;
	}

	public static int setPeacefulHunger(boolean newValue) {
		MyConfig.setPeacefulHunger(newValue);
		MyConfig.pushPeacefulHunger();
		return 1;
	}

	public static int setMinimumStarvationHealth(int newValue) {
		MyConfig.setMinimumStarvationHealth(newValue);
		MyConfig.pushMinimumStarvationHealth();
		return 1;
	}

	public static int setHungerAfterDeath(ServerPlayer sp, int newValue) {
		MyConfig.setHungerAfterDeath(newValue);
		showPresets(sp);
		return 1;
	}

	public static int setHealthAfterDeath(ServerPlayer sp, int newValue) {
		MyConfig.setHealthAfterDeath(newValue);
		showPresets(sp);
		return 1;
	}

	public static int setDebugLevel(int newValue) {
		MyConfig.setDebugLevel(newValue);
		MyConfig.pushDebugValue();
		return 1;
	}

}
