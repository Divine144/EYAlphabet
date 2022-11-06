package com.nyfaria.eyalphabet.entity.model;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AlphabetEntityModel extends AnimatedGeoModel<AlphabetEntity> {

    @Override
    public ResourceLocation getModelResource(AlphabetEntity object) {
        return new ResourceLocation(EYAlphabet.MODID, "geo/letter_" + Util.getLetterFromID(object.getLetterID()) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AlphabetEntity object) {
        return new ResourceLocation(EYAlphabet.MODID, "textures/entity/letter_" + Util.getLetterFromID(object.getLetterID()) + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AlphabetEntity object) {
        return new ResourceLocation(EYAlphabet.MODID, "animations/letter_" + Util.getLetterFromID(object.getLetterID()) + ".animation.json");
    }
}
