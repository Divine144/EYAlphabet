package com.nyfaria.eyalphabet.init;

import com.nyfaria.eyalphabet.EYAlphabet;
import dev._100media.hundredmediageckolib.item.animated.AnimatedItemProperties;
import dev._100media.hundredmediageckolib.item.animated.SimpleAnimatedItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EYAlphabet.MODID);
    public static final RegistryObject<Item> ALPHABET_WAND = ITEMS.register("alphabet_wand", () -> new SimpleAnimatedItem(new AnimatedItemProperties().tab(CreativeModeTab.TAB_COMBAT).durability(1000).rarity(Rarity.RARE)));

}
