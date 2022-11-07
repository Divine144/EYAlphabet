package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.cap.GlobalCapability;
import com.nyfaria.eyalphabet.cap.GlobalCapabilityAttacher;
import com.nyfaria.eyalphabet.entity.WitherStormEntity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;

public class StormSetTargetGoal extends TargetGoal {

    private final WitherStormEntity witherStorm;

    public StormSetTargetGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.witherStorm = (WitherStormEntity) pMob;
    }

    @Override
    public boolean canUse() {
        return this.witherStorm.getTarget() == null;
    }

    @Override
    public void start() {
        super.start();
        var uuid = GlobalCapabilityAttacher.getGlobalLevelCapability(this.witherStorm.level).map(GlobalCapability::getFireLightUUID).orElse(null);
        var entities = Util.getEntitiesInRange(this.witherStorm, Player.class, 20, 20, 20, p -> true).stream().findFirst().orElse(null);
        if (uuid != null && witherStorm.level.getPlayerByUUID(uuid) != null) {
            this.witherStorm.setTargetUUID(uuid);
        }
        else if (entities != null) {
            this.witherStorm.setTargetUUID(entities.getUUID());
        }
    }
}
