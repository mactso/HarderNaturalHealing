package com.mactso.hardernaturalhealing;


import com.mactso.hardernaturalhealing.config.MyConfig;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("hardernaturalhealing")
public class Main {

	    public static final String MODID = "hardernaturalhealing"; 
	    
	    public Main()
	    {
			System.out.println("hardernaturalhealing: Registering Mod.");
			FMLJavaModLoadingContext.get().getModEventBus().register(this);
	        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,MyConfig.COMMON_SPEC );
	    }


		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
				System.out.println("hardernaturalhealing: Registering Handler");
				MinecraftForge.EVENT_BUS.register(new playerTickHandler());
		}   
		
		@SubscribeEvent 
		public void preInit (final FMLServerStartingEvent event) {
				System.out.println("hardernaturalhealing: Turn normal healing Off");
				MinecraftForge.EVENT_BUS.register(new turnNormalHealingOff());
	} 

}
