package com.nyfaria.eyalphabet.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class FollowTargetGoal extends TargetGoal {

    public FollowTargetGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
    }

    @Override
    public boolean canUse() {
        return false;
    }
}
