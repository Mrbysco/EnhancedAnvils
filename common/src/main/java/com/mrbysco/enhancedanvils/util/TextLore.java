package com.mrbysco.enhancedanvils.util;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.regex.Pattern;

public enum TextLore implements StringRepresentable {
	LINE_1("LINE_1", 't', 1),
	LINE_2("LINE_2", 'u', 2),
	LINE_3("LINE_3", 'v', 3);

	private static final Pattern FORMATTING_PATTERN = Pattern.compile("(?i)\u00a7[TUV]");
	private final String name;
	private final String toString;
	private final int line;

	private TextLore(String name, char code, int line) {
		this.name = name;
		this.toString = "\u00a7" + code;
		this.line = line;
	}

	public String getName() {
		return this.name.toLowerCase(Locale.ROOT);
	}

	public int getLine() {
		return this.line;
	}

	@Override
	public String toString() {
		return this.toString;
	}

	@Override
	public String getSerializedName() {
		return this.getName();
	}

	public static TextLore getFromString(String format) {
		for (TextLore lore : values()) {
			if (lore.toString().equalsIgnoreCase(format)) {
				return lore;
			}
		}
		return null;
	}

	public static String stripFormatting(@Nullable String text) {
		return text == null ? "" : FORMATTING_PATTERN.matcher(text).replaceAll("");
	}

	public static boolean hasFormatting(String text) {
		return FORMATTING_PATTERN.matcher(text).find();
	}
}
