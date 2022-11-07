package com.nyfaria.eyalphabet.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WitherStormRenderer extends GeoEntityRenderer<WitherStormEntity> {
    public WitherStormRenderer(EntityRendererProvider.Context context) {
        super(context, new SimpleGeoEntityModel<>(EYAlphabet.MODID, "wither_storm"));
        this.shadowRadius = 0.6f;
    }

    @Override
    public void render(WitherStormEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(5, 5, 5);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
