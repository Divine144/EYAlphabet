package com.nyfaria.eyalphabet.entity;

import com.nyfaria.eyalphabet.entity.ai.goal.AlphabetSetTargetGoal;
import com.nyfaria.eyalphabet.entity.ai.goal.FEatsIGoal;
import com.nyfaria.eyalphabet.entity.ai.goal.HostileAlphabetGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class F2Entity extends AlphabetEntity implements ISpecialAlphabet {
    private static final EntityDataAccessor<Boolean> SHOULD_ATTACK_I = SynchedEntityData.defineId(F2Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(F2Entity.class, EntityDataSerializers.BOOLEAN);

    public F2Entity(EntityType<? extends F2Entity> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULD_ATTACK_I, false);
        this.entityData.define(ATTACKING, false);
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

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
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
        this.goalSelector.addGoal(2, new AlphabetSetTargetGoal(this, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldAttackI();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(3, new HostileAlphabetGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldFreeze() && !F2Entity.this.getShouldBeHostile();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && !F2Entity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
    }

    public boolean getShouldAttackI() {
        return this.entityData.get(SHOULD_ATTACK_I);
    }

    public void setShouldAttackI(boolean shouldAttack) {
        this.entityData.set(SHOULD_ATTACK_I, shouldAttack);
    }

    @Override
    protected <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        if (!this.getShouldFreeze()) {
            if (this.getShouldAttackI() && this.isAttacking()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            }
            // else if (this.getShouldJumpscare()) {
            //     event.getController().setAnimation(new AnimationBuilder().addAnimation("jumpscare", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            // }
            else if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public String getSpecialId() {
        return "f";
    }
}
