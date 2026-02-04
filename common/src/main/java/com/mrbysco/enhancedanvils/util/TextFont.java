package com.mrbysco.enhancedanvils.util;

import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.regex.Pattern;

public enum TextFont implements StringRepresentable {
	DEFAULT("DEFAULT", 'w', Identifier.withDefaultNamespace("default")),
	UNIFORM("UNIFORM", 'y', Identifier.withDefaultNamespace("uniform")),
	ALT("ENCHANTMENT", 'x', Identifier.withDefaultNamespace("alt")),
	ILLAGERALT("ILLAGER", 'z', Identifier.withDefaultNamespace("illageralt"));

	private static final Pattern FORMATTING_PATTERN = Pattern.compile("(?i)\u00a7[WXYZ]");
	private final String name;
	private final String toString;
	private final Identifier location;

	private TextFont(String name, char code, Identifier location) {
		this.name = name;
		this.toString = "\u00a7" + code;
		this.location = location;
	}

	public String getName() {
		return this.name.toLowerCase(Locale.ROOT);
	}

	public Identifier getLocation() {
		return this.location;
	}

	@Override
	public String toString() {
		return this.toString;
	}

	@Override
	public String getSerializedName() {
		return this.getName();
	}

	public static String stripFormatting(@Nullable String text) {
		return text == null ? "" : FORMATTING_PATTERN.matcher(text).replaceAll("");
	}

	public static boolean hasFormatting(String text) {
		return FORMATTING_PATTERN.matcher(text).find();
	}
}
