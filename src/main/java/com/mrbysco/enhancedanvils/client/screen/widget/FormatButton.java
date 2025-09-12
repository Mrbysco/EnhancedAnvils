package com.mrbysco.enhancedanvils.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;

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
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		Minecraft minecraft = Minecraft.getInstance();
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.chatFormatting.isColor()) {
			Integer color = this.chatFormatting.getColor();
			if (color != null) {
				if (!this.isActive()) {
					color = FastColor.ARGB32.color(120, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color));
				} else {
					color = FastColor.ARGB32.opaque(color);
				}
				guiGraphics.fill(this.getX() + 2, this.getY() + 2,
						this.getX() + this.getWidth() - 2, this.getY() + this.getHeight() - 2, color);
			}
		}
		int i = getFGColor();
		this.renderString(guiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
	}

	public static class Builder {
		private final Component message;
		private final ChatFormatting formatting;
		private final Button.OnPress onPress;
		@Nullable
		private Tooltip tooltip;
		private int x;
		private int y;
		private int width = 150;
		private int height = 20;
		private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;

		public Builder(Component message, ChatFormatting formatting, Button.OnPress onPress) {
			this.message = message;
			this.formatting = formatting;
			this.onPress = onPress;
		}

		public FormatButton.Builder pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public FormatButton.Builder width(int width) {
			this.width = width;
			return this;
		}

		public FormatButton.Builder size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public FormatButton.Builder bounds(int x, int y, int width, int height) {
			return this.pos(x, y).size(width, height);
		}

		public FormatButton.Builder tooltip(@Nullable Tooltip tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public FormatButton.Builder createNarration(FormatButton.CreateNarration createNarration) {
			this.createNarration = createNarration;
			return this;
		}

		public FormatButton build() {
			return build(FormatButton::new);
		}

		public FormatButton build(java.util.function.Function<FormatButton.Builder, FormatButton> builder) {
			return builder.apply(this);
		}
	}
}
