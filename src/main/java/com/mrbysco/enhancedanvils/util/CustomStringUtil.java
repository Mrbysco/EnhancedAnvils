package com.mrbysco.enhancedanvils.util;

import net.minecraft.ChatFormatting;

/**
 * A custom version of StringUtil from minecraft without the formatting code filter.
 */
public class CustomStringUtil {
	public static String filterText(String text) {
		return filterText(text, false);
	}

	public static String filterText(String text, boolean allowLineBreaks) {
		StringBuilder stringbuilder = new StringBuilder();

		for (char c0 : text.toCharArray()) {
			if (isAllowedCharacter(c0)) {
				stringbuilder.append(c0);
			} else if (allowLineBreaks && c0 == '\n') {
				stringbuilder.append(c0);
			}
		}

		return stringbuilder.toString();
	}

	public static boolean isAllowedCharacter(char character) {
		return character >= ' ' && character != 127;
	}

	/**
	 * Removes all formatting codes from a string.
	 * Known formatting codes are: §0-§9, §a-§f, §k-§r
	 * Known font codes are §w, §x, §y, §z
	 *
	 * @param text The text to filter
	 * @return The filtered text without formatting codes
	 */
	public static String fullyFiltered(String text) {
		// Filter the custom font codes first
		text = TextFont.stripFormatting(text);
		text = TextLore.stripFormatting(text);
		return ChatFormatting.stripFormatting(text);
	}
}
