package com.mrbysco.enhancedanvils.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class StringRenderHelper {
	public static void drawScrollingString(GuiGraphics guiGraphics, Font font, Component text, int minX, int maxX, int y, int color) {
		int maxWidth = maxX - minX;
		int textWidth = font.width(text.getVisualOrderText());
		if (textWidth <= maxWidth) {
			guiGraphics.drawString(font, text, minX, y, color);
		} else {
			AbstractWidget.renderScrollingString(guiGraphics, font, text, minX, y - 1, maxX, y + font.lineHeight, color);
		}
	}
}
