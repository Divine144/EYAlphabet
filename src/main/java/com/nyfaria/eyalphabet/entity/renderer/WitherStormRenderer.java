package com.nyfaria.eyalphabet.entity.renderer;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WitherStormRenderer extends GeoEntityRenderer<WitherStormEntity> {
    public WitherStormRenderer(EntityRendererProvider.Context context) {
        super(context, new SimpleGeoEntityModel<>(EYAlphabet.MODID, "wither_storm"));
        this.shadowRadius = 0.6f;
    }
}
