package com.nyfaria.eyalphabet.entity;

import com.nyfaria.eyalphabet.entity.ai.goal.AlphabetSetTargetGoal;
import com.nyfaria.eyalphabet.entity.ai.goal.HostileAlphabetGoal;
import com.nyfaria.eyalphabet.entity.ai.goal.WalkToBlockGoal;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;

public class E2Entity extends AlphabetEntity implements ISpecialAlphabet {

    public E2Entity(EntityType<? extends E2Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !E2Entity.this.getShouldFreeze();
            }
        });
        this.goalSelector.addGoal(1, new WalkToBlockGoal(this, 0.7F, Util.getPosFromConfig(this)));
        this.goalSelector.addGoal(2, new AlphabetSetTargetGoal(this, false));
        this.goalSelector.addGoal(3, new HostileAlphabetGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.5F) {
            @Override
            public boolean canUse() {
                return super.canUse() && E2Entity.this.isInWaterOrBubble() && !E2Entity.this.getShouldFreeze() && !E2Entity.this.getShouldBeHostile();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && this.canUse();
            }
        });
    }

    @Override
    public String getSpecialId() {
        return "e";
    }
}
