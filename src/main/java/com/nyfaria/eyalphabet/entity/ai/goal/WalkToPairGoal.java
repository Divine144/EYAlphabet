package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

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
    protected void moveMobToBlock() {
        BlockPos target = getMoveToTarget();
        this.entity.getNavigation().moveTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, speedModifier);
        this.entity.getLookControl().setLookAt(Vec3.atCenterOf(target));
    }

    @Override
    protected @NotNull BlockPos getMoveToTarget() {
        Direction direction = Util.findHorizontalDirection(blockPos, this.entity.position());
        return blockPos.relative(direction);
    }

    @Override
    public boolean canUse() {
        return !this.isReachedTarget() && !this.entity.isInWaterOrBubble() && !this.entity.getShouldBeHostile() && !this.entity.getShouldFreeze();
    }
}
