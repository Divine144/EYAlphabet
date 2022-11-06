package com.nyfaria.eyalphabet.entity.model;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WitherStormModel extends AnimatedGeoModel<WitherStormEntity> {

    @Override
    public ResourceLocation getModelResource(WitherStormEntity object) {
        return new ResourceLocation(EYAlphabet.MODID, "geo/wither_storm.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WitherStormEntity object) {
        return new ResourceLocation(EYAlphabet.MODID, "textures/entity/wither_storm.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WitherStormEntity animatable) {
        return new ResourceLocation(EYAlphabet.MODID, "animations/wither_storm.animation.json");
    }
}
