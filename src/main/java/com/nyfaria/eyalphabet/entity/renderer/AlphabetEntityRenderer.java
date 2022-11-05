package com.nyfaria.eyalphabet.entity.renderer;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.model.AlphabetEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class AlphabetEntityRenderer extends GeoEntityRenderer<AlphabetEntity> {

    public AlphabetEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlphabetEntityModel());
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull AlphabetEntity animate) {
        return null;
    }
}
