package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

public class WalkToPairGoal extends MoveToBlockGoal {

    private final AlphabetEntity entity;

    public WalkToPairGoal(PathfinderMob pMob, double pSpeedModifier, BlockPos pos) {
        super(pMob, pSpeedModifier, 0);
        this.blockPos = pos;
        this.entity = (AlphabetEntity) pMob;
    }

    @Override
    public double acceptedDistance() {
        return 2D;
    }

    @Override
    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        return !this.isReachedTarget();
    }

    @Override
    public boolean canUse() {
        return !this.isReachedTarget() && !this.entity.isInWaterOrBubble() && !this.entity.getShouldBeHostile() && !this.entity.getShouldFreeze();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.isReachedTarget();
    }
}
