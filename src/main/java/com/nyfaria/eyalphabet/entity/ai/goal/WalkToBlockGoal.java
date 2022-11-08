package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.NotNull;

public class WalkToBlockGoal extends MoveToBlockGoal {

    private final AlphabetEntity entity;

    public WalkToBlockGoal(PathfinderMob pMob, double pSpeedModifier, BlockPos pos) {
        super(pMob, pSpeedModifier, 0);
        this.blockPos = pos;
        this.entity = (AlphabetEntity) pMob;
    }

    @Override
    protected @NotNull BlockPos getMoveToTarget() {
        return blockPos;
    }

    @Override
    public double acceptedDistance() {
        return 0.1D;
    }

    @Override
    public void tick() {
        if (!blockPos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
            Path path = mob.getNavigation().createPath(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1);
            if (path != null) {
                this.mob.getNavigation().moveTo(path, this.speedModifier);
            }
        }
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
        return this.canUse();
    }
}
