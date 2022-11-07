package com.nyfaria.eyalphabet.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.eyalphabet.entity.JumpscareFEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class JumpscareFEntityRenderer extends AlphabetEntityRenderer<JumpscareFEntity> {
    public JumpscareFEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(JumpscareFEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float scale = 1 + 1.5F * (entity.jumpscareTicks / 10.0F);
        poseStack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
