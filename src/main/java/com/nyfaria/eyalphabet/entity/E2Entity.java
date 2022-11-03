package com.nyfaria.eyalphabet.entity;

import com.nyfaria.eyalphabet.entity.ai.goal.HostileAlphabetGoal;
import com.nyfaria.eyalphabet.entity.ai.goal.WalkToPairGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class E2Entity extends AlphabetEntity {

    public E2Entity(EntityType<? extends E2Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WalkToPairGoal(this, 1.0F, new BlockPos(0, 0, 0)));
        this.goalSelector.addGoal(2, new HostileAlphabetGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.5F) {
            @Override
            public boolean canUse() {
                return E2Entity.this.isInWaterOrBubble();
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

    private <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        return PlayState.STOP;
    }
}
