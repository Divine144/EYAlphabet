package com.nyfaria.eyalphabet.init;

import com.nyfaria.eyalphabet.EYAlphabet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EYAlphabet.MODID);

    public static final RegistryObject<SoundEvent> JUMPSCARE = registerSound("jumpscare");

    protected static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(EYAlphabet.MODID, name)));
    }
}