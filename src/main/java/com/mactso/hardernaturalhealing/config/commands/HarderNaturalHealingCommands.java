package com.mactso.hardernaturalhealing.config.commands;

import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mojang.brigadier.CommandDispatcher;
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
				.then(Commands.literal("showSettings").executes(ctx -> {
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
		.then(Commands.literal("newSetting").then(
				Commands.argument("newSetting", IntegerArgumentType.integer(1,5)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setNewSetting(p, IntegerArgumentType.getInteger(ctx, "newSetting"));
			}
			)
			)
			)
		.then(Commands.literal("peacefulHunger").then(
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
	
	public static int setPeacefulHunger (boolean newValue) {
		MyConfig.setPeacefulHunger(newValue);
		MyConfig.pushPeacefulHunger();
		return 1;
	}	
}
