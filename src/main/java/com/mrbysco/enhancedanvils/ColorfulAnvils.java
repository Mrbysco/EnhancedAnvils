package com.mrbysco.enhancedanvils;

import com.mojang.logging.LogUtils;
import com.mrbysco.enhancedanvils.client.ClientHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(ColorfulAnvils.MOD_ID)
public class ColorfulAnvils {
	public static final String MOD_ID = "enhancedanvils";
	public static final Logger LOGGER = LogUtils.getLogger();

	public ColorfulAnvils(IEventBus eventBus, Dist dist, ModContainer container) {
		NeoForge.EVENT_BUS.addListener(ClientHandler::onScreenOpen);
	}
}
