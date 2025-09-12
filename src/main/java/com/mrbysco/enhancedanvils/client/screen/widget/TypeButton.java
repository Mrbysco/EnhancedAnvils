package com.mrbysco.enhancedanvils.client.screen.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class TypeButton<T extends Enum<T>> extends Button {
	private final T type;

	protected TypeButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration, T type) {
		super(x, y, width, height, message, onPress, createNarration);
		this.type = type;
	}

	T getType() {
		return this.type;
	}

	public TypeButton(Builder<T> builder) {
		this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, builder.createNarration, builder.type);
		this.setTooltip(builder.tooltip);
	}

	public static class Builder<T extends Enum<T>> {
		private final Component message;
		private final T type;
		private final OnPress onPress;
		@Nullable
		private Tooltip tooltip;
		private int x;
		private int y;
		private int width = 150;
		private int height = 20;
		private CreateNarration createNarration = Button.DEFAULT_NARRATION;

		public Builder(Component message, T type, OnPress onPress) {
			this.message = message;
			this.type = type;
			this.onPress = onPress;
		}

		public TypeButton.Builder<T> pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public TypeButton.Builder<T> width(int width) {
			this.width = width;
			return this;
		}

		public TypeButton.Builder<T> size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public TypeButton.Builder<T> bounds(int x, int y, int width, int height) {
			return this.pos(x, y).size(width, height);
		}

		public TypeButton.Builder<T> tooltip(@Nullable Tooltip tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public TypeButton.Builder<T> createNarration(CreateNarration createNarration) {
			this.createNarration = createNarration;
			return this;
		}

		public TypeButton<T> build() {
			return build(TypeButton::new);
		}

		public TypeButton<T> build(java.util.function.Function<TypeButton.Builder<T>, TypeButton<T>> builder) {
			return builder.apply(this);
		}
	}
}
