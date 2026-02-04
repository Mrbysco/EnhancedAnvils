package com.mrbysco.enhancedanvils;

import com.mrbysco.enhancedanvils.client.screen.ExtendedAnvilScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AnvilMenu;

public class EnhancedAnvilsFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen.getClass().getName().equals("net.minecraft.client.gui.screens.inventory.AnvilScreen") ||
					screen.getClass().getName().equals("net.minecraft.class_471")) {
				AnvilScreen anvilScreen = (AnvilScreen) screen;
				AnvilMenu menu = anvilScreen.getMenu();
				Component title = anvilScreen.getTitle();
				screen.removed();
				client.setScreen(new ExtendedAnvilScreen(menu, client.player.getInventory(), title));
			}
		});
	}
}
