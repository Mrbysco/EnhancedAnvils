package com.mrbysco.enhancedanvils.client;

import com.mrbysco.enhancedanvils.client.screen.ExtendedAnvilScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ClientHandler {

	public static void onScreenOpen(ScreenEvent.Opening event) {
		// Check if screen is net.minecraft.client.gui.screens.inventory.AnvilScreen and not an instance of one (Modded Anvil Screens)
		if (event.getScreen().getClass().getName().equals("net.minecraft.client.gui.screens.inventory.AnvilScreen")) {
			AnvilScreen anvilScreen = (AnvilScreen) event.getScreen();
			event.setNewScreen(new ExtendedAnvilScreen(anvilScreen.getMenu(), Minecraft.getInstance().player.getInventory(), anvilScreen.getTitle()));
		}
	}
}
