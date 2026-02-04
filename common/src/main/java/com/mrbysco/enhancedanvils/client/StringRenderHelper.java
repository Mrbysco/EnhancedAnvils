package com.mrbysco.enhancedanvils.client;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class StringRenderHelper {
	public static void drawScrollingString(GuiGraphics guiGraphics, ActiveTextCollector textCollector,
	                                       Font font, Component text, int minX, int maxX, int y, int color) {
		int maxWidth = maxX - minX;
		int textWidth = font.width(text.getVisualOrderText());
		if (textWidth <= maxWidth) {
			guiGraphics.drawString(font, text, minX, y, color);
		} else {
			textCollector.acceptScrollingWithDefaultCenter(text, minX, maxX, y - 1, y + font.lineHeight);
		}
	}
}
