package com.nyfaria.eyalphabet.entity;

import com.nyfaria.eyalphabet.entity.ai.goal.AlphabetSetTargetGoal;
import com.nyfaria.eyalphabet.entity.ai.goal.HostileAlphabetGoal;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

import java.util.List;
import java.util.Locale;

public class AlphabetEntity extends PathfinderMob implements IAnimatable, IAlphabetHolder {
    private static final EntityDataAccessor<String> LETTER_ID = SynchedEntityData.defineId(AlphabetEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> SHOULD_BE_HOSTILE = SynchedEntityData.defineId(AlphabetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOULD_FREEZE = SynchedEntityData.defineId(AlphabetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final List<String> IDS = List.of("a", "c", "e", "f", "g", "h", "m", "n", "o", "p", "r", "s", "t", "u", "w", "i", "y");

    protected final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public AlphabetEntity(EntityType<? extends AlphabetEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LETTER_ID, "a");
        this.entityData.define(SHOULD_BE_HOSTILE, false);
        this.entityData.define(SHOULD_FREEZE, false);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        if (this instanceof ISpecialAlphabet specialAlphabet) {
            this.setLetterId(specialAlphabet.getSpecialId());
        } else {
            this.setLetterId(Util.getRandom(IDS, level.getRandom()));
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Letter", Tag.TAG_STRING)) {
            String letter = nbt.getString("Letter").toLowerCase(Locale.ROOT);
            this.entityData.set(LETTER_ID, IDS.contains(letter) ? letter : "a");
        } else {
            this.entityData.set(LETTER_ID, "a");
        }
        this.entityData.set(SHOULD_BE_HOSTILE, nbt.getBoolean("Hostile"));
        this.entityData.set(SHOULD_FREEZE, nbt.getBoolean("Freeze"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        nbt.putString("Letter", this.getLetterId());
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
        this.goalSelector.addGoal(1, new AlphabetSetTargetGoal(this, false));
        this.goalSelector.addGoal(2, new HostileAlphabetGoal(this, 1.0D, false));

        this.goalSelector.addGoal(3, new PanicGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldBeHostile() && !AlphabetEntity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldFreeze() && !AlphabetEntity.this.getShouldBeHostile();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public boolean canUse() {
                return super.canUse() && !AlphabetEntity.this.getShouldFreeze();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this) {
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

    @Override
    public String getLetterId() {
        return this.entityData.get(LETTER_ID);
    }

    public void setLetterId(String id) {
        this.entityData.set(LETTER_ID, id.toLowerCase(Locale.ROOT));
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
