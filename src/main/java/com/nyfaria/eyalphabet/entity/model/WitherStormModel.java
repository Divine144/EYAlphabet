package com.nyfaria.eyalphabet.entity.model;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WitherStormModel extends AnimatedGeoModel<WitherStormEntity> {

    private final ResourceLocation model = new ResourceLocation(EYAlphabet.MODID, "geo/wither_storm.geo.json");
    private final ResourceLocation texture = new ResourceLocation(EYAlphabet.MODID, "textures/entity/wither_storm.png");
    private final ResourceLocation animation = new ResourceLocation(EYAlphabet.MODID, "animations/wither_storm.animation.json");

    @Override
    public ResourceLocation getModelResource(WitherStormEntity object) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(WitherStormEntity object) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(WitherStormEntity animatable) {
        return animation;
    }
}
