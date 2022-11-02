package com.nyfaria.eyalphabet.event;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.renderer.AlphabetEntityRenderer;
import com.nyfaria.eyalphabet.init.EntityInit;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EYAlphabet.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        EntityRenderers.register(EntityInit.ALPHABET_ENTITY.get(), AlphabetEntityRenderer::new);
    }
}
