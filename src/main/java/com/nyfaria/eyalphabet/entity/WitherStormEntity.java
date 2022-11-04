package com.nyfaria.eyalphabet.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WitherStormEntity extends Mob {

    private static final EntityDataAccessor<Optional<UUID>> TARGET_UUID = SynchedEntityData.defineId(WitherStormEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public WitherStormEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.set(TARGET_UUID, Optional.of(UUID.fromString("c4eb26e9-a49b-4d7b-93c0-10d7b314bea6")));
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.getTargetUUID() != null) {
            pCompound.putUUID("Target", this.getTargetUUID());
        }
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Target")) {
            this.entityData.set(TARGET_UUID, Optional.of(pCompound.getUUID("Target")));
        }
        super.readAdditionalSaveData(pCompound);
    }

    @Nullable
    public UUID getTargetUUID() {
        return this.entityData.get(TARGET_UUID).orElse(null);
    }
}
