package com.nyfaria.eyalphabet.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.model.AlphabetEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class AlphabetEntityRenderer<T extends AlphabetEntity> extends GeoEntityRenderer<T> {

    public AlphabetEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AlphabetEntityModel<>());
        this.shadowRadius = 0.6f;
    }

    @Override
    public void render(T animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        switch (animatable.getLetterId()) {
            case "c", "m", "w", "y" -> poseStack.scale(1.1F, 1.6F, 1.1F);
            case "u" -> poseStack.scale(1.15F, 1.15F, 1.15F);
            case "o" -> poseStack.scale(1.2F, 1.1F, 1.2F);
            case "n" -> poseStack.scale(1.25F, 1.25F, 1.25F);
        }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
