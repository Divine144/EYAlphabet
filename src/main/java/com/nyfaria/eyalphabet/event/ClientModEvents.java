package com.nyfaria.eyalphabet.event;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.renderer.AlphabetEntityRenderer;
import com.nyfaria.eyalphabet.entity.renderer.WitherStormRenderer;
import com.nyfaria.eyalphabet.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EYAlphabet.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void init(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.ALPHABET_ENTITY.get(), AlphabetEntityRenderer::new);
        event.registerEntityRenderer(EntityInit.SPECIAL_F.get(), AlphabetEntityRenderer::new);
        event.registerEntityRenderer(EntityInit.SPECIAL_H.get(), AlphabetEntityRenderer::new);
        event.registerEntityRenderer(EntityInit.SPECIAL_E.get(), AlphabetEntityRenderer::new);
        event.registerEntityRenderer(EntityInit.WITHER_STORM_ENTITY.get(), WitherStormRenderer::new);
    }
}
