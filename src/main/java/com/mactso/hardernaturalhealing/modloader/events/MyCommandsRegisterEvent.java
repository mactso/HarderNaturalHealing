package com.mactso.hardernaturalhealing.modloader.events;


import com.mactso.hardernaturalhealing.common.commands.MyCommands;
import com.mactso.hardernaturalhealing.common.utility.MyUtilities;
import com.mactso.hardernaturalhealing.modloader.main.Main;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class MyCommandsRegisterEvent {
    @SubscribeEvent
    public void onCommandsRegistry(RegisterCommandsEvent event) {
        MyUtilities.debugMsg(0, Main.MODID + ": Registering Commands");
        MyCommands.register(event.getDispatcher());
    }
}



