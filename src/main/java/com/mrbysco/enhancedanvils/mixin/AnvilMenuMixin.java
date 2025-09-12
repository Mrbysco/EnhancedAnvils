package com.mrbysco.enhancedanvils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mrbysco.enhancedanvils.util.CustomStringUtil;
import com.mrbysco.enhancedanvils.util.TextHelper;
import com.mrbysco.enhancedanvils.util.TextLore;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

	@Shadow
	@Nullable
	private String itemName;

	public AnvilMenuMixin(@org.jetbrains.annotations.Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
		super(type, containerId, playerInventory, access);
	}

	@ModifyArg(method = "setItemName(Ljava/lang/String;)Z",
			slice = @Slice(
					from = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/world/inventory/AnvilMenu;getSlot(I)Lnet/minecraft/world/inventory/Slot;"),
					to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/world/inventory/AnvilMenu;createResult()V")
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"),
			index = 1)
	private Object colorfulanvils$setItemName(Object value) {
		if (value instanceof Component component) {
			component = TextHelper.changeFont(component);
			return component;
		}
		return value;
	}

	@Inject(method = "setItemName(Ljava/lang/String;)Z", at = @At(value = "HEAD"), cancellable = true)
	public void colorfulanvils$setItemName2(String itemName, CallbackInfoReturnable<Boolean> cir) {
		if (TextLore.hasFormatting(itemName)) {
			this.itemName = itemName;
			if (this.getSlot(2).hasItem()) {
				ItemStack itemstack = this.getSlot(2).getItem();
				TextHelper.setLore(itemstack, itemName);
				this.broadcastChanges();
			}

			this.createResult();
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "createResult()V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V",
					ordinal = 0,
					shift = At.Shift.AFTER
			), cancellable = true
	)
	public void colorfulanvils$createResult(CallbackInfo ci, @Local(name = "itemstack") ItemStack itemstack) {
		if (this.inputSlots.getItem(1).isEmpty() && itemName != null && TextLore.hasFormatting(itemName)) {
			ItemStack itemstack1 = itemstack.copy();
			if (TextHelper.loreChanged(itemstack1.getOrDefault(DataComponents.LORE, ItemLore.EMPTY), itemName)) {
				TextHelper.setLore(itemstack1, itemName);
				this.resultSlots.setItem(0, itemstack1);
				ci.cancel();
			}
		}
	}

	@Inject(method = "validateName(Ljava/lang/String;)Ljava/lang/String;",
			at = @At(value = "HEAD"), cancellable = true)
	private static void colorfulanvils$validateName(String itemName, CallbackInfoReturnable<String> cir) {
		String s = CustomStringUtil.filterText(itemName);
		cir.setReturnValue(CustomStringUtil.fullyFiltered(s).length() <= 50 ? TextLore.stripFormatting(s) : null);
	}
}
