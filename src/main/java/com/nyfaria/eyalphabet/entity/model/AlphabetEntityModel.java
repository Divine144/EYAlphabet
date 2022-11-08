package com.nyfaria.eyalphabet.entity.model;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.HashMap;
import java.util.Map;

public class AlphabetEntityModel<T extends AlphabetEntity> extends AnimatedGeoModel<T> {
    private final Map<String, ResourceLocation> modelCache = new HashMap<>();
    private final Map<String, ResourceLocation> textureCache = new HashMap<>();
    private final Map<String, ResourceLocation> animationCache = new HashMap<>();

    @Override
    public ResourceLocation getModelResource(T object) {
        return this.modelCache.computeIfAbsent(object.getLetterId(), k -> new ResourceLocation(EYAlphabet.MODID, "geo/letter_" + k + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return this.textureCache.computeIfAbsent(object.getLetterId(), k -> new ResourceLocation(EYAlphabet.MODID, "textures/entity/letter_" + k + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T object) {
        return this.animationCache.computeIfAbsent(object.getLetterId(), k -> new ResourceLocation(EYAlphabet.MODID, "animations/letter_" + k + ".animation.json"));
    }
}
