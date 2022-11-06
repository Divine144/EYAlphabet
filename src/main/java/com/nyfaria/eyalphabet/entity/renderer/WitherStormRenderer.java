package com.nyfaria.eyalphabet.entity.renderer;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import com.nyfaria.eyalphabet.entity.model.WitherStormModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WitherStormRenderer extends GeoEntityRenderer<WitherStormEntity> {

    public WitherStormRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WitherStormModel());
        shadowRadius = 0.6f;
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull WitherStormEntity animate) {
        return new ResourceLocation(EYAlphabet.MODID, "textures/entity/wither_storm.png");
    }
}
