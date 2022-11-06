package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.cap.GlobalCapability;
import com.nyfaria.eyalphabet.cap.GlobalCapabilityAttacher;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;

public class FollowTargetGoal extends TargetGoal {

    private final WitherStormEntity entity;

    public FollowTargetGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.entity = (WitherStormEntity) pMob;
    }

    @Override
    public void tick() {
        super.tick();
        this.entity.setTargetUUID(GlobalCapabilityAttacher.getGlobalLevelCapability(this.entity.level).map(GlobalCapability::getFireLightUUID).orElse(null));
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
        return true;
    }
}
