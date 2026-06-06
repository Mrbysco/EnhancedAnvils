package com.mrbysco.enhancedanvils.mixin;

import com.mrbysco.enhancedanvils.client.screen.ExtendedAnvilScreen;
import com.mrbysco.enhancedanvils.client.screen.widget.FormatButton;
import com.mrbysco.enhancedanvils.client.screen.widget.TypeButton;
import deborn.modelbrowser.config.ModConfig;
import deborn.modelbrowser.gui.ModelBrowserWidget;
import deborn.modelbrowser.mixin.HandledScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Based on AnvilScreenMixin from ModelBrowser but adapted for ExtendedAnvilScreen
 * Original: <a href="https://github.com/DebornMC/model-browser/blob/26.1/src/client/java/deborn/modelbrowser/mixin/AnvilScreenMixin.java">...</a>
 * License: CC0-1.0
 */
@Mixin(value = ExtendedAnvilScreen.class, remap = false)
public abstract class ModelBrowserAnvilScreenMixin extends AnvilScreen {
	private ImageButton toggleButton;
	private ModelBrowserWidget modelBrowserWidget;
	private static final WidgetSprites RECIPE_BUTTON_TEXTURES = new WidgetSprites(
			Identifier.withDefaultNamespace("icon/search"));
	private static final int UI_SHIFT_AMOUNT = 77;

	public ModelBrowserAnvilScreenMixin(AnvilMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	private int getTop() {
		return (this.height - 166) / 2;
	}

	private int getLeft() {
		return (this.width - 176) / 2;
	}

	@Shadow
	protected abstract void subInit();

	@Shadow
	private Button menuButton;

	@Shadow
	@Final
	private FormatButton[] formattingButtons;

	@Shadow
	@Final
	private TypeButton[] fontButtons;

	@Shadow
	@Final
	private TypeButton[] loreButtons;

	@Shadow
	private boolean buttonsVisible;

	@Inject(method = "subInit", at = @At("TAIL"))
	private void setupUI(CallbackInfo ci) {
		if (ModConfig.INSTANCE != null && !ModConfig.INSTANCE.showAnvilScreenTab) {
			return;
		}
		modelBrowserWidget = new ModelBrowserWidget(minecraft, this.width, this.height);
		modelBrowserWidget.initialize();

		name.setWidth(86);
		toggleButton = new ImageButton(
				this.getLeft() + 154,
				this.getTop() + 22,
				12, 12,
				RECIPE_BUTTON_TEXTURES,
				b -> toggleModelBrowser(),
				Component.translatable("modelbrowser.open_menu"));
		addRenderableWidget(toggleButton);

		if (modelBrowserWidget.isOpen()) {
			shiftUI();
		}
	}

	private void toggleModelBrowser() {
		modelBrowserWidget.toggle();
		shiftUI();

		// Disable EnhancedAnvils buttons
		if (modelBrowserWidget.isOpen()) {
			if (buttonsVisible) {
				buttonsVisible = false;
			}
		}
		menuButton.active = !modelBrowserWidget.isOpen();
	}

	private void shiftUI() {
		int dir = modelBrowserWidget.isOpen() ? UI_SHIFT_AMOUNT : -UI_SHIFT_AMOUNT;
		HandledScreenAccessor acc = (HandledScreenAccessor) (Object) this;
		acc.setX(acc.getX() + dir);
		name.setX(name.getX() + dir);
		toggleButton.setX(toggleButton.getX() + dir);

		// EnhancedAnvils elements
		menuButton.setX(menuButton.getX() + dir);
		for (var button : formattingButtons) {
			button.setX(button.getX() + dir);
		}
		for (var button : fontButtons) {
			button.setX(button.getX() + dir);
		}
		for (var button : loreButtons) {
			button.setX(button.getX() + dir);
		}
	}

	@Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
	private void interceptKeys(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
		if (modelBrowserWidget == null || !modelBrowserWidget.isOpen())
			return;
		if (input.isEscape()) {
			Minecraft client = Minecraft.getInstance();
			if (client != null && client.player != null) {
				client.player.closeContainer();
			}
			cir.setReturnValue(true);
			return;
		}

		if (modelBrowserWidget.getSearchField().canConsumeInput()) {
			modelBrowserWidget.getSearchField().keyPressed(input);
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "extractBackground", at = @At("TAIL"))
	private void drawShiftedRecipeBook(GuiGraphicsExtractor ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (modelBrowserWidget == null || !modelBrowserWidget.isOpen())
			return;
		modelBrowserWidget.drawBackground(ctx);
	}

	@Inject(method = "extractLabels", at = @At("TAIL"))
	private void drawModelGrid(GuiGraphicsExtractor ctx, int mouseX, int mouseY, CallbackInfo ci) {
		if (modelBrowserWidget == null || !modelBrowserWidget.isOpen())
			return;
		modelBrowserWidget.drawForeground(ctx, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
		if (modelBrowserWidget == null || !modelBrowserWidget.isOpen())
			return super.mouseClicked(click, doubled);

		HandledScreenAccessor acc = (HandledScreenAccessor) (Object) this;
		AbstractContainerMenu handler = acc.getHandler();

		if (modelBrowserWidget.handleClick(click, doubled, handler, name, this)) {
			// ModelBrowser.LOGGER.info("Handled mouse click in model browser");
			return true;
		}

		return super.mouseClicked(click, doubled);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent click) {
		if (modelBrowserWidget == null || !modelBrowserWidget.isOpen())
			return super.mouseReleased(click);

		if (modelBrowserWidget.handleRelease(click)) {
			return true;
		}

		return super.mouseReleased(click);
	}

	@Inject(method = "extractRenderState", at = @At("HEAD"))
	public void drawWidget(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		if (modelBrowserWidget != null && modelBrowserWidget.isOpen())
			modelBrowserWidget.extractRenderState(graphics, mouseX, mouseY, partialTick);
	}
}