package com.mrbysco.enhancedanvils.plugin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ModelBrowserMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
		if (FabricLoader.getInstance().isModLoaded("modelbrowser")) {
			System.out.println("[EnhancedAnvils] Applying Model Browser compatibility");
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		List<String> classList = List.of(
				"com.mrbysco.enhancedanvils.mixin.ModelBrowserAnvilScreenMixin"
		);
		if (classList.contains(mixinClassName)) {
			return FabricLoader.getInstance().isModLoaded("modelbrowser");
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
		// noop
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// noop
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// noop
	}
}