package com.nyfaria.eyalphabet.entity.renderer;

import com.nyfaria.eyalphabet.entity.IAlphabetHolder;
import com.nyfaria.eyalphabet.entity.model.AlphabetEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class AlphabetEntityRenderer<T extends LivingEntity & IAlphabetHolder & IAnimatable> extends GeoEntityRenderer<T> {
    public AlphabetEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AlphabetEntityModel<>());
        this.shadowRadius = 0.6f;
    }
}
