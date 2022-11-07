package com.nyfaria.eyalphabet.entity.model;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AlphabetEntityModel extends AnimatedGeoModel<AlphabetEntity> {

    @Override
    public ResourceLocation getModelResource(AlphabetEntity object) {
        return object.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(AlphabetEntity object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(AlphabetEntity object) {
        return object.getAnimationLocation();
    }
}
