package com.mrbysco.enhancedanvils.mixin;

import com.mrbysco.enhancedanvils.client.screen.ExtendedAnvilScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
	@Shadow
	@Final
	private Player player;

	public AnvilScreenMixin(AnvilMenu itemCombinerMenu, Inventory inventory, Component component, ResourceLocation resourceLocation) {
		super(itemCombinerMenu, inventory, component, resourceLocation);
	}

	@Inject(method = "subInit()V", at = @At("HEAD"))
	private void enhancedanvils$init(CallbackInfo ci) {
		if (((AnvilScreen) (Object) this).getClass().getName().equals("net.minecraft.client.gui.screens.inventory.AnvilScreen")) {
			this.minecraft.setScreen(new ExtendedAnvilScreen(this.menu, this.player.getInventory(), this.title));
		}
	}
}
