package com.mactso.hardernaturalhealing.common.commands;

import java.util.List;
import java.util.Map;
import java.util.Locale;

import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.config.MyConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class MyCommands {
	String subcommand = "";
	String value = "";

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(Commands.literal("hardernaturalhealing") // root
				.requires(source -> source.hasPermission(2)) // requires

				.then(Commands.literal("help") // help
						.executes(ctx -> { // executes
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							showHelp(p);
							return 1;
						}) // executes
				) // then(help)
					// 0 - show
				.then(Commands.literal("showSettings") // show
						.executes(ctx -> { // executes
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							showSettings(p);
							return 1;
						}) // executes
				) // then(show)

				// 1 - config
				.then(Commands.literal("config") // config
						.then(Commands.literal("preset") // literal(preset)
								.then(Commands.argument("preset", StringArgumentType.word()) // argument(preset)
										.suggests((ctx, builder) -> { // SuggestionProvider
											List<String> options = List.of("easy", "normal",
													"harder", "superhard", "wakeup");
											for (String s : options) {
												builder.suggest(s); // adds each string as a suggestion
											}
											return builder.buildFuture(); // returns CompletableFuture<Suggestions>
										})// suggests
										.executes(ctx -> { // executes
											ServerPlayer p = ctx.getSource().getPlayerOrException();
											String presetArg = StringArgumentType.getString(ctx, "preset");
											return MyCommands.setPreset(p, presetArg);
										}) // executes
								) // then(argument)
						) // then(literal(preset))

						.then(Commands.literal("peacefulHunger") //
							    .then(Commands.literal("true") //
							        .executes(ctx -> {
							            ServerPlayer sp = ctx.getSource().getPlayerOrException();
							            return setPeacefulHunger(sp, true);
							        })
							    ) //
							    .then(Commands.literal("false") //
							        .executes(ctx -> {
							            ServerPlayer sp = ctx.getSource().getPlayerOrException();
							            return setPeacefulHunger(sp, false);
							        })
							    ) //
							) // then(peacefulHunger)

						.then(Commands.literal("minimumStarvationHealth") // minimumStarvationHealth
								.then(Commands.argument("value", IntegerArgumentType.integer(0, 20)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setMinimumStarvationHealth(sp,
													IntegerArgumentType.getInteger(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(minimumStarvationHealth)

						.then(Commands.literal("healthAfterDeath") // healthAfterDeath
								.then(Commands.argument("value", IntegerArgumentType.integer(0, 20)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setHealthAfterDeath(sp,
													IntegerArgumentType.getInteger(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(healthAfterDeath)

						.then(Commands.literal("hungerAfterDeath") // hungerAfterDeath
								.then(Commands.argument("value", IntegerArgumentType.integer(0, 20)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setHungerAfterDeath(sp,
													IntegerArgumentType.getInteger(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(hungerAfterDeath)
				) // then(config)

				// 2 - healing
				.then(Commands.literal("healing") // healing
						.then(Commands.literal("healingPerSecond") // healingPerSecond
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0.01, 10.0)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setHealingPerSecond(sp, DoubleArgumentType.getDouble(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(healingPerSecond)

						.then(Commands.literal("attackHealingDelayTicks") // attackHealingDelayTicks
								.then(Commands.argument("value", IntegerArgumentType.integer(0, 3000)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setAttackHealingDelayTicks(sp,
													IntegerArgumentType.getInteger(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(attackHealingDelayTicks)

						.then(Commands.literal("minimumFoodHealingLevel") // minimumFoodHealingLevel
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 22.0)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setMinimumFoodHealingLevel(sp,
													DoubleArgumentType.getDouble(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(minimumFoodHealingLevel)

						.then(Commands.literal("healingExhaustionCost") // healingExhaustionCost
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 10.0)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setHealingExhaustionCost(sp,
													DoubleArgumentType.getDouble(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(healingExhaustionCost)

						.then(Commands.literal("wakeupHealingAmount") // wakeupHealingAmount
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 10.0)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setWakeupHealingAmount(sp,
													DoubleArgumentType.getDouble(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(wakeupHealingAmount)

						.then(Commands.literal("extraExhaustionWhenHurt") // extraExhaustionWhenHurt
								.then(Commands.argument("value", DoubleArgumentType.doubleArg(0.0, 1.0)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setExtraExhaustionWhenHurt(sp,
													DoubleArgumentType.getDouble(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(extraExhaustionWhenHurt)
				) // then(healing)

				// 3 - debug
				.then(Commands.literal("debug") // debug
						.then(Commands.literal("level") // level
								.then(Commands.argument("value", IntegerArgumentType.integer(0, 2)) // argument
										.executes(ctx -> { // executes
											ServerPlayer sp = ctx.getSource().getPlayerOrException();
											return setDebugLevel(sp,IntegerArgumentType.getInteger(ctx, "value"));
										}) // executes
								) // then(argument)
						) // then(level)
				) // then(debug)
		); // register
	}

	private static void showHelp(ServerPlayer p) {
		MyUtilities.sendBoldChat(p, "Harder Natural Healing Commands", ChatFormatting.DARK_GREEN);

		MyUtilities.sendChat(p, "/hardernaturalhealing showSetting", ChatFormatting.GREEN);
		MyUtilities.sendChat(p, "  Shows current configuration", ChatFormatting.GRAY);

		MyUtilities.sendChat(p, "/hardernaturalhealing config preset", ChatFormatting.GREEN);
		MyUtilities.sendChat(p, "  Applies a preset difficulty profile (easy, hard, etc.)", ChatFormatting.GRAY);

		MyUtilities.sendChat(p, "/hardernaturalhealing config <value>", ChatFormatting.GREEN);
		MyUtilities.sendChat(p, "  Sets other values that are not healing values.", ChatFormatting.GRAY);

		MyUtilities.sendChat(p, "/hardernaturalhealing healing <value>", ChatFormatting.GREEN);
		MyUtilities.sendChat(p, "  Sets values that are healing values", ChatFormatting.GRAY);

		MyUtilities.sendChat(p, "/hardernaturalhealing debug level <0-2>", ChatFormatting.GREEN);
		MyUtilities.sendChat(p, "  Sets debug verbosity. 0 is off, 2 is maximum.", ChatFormatting.GRAY);
	}

	private static void showSettings(ServerPlayer p) {
		String chatMessage = "Current Settings";
		MyUtilities.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);
		chatMessage = " Debug Level...................................: " + MyConfig.getDebugLevel()
				+ "\n health after death .................: " + MyConfig.getHealthAfterDeath()
				+ "\n peaceful hunger........................: " + MyConfig.isPeacefulHunger()
				+ "\n hunger after death ..............: " + MyConfig.getHungerAfterDeath()
				+ "\n minimum starvation health...: " + MyConfig.getMinimumStarvationHealth()
				+ "\n healingPerSecond.....................: " + MyConfig.getHealingPerSecond()
				+ "\n attackHealingDelayTicks.......: " + MyConfig.getAttackHealingDelayTicks()
				+ "\n minimumFoodHealingLevel......: " + MyConfig.getMinimumFoodHealingLevel()
				+ "\n healingExhaustionCost..........: " + MyConfig.getHealingExhaustionCost()
				+ "\n wakeupHealingAmount..............: " + MyConfig.getWakeupHealingAmount()
				+ "\n extraExhaustionWhenHurt..: " + MyConfig.getExtraExhaustionWhenHurt();

		MyUtilities.sendChat(p, chatMessage, ChatFormatting.GREEN);
	}

	public static int setPreset(ServerPlayer player, String presetInput) {

		// Normalize input: trim whitespace and lowercase in a locale-independent way
		presetInput = presetInput.trim().toLowerCase(Locale.ROOT);

		String[] presetNames = new String[] { "easy", "normal", "harder", "superhard", "wakeup" };
		// Map string names to numeric indexes
		Map<String, Integer> nameToIndex = Map.of("easy", 0, "normal", 1, "harder", 2, "superhard", 3, "wakeup", 4);

		// Presets table: healingPerSecond, attackDelay, minFood, exhaustionCost,
		// wakeupHealing, extraExhaustion
		double[][] presets = { { 0.75, 40.0, 12.0, 1.0, 6.0, 0.0 }, // easy
				{ 0.5, 100.0, 16.0, 2.0, 0.0, 0.0125 }, // normal
				{ 0.25, 200.0, 16.0, 2.0, 2.0, 0.03 }, // harder
				{ 0.016, 300.0, 16.0, 2.0, 0.5, 0.0 }, // superhard
				{ 0.0, 0.0, 16.0, 2.0, 6.0, 0.0 } // wakeup
		};

		// code
		int presetIndex = -1;

		// Try numeric first
		try {
			int num = Integer.parseInt(presetInput);
			if (num >= 1 && num <= 5) {
				presetIndex = num - 1;
			}
		} catch (NumberFormatException ignored) {
			// Not numeric, try word
		}

		// If not numeric, try word
		if (presetIndex == -1) {
			Integer idx = nameToIndex.get(presetInput.toLowerCase(Locale.ROOT));
			if (idx != null)
				presetIndex = idx;
		}

		// Invalid input
		if (presetIndex == -1) {
			MyUtilities.sendChat(player, "Invalid preset: " + presetInput, ChatFormatting.RED);
			return 0; // failure
		}

		String chosenPreset = presetNames[presetIndex];
		MyConfig.setHealingPerSecond(presets[presetIndex][0]);
		MyConfig.setAttackHealingDelayTicks((int) presets[presetIndex][1]);
		MyConfig.setMinimumFoodHealingLevel(presets[presetIndex][2]);
		MyConfig.setHealingExhaustionCost(presets[presetIndex][3]);
		MyConfig.setWakeupHealingAmount(presets[presetIndex][4]);
		MyConfig.setExtraExhaustionWhenHurt(presets[presetIndex][5]);
		MyConfig.saveConfig();

		MyUtilities.sendChat(player, "\nPreset set to " + chosenPreset, ChatFormatting.GREEN);
		showSettings(player);
		return 1; // success
	}

	public static int setHealingPerSecond(ServerPlayer player, double newValue) {
		MyConfig.setHealingPerSecond(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Healing per second was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setAttackHealingDelayTicks(ServerPlayer player, int newValue) {
		MyConfig.setAttackHealingDelayTicks(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Attack healing delay ticks was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setMinimumFoodHealingLevel(ServerPlayer player, double newValue) {
		MyConfig.setMinimumFoodHealingLevel(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Minimum food healing level was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setHealingExhaustionCost(ServerPlayer player, double newValue) {
		MyConfig.setHealingExhaustionCost(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Healing exhaustion cost was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setWakeupHealingAmount(ServerPlayer player, double newValue) {
		MyConfig.setWakeupHealingAmount(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Wakeup healing amount was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setExtraExhaustionWhenHurt(ServerPlayer player, double newValue) {
		MyConfig.setExtraExhaustionWhenHurt(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Extra exhaustion when hurt was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setPeacefulHunger(ServerPlayer player, boolean newValue) {
		MyConfig.setPeacefulHunger(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Peaceful hunger was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setMinimumStarvationHealth(ServerPlayer player, int newValue) {
		MyConfig.setMinimumStarvationHealth(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Minimum starvation health was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setHungerAfterDeath(ServerPlayer player, int newValue) {
		MyConfig.setHungerAfterDeath(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Hunger after death was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setHealthAfterDeath(ServerPlayer player, int newValue) {
		MyConfig.setHealthAfterDeath(newValue);
		MyConfig.saveConfig();
		MyUtilities.sendChat(player, "Health after death was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

	public static int setDebugLevel(ServerPlayer player, int newValue) {
		MyConfig.setDebugLevel(newValue);
		// Do not save permanently to disk
		MyUtilities.sendChat(player, "Debug level was set to " + newValue, ChatFormatting.GREEN);
		return 1;
	}

}
