package com.nyfaria.eyalphabet.entity;

import com.nyfaria.eyalphabet.entity.ai.goal.HostileAlphabetGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class AlphabetEntity extends PathfinderMob implements IAnimatable {

    private static final EntityDataAccessor<Integer> LETTER_ID = SynchedEntityData.defineId(AlphabetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHOULD_BE_HOSTILE = SynchedEntityData.defineId(AlphabetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOULD_FREEZE = SynchedEntityData.defineId(AlphabetEntity.class, EntityDataSerializers.BOOLEAN);

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public AlphabetEntity(EntityType<? extends AlphabetEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        int y = this.random.nextIntBetweenInclusive(0, 16);
        System.out.println(y);
        this.entityData.define(LETTER_ID, this instanceof ISpecialAlphabet special ? special.getSpecialID() : y);
        this.entityData.define(SHOULD_BE_HOSTILE, false);
        this.entityData.define(SHOULD_FREEZE, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.entityData.set(LETTER_ID, nbt.getInt("Letter"));
        this.entityData.set(SHOULD_BE_HOSTILE, nbt.getBoolean("Hostile"));
        this.entityData.set(SHOULD_FREEZE, nbt.getBoolean("Freeze"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        nbt.putInt("Letter", this.entityData.get(LETTER_ID));
        nbt.putBoolean("Hostile", this.entityData.get(SHOULD_BE_HOSTILE));
        nbt.putBoolean("Freeze", this.entityData.get(SHOULD_FREEZE));
        super.addAdditionalSaveData(nbt);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldFreeze();
            }
        });
        this.goalSelector.addGoal(1, new HostileAlphabetGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new PanicGoal(this, 2.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldBeHostile() && !AlphabetEntity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
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

    public int getLetterID() {
        return this.entityData.get(LETTER_ID);
    }

    public boolean getShouldBeHostile() {
        return this.entityData.get(SHOULD_BE_HOSTILE);
    }

    public void setShouldBeHostile(boolean shouldBeHostile) {
        this.entityData.set(SHOULD_BE_HOSTILE, shouldBeHostile);
    }

    public boolean getShouldFreeze() {
        return this.entityData.get(SHOULD_FREEZE);
    }

    public void setShouldFreeze(boolean shouldFreeze) {
        this.entityData.set(SHOULD_FREEZE, shouldFreeze);
    }

    protected <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        if (!this.getShouldFreeze()) {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
            }
            else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
            }
        }
        return PlayState.CONTINUE;
    }
}
