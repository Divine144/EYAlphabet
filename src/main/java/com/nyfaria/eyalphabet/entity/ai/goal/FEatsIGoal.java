package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.F2Entity;
import com.nyfaria.eyalphabet.entity.ISpecialAlphabet;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class FEatsIGoal extends TargetGoal {

    private final F2Entity entity;

    public FEatsIGoal(Mob pMob) {
        super(pMob, false);
        this.entity = (F2Entity) pMob;
    }

    // Timer to sync "I" death with "F" open mouth animation timer
    int timer = 0;

    @Override
    public void tick() {
        super.tick();
        if (this.entity.getTarget() != null) {
            timer++;
            if (timer % 30 == 0) {
                this.entity.setAttacking(false);
                this.entity.getTarget().kill();
                timer = 0;
            }
        }
        else this.entity.setShouldAttackI(false);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        super.start();
        var entitiesList = Util.getEntitiesInRange(this.entity, AlphabetEntity.class, 3, 3, 3, p -> !(p instanceof ISpecialAlphabet));
        for (AlphabetEntity e : entitiesList) {
            if ("i".equals(e.getLetterId())) {
                this.entity.setTarget(e);
                this.entity.setAttacking(true);
                break;
            }
        }
    }

    @Override
    public boolean canUse() {
        return this.entity.getShouldAttackI() && !this.entity.getShouldFreeze();
    }
}
