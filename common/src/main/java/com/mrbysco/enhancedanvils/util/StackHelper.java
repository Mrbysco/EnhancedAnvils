package com.mrbysco.enhancedanvils.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class StackHelper {
	public static void changeFont(ItemStack stack, String itemName) {
		if (!stack.isEmpty() && stack.has(DataComponents.CUSTOM_NAME) && itemName != null && TextFont.hasFormatting(itemName)) {
			Component name = stack.get(DataComponents.CUSTOM_NAME);
			stack.set(DataComponents.CUSTOM_NAME, TextHelper.changeFont(name));
		}
	}
}
