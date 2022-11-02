package com.nyfaria.eyalphabet.init;

import com.nyfaria.eyalphabet.EYAlphabet;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EYAlphabet.MODID);
}
