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

public class F2Entity extends AlphabetEntity implements ISpecialAlphabet {

    private static final EntityDataAccessor<Boolean> SHOULD_ATTACK_I = SynchedEntityData.defineId(F2Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOULD_JUMPSCARE = SynchedEntityData.defineId(F2Entity.class, EntityDataSerializers.BOOLEAN);

    public F2Entity(EntityType<? extends F2Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULD_ATTACK_I, false);
        this.entityData.define(SHOULD_JUMPSCARE, true);
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
    public void tick() {
        super.tick();
        if (this.tickCount >= 60) {
            this.entityData.set(SHOULD_JUMPSCARE, false);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldFreeze();
            }
        });
        this.goalSelector.addGoal(1, new FEatsIGoal(this));
        this.goalSelector.addGoal(2, new HostileAlphabetGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldFreeze();
            }
        });
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldFreeze();
            }
        });
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldFreeze();
            }
        });
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

    public boolean getShouldJumpscare() {
        return this.entityData.get(SHOULD_JUMPSCARE);
    }

    @Override
    public int getSpecialId() {
        return -3;
    }

    private <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        if (this.getShouldAttackI() && this.getTarget() != null && !this.getShouldFreeze() || (this.getShouldJumpscare() && this.tickCount <= 60)) {
            // Play open mouth animation
        }
        return PlayState.STOP;
    }
}
