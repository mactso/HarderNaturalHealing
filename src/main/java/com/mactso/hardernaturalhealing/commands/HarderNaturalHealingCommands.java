package com.mactso.hardernaturalhealing.commands;

import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.utility.Utility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class HarderNaturalHealingCommands {
	String subcommand = "";
	String value = "";
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{

		dispatcher.register(Commands.literal("hardernaturalhealing")
				.then(Commands.literal("0-ShowSettings").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					showSettings(p);
							return 1;
					}
					)
					)				
		.requires((source) -> 
			{
				return source.hasPermission(2);
			}
		)
		.then(Commands.literal("1-NewSetting-(1to5)").then(
				Commands.argument("newSetting", IntegerArgumentType.integer(1,5)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setNewSetting(p, IntegerArgumentType.getInteger(ctx, "newSetting"));
			}
			)
			)
			)
		.then(Commands.literal("1-PeacefulHunger").then(

				Commands.literal("true").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return MyConfig.setPeacefulHunger(true);
				}
				)).then (
				Commands.literal("false").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return MyConfig.setPeacefulHunger(false);
			}
			)
			)
			)
		.then(Commands.literal("1-MinimumStarvationHealth").then(
				Commands.argument("minimumStarvationHealth", IntegerArgumentType.integer(0,20)).executes(ctx -> {
					return setMinimumStarvationHealth(IntegerArgumentType.getInteger(ctx, "minimumStarvationHealth"));
			}
			)
			)
			)
		.then(Commands.literal("1-HealthAfterDeath").then(
				Commands.argument("healthafterdeath", IntegerArgumentType.integer(0,20)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setHealthAfterDeath(p, IntegerArgumentType.getInteger(ctx, "healthafterdeath"));
			}
			)
			)
			)
		.then(Commands.literal("1-HungerAfterDeath").then(
				Commands.argument("hungerafterdeath", IntegerArgumentType.integer(0,20)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setHungerAfterDeath(p, IntegerArgumentType.getInteger(ctx, "hungerafterdeath"));
			}
			)
			)
			)
		.then(Commands.literal("2-healingPerSecond").then(
				Commands.argument("healingPerSecond", DoubleArgumentType.doubleArg(0.25,10.0)).executes(ctx -> {
					return setHealingPerSecond(DoubleArgumentType.getDouble(ctx, "healingPerSecond 0.25 to 10.0"));
			}
			)
			)
			)
		.then(Commands.literal("2-attackHealingDelayTicks").then(
				Commands.argument("attackHealingDelayTicks", IntegerArgumentType.integer(0,3000)).executes(ctx -> {
					return setAttackHealingDelayTicks(IntegerArgumentType.getInteger(ctx, "attackHealingDelayTicks"));
			}
			)
			)
			)		
		.then(Commands.literal("2-minimumFoodHealingLevel").then(
				Commands.argument("minimumFoodHealingLevel", DoubleArgumentType.doubleArg(0.0,22.0)).executes(ctx -> {
					return setMinimumFoodHealingLevel(DoubleArgumentType.getDouble(ctx, "minimumFoodHealingLevel"));
			}
			)
			)
			)
		.then(Commands.literal("2-healingExhaustionCost").then(
				Commands.argument("healingExhaustionCost", DoubleArgumentType.doubleArg(0.0,10.0)).executes(ctx -> {
					return setHealingExhaustionCost(DoubleArgumentType.getDouble(ctx, "healingExhaustionCost"));
			}
			)
			)
			)	
		.then(Commands.literal("2-wakeupHealingAmount").then(
				Commands.argument("wakeupHealingAmount", DoubleArgumentType.doubleArg(0.0,10.0)).executes(ctx -> {
					return setWakeupHealingAmount(DoubleArgumentType.getDouble(ctx, "wakeupHealingAmount"));
			}
			)
			)
			)
		.then(Commands.literal("2-extraExhaustionWhenHurt").then(
				Commands.argument("extraExhaustionWhenHurt", DoubleArgumentType.doubleArg(0.0,1.0)).executes(ctx -> {
					return setExtraExhaustionWhenHurt(DoubleArgumentType.getDouble(ctx, "extraExhaustionWhenHurt"));
			}
			)
			)
			)
		.then(Commands.literal("3-debugLevel").then(
				Commands.argument("debugLevel", IntegerArgumentType.integer(0,2)).executes(ctx -> {
					return setDebugLevel(IntegerArgumentType.getInteger(ctx, "debugLevel"));
			}
			)
			)
			)		
		);
		

	}



	private static void showNewSetup (ServerPlayer p, int i) {
		String[] setupNames = new String[] {"easy healing", "original healing", "harder healing", "superhard healing", "Healing from Sleep Only"};
		String chatMessage = 
				"\nChanged to Setting: " + setupNames [i] ;
        Utility.sendChat (p,chatMessage,ChatFormatting.YELLOW);

	}
	
	private static void showSettings(ServerPlayer p) {
		String chatMessage = 
				"Current Settings";
		Utility.sendBoldChat (p,chatMessage, ChatFormatting.DARK_GREEN);
		chatMessage = 
				" Debug Level....................................................................: " + MyConfig.getDebugLevel() +
				"\n Health when respawning .....................................: " + MyConfig.getHealthAfterDeath() +
				"\n Use Peaceful Difficulty Hunger.....................: " + MyConfig.isPeacefulHunger() +
				"\n New Hunger Level when Respawning ........: " + MyConfig.getHungerAfterDeath() +
				"\n Minimum Health from Starvation.......................: " + MyConfig.getMinimumStarvationHealth() +
			    "\n Healing Per Second..................................................: "+ MyConfig.getHealingPerSecond() +
				"\n Seconds Healing Delayed By Attacking....: " + MyConfig.getAttackHealingDelaySeconds() +
				"\n Minimum Food Healing Level.................................: " + MyConfig.getMinimumFoodHealingLevel() +
				"\n Healing Food Exhaustion Cost.........................: " + MyConfig.getHealingExhaustionCost() +
				"\n Sleep Healing Amount..............................................: " + MyConfig.getWakeupHealingAmount() +
				"\n Extra Food Exhaustion When Hurt..............: "+MyConfig.getExtraExhaustionWhenHurt();
				
		        Utility.sendChat (p,chatMessage,ChatFormatting.GREEN);
	}

	public static int setNewSetting(ServerPlayer p, int s) {
		double [][]settings = new double [][] {
			{0.75, 40.0,12.0,1.0,6.0,0.0}, // easy
			{ 0.5,100.0,16.0,2.0,0.0,0.0125}, // normal
			{0.25,200.0,16.0,2.0,2.0,0.03}, // harder
			{0.016,300.0,16.0,2.0,0.5,0.0}, // superhard
			{0.0,0.0,16.0,2.0,6.0,0.0} // wakeup only
			};
	
		MyConfig.setHealingPerSecond(settings[s-1][0]);
		MyConfig.pushHealingPerSecond();
		MyConfig.setAttackHealingDelayTicks((int)settings[s-1][1]); 
		MyConfig.pushAttackHealingDelayTicks(); 
		MyConfig.setMinimumFoodHealingLevel(settings[s-1][2]);
		MyConfig.pushMinimumFoodHealingLevel(); 
		MyConfig.setHealingExhaustionCost(settings[s-1][3]); 
		MyConfig.pushHealingExhaustionCost(); 
		MyConfig.setWakeupHealingAmount(settings[s-1][4]);
		MyConfig.pushWakeupHealingAmount();
		MyConfig.setExtraExhaustionWhenHurt(settings[s-1][5]);
		MyConfig.pushExtraExhaustionWhenHurt();
		showNewSetup(p,s-1);
		showSettings(p);
		return 1;
		
	}	
	
	public static int setHealingPerSecond (double newValue) {
		MyConfig.setHealingPerSecond(newValue);
		MyConfig.pushHealingPerSecond();
		return 1;
	}

	public static int setAttackHealingDelayTicks (double newValue) {
	MyConfig.setAttackHealingDelayTicks((int)newValue); 
	MyConfig.pushAttackHealingDelayTicks(); 
	return 1;
	}

	public static int setMinimumFoodHealingLevel (double newValue) {
	MyConfig.setMinimumFoodHealingLevel(newValue);
	MyConfig.pushMinimumFoodHealingLevel(); 
	return 1;
	}

	public static int setHealingExhaustionCost (double newValue) {
	MyConfig.setHealingExhaustionCost(newValue); 
	MyConfig.pushHealingExhaustionCost(); 
	return 1;
	}

	public static int setWakeupHealingAmount (double newValue) {
	MyConfig.setWakeupHealingAmount(newValue);
	MyConfig.pushWakeupHealingAmount();
	return 1;
	}

	public static int setExtraExhaustionWhenHurt (double d) {
	MyConfig.setExtraExhaustionWhenHurt(d);
	MyConfig.pushExtraExhaustionWhenHurt();
	return 1;
	}

	public static int setPeacefulHunger (boolean newValue) {
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
		showSettings(sp);
		return 1;
	}
	
	public static int setHealthAfterDeath(ServerPlayer sp, int newValue) {
		MyConfig.setHealthAfterDeath(newValue);
		showSettings(sp);
		return 1;
	}	
	public static int setDebugLevel( int newValue) {
		MyConfig.setDebugLevel(newValue);
		MyConfig.pushDebugValue();
		return 1;
	}

	
}
