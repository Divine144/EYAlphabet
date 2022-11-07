package com.nyfaria.eyalphabet.sound;

import com.nyfaria.eyalphabet.init.SoundInit;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class JumpscareSoundInstance extends AbstractTickableSoundInstance {
    private final LivingEntity entity;

    public JumpscareSoundInstance(LivingEntity entity) {
        super(SoundInit.JUMPSCARE.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.looping = false;
    }

    @Override
    public void tick() {
        if (this.entity.isRemoved())
            return;

        this.x = this.entity.getX();
        this.y = this.entity.getY();
        this.z = this.entity.getZ();
    }
}
