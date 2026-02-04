package com.mrbysco.enhancedanvils.client.screen;

import com.mrbysco.enhancedanvils.client.StringRenderHelper;
import com.mrbysco.enhancedanvils.client.screen.widget.ComponentEditBox;
import com.mrbysco.enhancedanvils.client.screen.widget.FormatButton;
import com.mrbysco.enhancedanvils.client.screen.widget.TypeButton;
import com.mrbysco.enhancedanvils.util.TextFont;
import com.mrbysco.enhancedanvils.util.TextHelper;
import com.mrbysco.enhancedanvils.util.TextLore;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;

import java.util.List;

public class ExtendedAnvilScreen extends AnvilScreen {
	private final List<ChatFormatting> formats = List.of(
			ChatFormatting.BLACK, ChatFormatting.DARK_BLUE, ChatFormatting.DARK_GREEN, ChatFormatting.DARK_AQUA,
			ChatFormatting.DARK_RED, ChatFormatting.DARK_PURPLE, ChatFormatting.GOLD, ChatFormatting.GRAY,
			ChatFormatting.DARK_GRAY, ChatFormatting.BLUE, ChatFormatting.GREEN, ChatFormatting.AQUA,
			ChatFormatting.RED, ChatFormatting.LIGHT_PURPLE, ChatFormatting.YELLOW, ChatFormatting.WHITE,
			ChatFormatting.OBFUSCATED, ChatFormatting.BOLD, ChatFormatting.STRIKETHROUGH,
			ChatFormatting.UNDERLINE, ChatFormatting.ITALIC, ChatFormatting.RESET
	);
	private final List<TextFont> fonts = List.of(TextFont.values());
	private final List<TextLore> lores = List.of(TextLore.values());

	private final FormatButton[] formattingButtons = new FormatButton[formats.size()];
	private final TypeButton[] fontButtons = new TypeButton[fonts.size()];
	private final TypeButton[] loreButtons = new TypeButton[lores.size()];
	private Button menuButton;
	private boolean buttonsVisible = false;

