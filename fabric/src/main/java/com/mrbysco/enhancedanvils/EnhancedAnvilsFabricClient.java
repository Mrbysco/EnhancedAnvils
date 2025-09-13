package com.mrbysco.enhancedanvils;

import net.fabricmc.api.ClientModInitializer;

public class EnhancedAnvilsFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
//		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
//			if (screen.getClass().getName().equals("net.minecraft.client.gui.screens.inventory.AnvilScreen")) {
//				AnvilScreen anvilScreen = (AnvilScreen) screen;
//				AnvilMenu menu = anvilScreen.getMenu();
//				Component title = anvilScreen.getTitle();
//				screen.removed();
//				client.setScreen(new ExtendedAnvilScreen(menu, client.player.getInventory(), title));
//			}
//		});
	}
}
