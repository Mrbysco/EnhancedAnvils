package com.mrbysco.enhancedanvils.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TextHelper {

	/**
	 * Changes the font of a given component if it starts with a known font code.
	 *
	 * @param component The component to change the font of
	 * @return A new component with the changed font, or the original component if no font code was found
	 */
	public static Component changeFont(Component component) {
		String text = component.getString();
		for (TextFont font : TextFont.values()) {
			// Check if the text starts with the font code
			if (text.startsWith(font.toString())) {
				// Remove the font code from the text
				String newText = text.substring(font.toString().length());
				// Create a new component with the new text and font style
				MutableComponent returnComp = Component.literal(newText);

				returnComp.withStyle(style -> style.withFont(font.getLocation()));
				return returnComp;
			}
		}
		return component;
	}

	/**
	 * Checks if the lore has changed based on the provided item name.
	 * @param originalLore The original lore of the item
	 * @param itemName The item name containing potential lore formatting codes
	 * @return True if the lore has changed, false otherwise
	 */
	public static boolean loreChanged(ItemLore originalLore, String itemName) {
		for (TextLore lore : TextLore.values()) {
			if (itemName.contains(lore.toString())) {
				String strippedName = TextLore.stripFormatting(itemName);
				if (strippedName == null) {
					strippedName = "";
				} else {
					strippedName = strippedName.replace(lore.toString(), "");
				}

				List<Component> lines = new ArrayList<>(originalLore.lines());
				//Check if the TextLore line is different from the stripped name
				if (lines.size() >= lore.getLine()) {
					if (lines.get(lore.getLine() - 1).getString().equals(strippedName)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Creates a new ItemLore based on the original lore and the provided item name.
	 * @param originalLore The original lore of the item
	 * @param itemName The item name containing potential lore formatting codes
	 * @return A new ItemLore with the updated lore, or null if no changes were made
	 */
	@Nullable
	public static ItemLore createLore(ItemLore originalLore, String itemName) {
		for (TextLore lore : TextLore.values()) {
			if (itemName.contains(lore.toString())) {
				String strippedName = TextLore.stripFormatting(itemName);
				if (strippedName == null) {
					strippedName = "";
				} else {
					strippedName = strippedName.replace(lore.toString(), "");
				}

				List<Component> lines = new ArrayList<>(originalLore.lines());
				if (lines.size() >= lore.getLine()) {
					if (strippedName.isEmpty())
 						lines.set(lore.getLine() - 1, Component.empty());
					else
						lines.set(lore.getLine() - 1, TextHelper.changeFont(Component.literal(strippedName)));
				} else {
					// If the lore doesn't have enough lines, add empty lines until it does
					while (lines.size() < lore.getLine() - 1) {
						lines.add(Component.empty());
					}
					lines.add(changeFont(Component.literal(strippedName)));
				}

				return new ItemLore(lines);
			}
		}
		return null;
	}

	/**
	 * Sets the lore of an ItemStack based on the provided item name.
	 *
	 * @param stack The ItemStack to set the lore for
	 * @param itemName  The item name containing potential lore formatting codes
	 */
	public static void setLore(ItemStack stack, String itemName) {
		ItemLore lore = TextHelper.createLore(
				stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY), itemName
		);
		boolean allEmpty = true;
		for (Component line : lore.lines()) {
			if (!line.getString().isEmpty()) {
				allEmpty = false;
				break;
			}
		}
		if (allEmpty) {
			stack.remove(DataComponents.LORE);
		} else {
			stack.set(DataComponents.LORE, lore);
		}
	}
}
