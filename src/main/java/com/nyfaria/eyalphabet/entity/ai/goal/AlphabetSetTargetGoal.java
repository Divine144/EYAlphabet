package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class AlphabetSetTargetGoal extends TargetGoal {

    private final AlphabetEntity entity;

    public AlphabetSetTargetGoal(AlphabetEntity pMob, boolean pMustSee) {
        super(pMob, pMustSee);
        this.entity = pMob;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() == null && this.entity.getShouldBeHostile() && !this.entity.getShouldFreeze();
    }

    @Override
    public void start() {
        var entities = Util.getEntitiesInRange(this.entity, AlphabetEntity.class, 30, 30, 30, i -> i != this.entity);
        if (!entities.isEmpty()) {
            this.entity.setTarget(entities.get(RandomSource.create().nextIntBetweenInclusive(0, entities.size() - 1)));
            super.start();
        }
    }
}
