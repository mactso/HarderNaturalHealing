package com.mactso.hardernaturalhealing.config.commands;

import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextColor;
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
		.then(Commands.literal("1-NewSetting").then(
				Commands.argument("newSetting", IntegerArgumentType.integer(1,5)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setNewSetting(p, IntegerArgumentType.getInteger(ctx, "newSetting"));
			}
			)
			)
			)
		.then(Commands.literal("1-PeacefulHunger").then(
				Commands.literal("true").executes(ctx -> {
					return MyConfig.setPeacefulHunger(true);
				}
				)).then (
				Commands.literal("false").executes(ctx -> {
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
		.then(Commands.literal("2-healingPerSecond").then(
				Commands.argument("healingPerSecond", DoubleArgumentType.doubleArg(0.25,10.0)).executes(ctx -> {
					return setHealingPerSecond(DoubleArgumentType.getDouble(ctx, "healingPerSecond"));
			}
			)
			)
			)
		.then(Commands.literal("2-attackHealingDelayTicks").then(
				Commands.argument("attackHealingDelayTicks", IntegerArgumentType.integer(0,3000)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setAttackHealingDelayTicks(IntegerArgumentType.getInteger(ctx, "attackHealingDelayTicks"));
			}
			)
			)
			)		
		.then(Commands.literal("2-getMinimumFoodHealingLevel").then(
				Commands.argument("getMinimumFoodHealingLevel", DoubleArgumentType.doubleArg(0.0,22.0)).executes(ctx -> {
					return setMinimumFoodHealingLevel(DoubleArgumentType.getDouble(ctx, "getMinimumFoodHealingLevel"));
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
		);
		

	}



	private static void showNewSetup (ServerPlayer p, int i) {
		String[] setupNames = new String[] {"easy", "normal", "harder", "superhard", "wakeup"};
		String chatMessage = 
				"Setup: " + setupNames [i] ;
        MyConfig.sendChat (p,chatMessage,TextColor.fromLegacyFormat(ChatFormatting.GREEN));

	}
	
	private static void showSettings(ServerPlayer p) {
		String chatMessage = 
				"Current Settings";
		MyConfig.sendBoldChat (p,chatMessage, TextColor.fromLegacyFormat(ChatFormatting.DARK_GREEN));
		chatMessage = 
				" peaceful hunger.............:" + MyConfig.isPeacefulHunger() +
			    "\n minimum starvation health.: " + MyConfig.getMinimumStarvationHealth() +
			    "\n healingPerSecond..........:"+ MyConfig.getHealingPerSecond() +
				"\n attackHealingDelayTicks...:" + MyConfig.getAttackHealingDelayTicks() +
				"\n minimumFoodHealingLevel...:" + MyConfig.getMinimumFoodHealingLevel() +
				"\n healingExhaustionCost.....:" + MyConfig.getHealingExhaustionCost() +
				"\n wakeupHealingAmount.......:" + MyConfig.getWakeupHealingAmount() +
				"\n extraExhaustionWhenHurt...:"+MyConfig.getExtraExhaustionWhenHurt();
				
		        MyConfig.sendChat (p,chatMessage,TextColor.fromLegacyFormat(ChatFormatting.GREEN));
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

	private static int setMinimumStarvationHealth( int newValue) {
		MyConfig.setMinimumStarvationHealth(newValue);
		MyConfig.pushMinimumStarvationHealth();
		return 0;
	}

}
