package com.mrbysco.enhancedanvils.client.screen.widget;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;

public class FormatButton extends Button {
	private final ChatFormatting chatFormatting;

	protected FormatButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration, ChatFormatting chatFormatting) {
		super(x, y, width, height, message, onPress, createNarration);
		this.chatFormatting = chatFormatting;
	}

	public FormatButton(Builder builder) {
		this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, builder.createNarration, builder.formatting);
		this.setTooltip(builder.tooltip);
	}

	@Override
	protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		Minecraft minecraft = Minecraft.getInstance();
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		if (this.chatFormatting.isColor()) {
			Integer color = this.chatFormatting.getColor();
			if (color != null) {
				if (!this.isActive()) {
					color = ARGB.color(120, ARGB.red(color), ARGB.green(color), ARGB.blue(color));
				} else {
					color = ARGB.opaque(color);
				}
				guiGraphics.fill(this.getX() + 2, this.getY() + 2,
						this.getX() + this.getWidth() - 2, this.getY() + this.getHeight() - 2, color);
			}
		}
		int i = this.active ? 16777215 : 10526880;
		var message = getMessage().copy().withStyle(style -> style.withColor(i)); // TODO 1.21.11: Inefficient, check how Vanilla does this
		this.renderScrollingStringOverContents(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE), message, 2);
	}


	public static class Builder {
		private final Component message;
		private final ChatFormatting formatting;
		private final OnPress onPress;
		@Nullable
		private Tooltip tooltip;
		private int x;
		private int y;
		private int width = 150;
		private int height = 20;
		private CreateNarration createNarration = Button.DEFAULT_NARRATION;

		public Builder(Component message, ChatFormatting formatting, OnPress onPress) {
			this.message = message;
			this.formatting = formatting;
			this.onPress = onPress;
		}

		public Builder pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public Builder width(int width) {
			this.width = width;
			return this;
		}

		public Builder size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public Builder bounds(int x, int y, int width, int height) {
			return this.pos(x, y).size(width, height);
		}

		public Builder tooltip(@Nullable Tooltip tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public Builder createNarration(CreateNarration createNarration) {
			this.createNarration = createNarration;
			return this;
		}

		public FormatButton build() {
			return build(FormatButton::new);
		}

		public FormatButton build(java.util.function.Function<Builder, FormatButton> builder) {
			return builder.apply(this);
		}
	}
}