	public ExtendedAnvilScreen(AnvilMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	protected void subInit() {
		int centerWidth = (this.width - this.imageWidth) / 2;
		int centerHeight = (this.height - this.imageHeight) / 2;
		this.name = new ComponentEditBox(font, centerWidth + 62, centerHeight + 24, 103, 12, Component.translatable("container.repair"));
		this.name.setCanLoseFocus(false);
		this.name.setTextColor(-1);
		this.name.setTextColorUneditable(-1);
		this.name.setBordered(false);
		this.name.setMaxLength(50);
		this.name.setResponder(this::onNameChanged);
		this.name.setValue("");
		this.addRenderableWidget(this.name);
		this.name.setEditable(this.menu.getSlot(0).hasItem());

		// Add hamburger menu button on left side of UI
		this.menuButton = new Button.Builder(Component.literal("☰"), (button) -> {
			this.buttonsVisible = !this.buttonsVisible;
			this.name.setFocused(true);
		}).bounds(centerWidth + 4, centerHeight + 4, 16, 16).build();
		this.menuButton.setTooltip(Tooltip.create(Component.literal("Show/Hide Formatting Options")));
		this.addRenderableWidget(menuButton);

		for (int i = 0; i < formats.size(); i++) {
			ChatFormatting format = formats.get(i);
			int xPos = centerWidth - (4 * 18 + 4) + (i % 4) * 18;
			int yPos = centerHeight + 4 + (i / 4) * 18;
			// Center the last 2 values (because there are 22 total)
			if (i >= 20) {
				xPos += 18;
			}
			Component name = Component.empty();
			switch (format) {
				case OBFUSCATED -> name = Component.literal("O").withStyle(ChatFormatting.OBFUSCATED);
				case BOLD -> name = Component.literal("B").withStyle(ChatFormatting.BOLD);
				case STRIKETHROUGH -> name = Component.literal("ab").withStyle(ChatFormatting.STRIKETHROUGH);
				case UNDERLINE -> name = Component.literal("U").withStyle(ChatFormatting.UNDERLINE);
				case ITALIC -> name = Component.literal("I").withStyle(ChatFormatting.ITALIC);
				case RESET -> name = Component.literal("\uD83D\uDDD8");
			}

			formattingButtons[i] = new FormatButton.Builder(name, format, (button) -> {
				if (this.name.isEditable())
					this.name.insertText(format.toString());
				this.setFocused(this.name);
			}).bounds(xPos, yPos, 16, 16).build();
			formattingButtons[i].setTooltip(Tooltip.create(Component.literal(format.getName())));
			formattingButtons[i].active = false;
			formattingButtons[i].visible = false;
			this.addRenderableWidget(formattingButtons[i]);
		}

		int xPos = centerWidth + this.imageWidth + 4;
		int yPos = centerHeight - 14;
		for (int i = 0; i < fonts.size(); i++) {
			TextFont fontType = fonts.get(i);
			yPos += 18;

			MutableComponent name = Component.literal(fontType.getName()).withStyle((style -> style.withFont(new FontDescription.Resource(fontType.getLocation()))));
			fontButtons[i] = new TypeButton.Builder<>(name, fontType, (button) -> {
				if (this.name.isEditable()) {
					// If it already starts with a font replace the font
					String currentText = this.name.getValue();
					String strippedText = TextFont.stripFormatting(currentText);
					//Check if first 2 character is lore, if so place font after lore
					if (strippedText != null && strippedText.length() >= 2) {
						String firstTwo = strippedText.substring(0, 2);
						TextLore lore = TextLore.getFromString(firstTwo);
						if (lore != null) {
							this.name.setValue(lore.toString() + fontType + strippedText.substring(2));
							this.setFocused(this.name);
						} else {
							this.name.setValue(fontType + strippedText);
						}
					} else {
						this.name.setValue(fontType + strippedText);
					}
					this.setFocused(this.name);
				}
			}).bounds(xPos, yPos, 48, 16).build();
			fontButtons[i].setTooltip(Tooltip.create(Component.literal(fontType.getName())));
			fontButtons[i].active = false;
			fontButtons[i].visible = false;
			this.addRenderableWidget(fontButtons[i]);
		}

		yPos += 8;
		for (int i = 0; i < lores.size(); i++) {
			TextLore lore = lores.get(i);
			yPos += 18;
			MutableComponent name = Component.literal("Lore " + lore.getLine()).withStyle(style -> style.withColor(ChatFormatting.GRAY));
			loreButtons[i] = new TypeButton.Builder<>(name, lore, (button) -> {
				if (this.name.isEditable()) {
					// If it already has a lore of that line, replace it
					String currentText = this.name.getValue();
					String strippedText = TextLore.stripFormatting(currentText);
					this.name.setValue(lore + strippedText);
				}
				this.setFocused(this.name);
			}).bounds(xPos, yPos, 48, 16).build();
			loreButtons[i].setTooltip(Tooltip.create(Component.literal("Add to " + name.getString())));
			loreButtons[i].active = false;
			loreButtons[i].visible = false;
			this.addRenderableWidget(loreButtons[i]);
		}
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		if (this.buttonsVisible) {
			this.setButtonsVisible(true, formattingButtons, fontButtons, loreButtons);
			this.setButtonsActive(this.name.isEditable(), formattingButtons, fontButtons, loreButtons);
		} else {
			this.setButtonsVisible(false, formattingButtons, fontButtons, loreButtons);
			this.setButtonsActive(false, formattingButtons, fontButtons, loreButtons);
		}
	}

	private void setButtonsVisible(boolean visible, Button[]... buttonArrays) {
		for (Button[] array : buttonArrays) {
			for (Button button : array) {
				button.visible = visible;
			}
		}
	}

	private void setButtonsActive(boolean visible, Button[]... buttonArrays) {
		for (Button[] array : buttonArrays) {
			for (Button button : array) {
				button.active = visible;
			}
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);

		if (buttonsVisible && menu.getSlot(0).hasItem() && this.name instanceof ComponentEditBox box) {
			if (!box.isVisible()) return;
			Component adjustedValue = TextHelper.changeFont(box.getFinalValue());
			TooltipRenderUtil.renderTooltipBackground(guiGraphics, box.getX() - 58, box.getY() - 40, 168, this.font.lineHeight, null);
			StringRenderHelper.drawScrollingString(guiGraphics, guiGraphics.textRenderer(), this.font, adjustedValue,
					box.getX() - 58, box.getX() + 110, box.getY() - 40, ARGB.opaque(-1)
			);
		}
	}
}
