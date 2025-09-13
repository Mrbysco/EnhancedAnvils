package com.mrbysco.enhancedanvils;

import com.mrbysco.enhancedanvils.client.ClientHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class EnhancedAnvilsNeoForge {
    
    public EnhancedAnvilsNeoForge(IEventBus eventBus, Dist dist, ModContainer container) {
	    NeoForge.EVENT_BUS.addListener(ClientHandler::onScreenOpen);
    }
}