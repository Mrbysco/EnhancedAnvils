package com.mrbysco.enhancedanvils.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class StringRenderHelper {
	public static int drawScrollingString(GuiGraphics guiGraphics, Font font, Component text, int minX, int maxX, int y, int color) {
		int maxWidth = maxX - minX;
		int textWidth = font.width(text.getVisualOrderText());
		if (textWidth <= maxWidth) {
			return guiGraphics.drawString(font, text, minX, y, color);
		} else {
			AbstractWidget.renderScrollingString(guiGraphics, font, text, minX, y, maxX, y + font.lineHeight, color);
			return maxWidth;
		}
	}
}
