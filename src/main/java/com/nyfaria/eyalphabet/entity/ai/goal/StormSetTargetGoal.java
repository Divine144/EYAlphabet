package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.cap.GlobalCapability;
import com.nyfaria.eyalphabet.cap.GlobalCapabilityAttacher;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

public class StormSetTargetGoal extends TargetGoal {

    private final WitherStormEntity witherStorm;

    public StormSetTargetGoal (WitherStormEntity pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.witherStorm = pMob;
    }

    @Override
    public boolean canUse() {
        return this.witherStorm.getTarget() == null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.witherStorm.getTarget() == null;
    }

    @Override
    public void tick() {
        super.tick();
        var targetUUID = GlobalCapabilityAttacher.getGlobalLevelCapability(this.witherStorm.level).map(GlobalCapability::getFireLightUUID).orElse(null);
        if (targetUUID != null) {
            this.witherStorm.setTargetUUID(targetUUID);
            this.witherStorm.setTarget(this.witherStorm.getTarget());
            super.start();
        }
        else {
            Player nearestPlayer = Util.getEntitiesInRange(this.witherStorm, Player.class, 15, 15, 15, p -> true).stream().findFirst().orElse(null);
            if (nearestPlayer != null) {
                this.witherStorm.setTargetUUID(nearestPlayer.getUUID());
                this.witherStorm.setTarget(this.witherStorm.getTarget());
                super.start();
            }
        }
    }
}
