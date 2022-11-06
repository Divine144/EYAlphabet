package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

public class StormFollowTargetGoal extends TargetGoal {

    private final WitherStormEntity entity;

    public StormFollowTargetGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.entity = (WitherStormEntity) pMob;
    }

    @Override
    public void tick() {
        super.tick();
        Player target = this.entity.getTarget();
        if (target != null) {
            this.mob.setTarget(target);
            this.mob.getNavigation().moveTo(target, 1.0F);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
}
