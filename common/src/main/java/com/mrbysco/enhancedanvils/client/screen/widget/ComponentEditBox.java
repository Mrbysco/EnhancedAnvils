package com.mrbysco.enhancedanvils.client.screen.widget;

import com.mrbysco.enhancedanvils.util.CustomStringUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ComponentEditBox extends EditBox {
	private Component finalValue = Component.empty();

	public ComponentEditBox(Font font, int width, int height, Component message) {
		super(font, width, height, message);
	}

	public ComponentEditBox(Font font, int x, int y, int width, int height, Component message) {
		super(font, x, y, width, height, message);
	}

	public ComponentEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
		super(font, x, y, width, height, editBox, message);
	}

	public Component getFinalValue() {
		return finalValue.copy();
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		if (!this.canConsumeInput()) {
			return false;
		} else if (CustomStringUtil.isAllowedCharacter(event.codepoint())) {
			if (this.isEditable()) {
				this.insertText(event.codepointAsString());
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void insertText(String input) {
		int i = Math.min(this.cursorPos, this.highlightPos);
		int j = Math.max(this.cursorPos, this.highlightPos);
		String filteredValue = CustomStringUtil.fullyFiltered(this.value);
		int k = this.maxLength - filteredValue.length() - (i - j);
		if (k > 0) {
			String s = CustomStringUtil.filterText(input);
			int l = s.length();
			if (k < l) {
				if (Character.isHighSurrogate(s.charAt(k - 1))) {
					k--;
				}

				s = s.substring(0, k);
				l = k;
			}

			this.value = new StringBuilder(this.value).replace(i, j, s).toString();
			this.setCursorPosition(i + l);
			this.setHighlightPos(this.cursorPos);
			this.onValueChange(this.value);
		}
	}

	@Override
	public void setValue(String value) {
		String formattedString = CustomStringUtil.fullyFiltered(value);
		if (formattedString.length() > this.maxLength) {
			this.value = value.substring(0, this.maxLength);
		} else {
			this.value = value;
		}

		this.moveCursorToEnd(false);
		this.setHighlightPos(this.cursorPos);
		this.onValueChange(value);
		this.finalValue = Component.literal(value);
	}

	@Override
	public void onValueChange(String newText) {
		super.onValueChange(newText);
		this.finalValue = Component.literal(newText);
	}
}
