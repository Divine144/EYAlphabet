package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.cap.GlobalCapability;
import com.nyfaria.eyalphabet.cap.GlobalCapabilityAttacher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class JumpscareTargetGoal extends NearestAttackableTargetGoal<Player> {
    private UUID firelightId;

    public JumpscareTargetGoal(Mob mob) {
        super(mob, Player.class, false);
        this.targetConditions = TargetingConditions.forNonCombat().range(this.getFollowDistance()).selector(this::checkTarget);
    }

    protected boolean checkTarget(LivingEntity livingEntity) {
        return this.firelightId == null || livingEntity.getUUID().equals(this.firelightId);
    }

    @Override
    protected void findTarget() {
        this.firelightId = GlobalCapabilityAttacher.getGlobalLevelCapability(this.mob.level).map(GlobalCapability::getFireLightUUID).orElse(null);
        super.findTarget();
    }
}
