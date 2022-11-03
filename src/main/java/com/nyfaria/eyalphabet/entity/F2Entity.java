package com.nyfaria.eyalphabet.entity;

import com.nyfaria.eyalphabet.entity.ai.goal.FEatsIGoal;
import com.nyfaria.eyalphabet.entity.ai.goal.HostileAlphabetGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class F2Entity extends AlphabetEntity {

    private static final EntityDataAccessor<Boolean> SHOULD_ATTACK_I = SynchedEntityData.defineId(F2Entity.class, EntityDataSerializers.BOOLEAN);

    public F2Entity(EntityType<? extends F2Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULD_ATTACK_I, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.entityData.set(SHOULD_ATTACK_I, nbt.getBoolean("shouldAttack"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        nbt.putBoolean("shouldAttack", this.entityData.get(SHOULD_ATTACK_I));
        super.addAdditionalSaveData(nbt);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FEatsIGoal(this));
        this.goalSelector.addGoal(2, new HostileAlphabetGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 2F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationEvent));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public boolean getShouldAttackI() {
        return this.entityData.get(SHOULD_ATTACK_I);
    }

    public void setShouldAttackI(boolean shouldAttack) {
        this.entityData.set(SHOULD_ATTACK_I, shouldAttack);
    }

    private <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        if (this.getShouldAttackI() && this.getTarget() != null) {

        }
        return PlayState.STOP;
    }
}
