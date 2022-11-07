package com.nyfaria.eyalphabet.network;

import com.nyfaria.eyalphabet.entity.JumpscareFEntity;
import com.nyfaria.eyalphabet.sound.JumpscareSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;

public class ClientNetworkHandler {
    public static void startJumpscareSound(int entityId) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;

        if (level.getEntity(entityId) instanceof LivingEntity livingEntity) {
            livingEntity.noPhysics = true;
            if (livingEntity instanceof JumpscareFEntity jumpscareFEntity)
                jumpscareFEntity.tickJumpscareTicks = true;
            Minecraft.getInstance().getSoundManager().play(new JumpscareSoundInstance(livingEntity));
        }
    }
}
