package com.nyfaria.eyalphabet.entity.ai.goal;

import com.nyfaria.eyalphabet.config.EYAlphabetConfig;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.F2Entity;
import com.nyfaria.hmutility.HMUtility;
import com.nyfaria.hmutility.utils.HMUEntityUtils;
import com.nyfaria.hmutility.utils.HMUVectorUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class HostileAlphabetGoal extends MeleeAttackGoal {

    private final AlphabetEntity mob;

    public HostileAlphabetGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.mob = (AlphabetEntity) pMob;
    }

    @Override
    public boolean canUse() {
        return (this.mob.getShouldBeHostile() || (this.mob.getShouldBeHostile() && this.mob instanceof F2Entity entity && !entity.getShouldAttackI())) && !this.mob.getShouldFreeze();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        if (this.mob.level instanceof ServerLevel level && pEnemy instanceof AlphabetEntity && pEnemy != this.mob) {
            RandomSource source = this.mob.getRandom();
            int explosionChance = (int) (double) EYAlphabetConfig.INSTANCE.hostileLettersExplosionChance.get();
            int fireChargeChance = (int) (double) EYAlphabetConfig.INSTANCE.hostileLettersFireChargeChance.get();
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
                if (source.nextIntBetweenInclusive(0, 100) == explosionChance) {
                    level.explode(mob, mob.getX(), mob.getY(), mob.getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
                }
                else if (source.nextIntBetweenInclusive(0, 100) == fireChargeChance) {
                    Vec3 vec3 = this.mob.getViewVector(1.0F);
                    double d2 = this.mob.getX() - (this.mob.getX() + vec3.x * 2.0D);
                    double d3 = this.mob.getY(0.5D) - (0.5D + this.mob.getY());
                    double d4 = this.mob.getZ() - (this.mob.getZ() + vec3.z * 2.0D);
                    LargeFireball largefireball = new LargeFireball(level, this.mob, d2, d3, d4, 1);
                    largefireball.setPos(this.mob.getX() + vec3.x * 2.0D, this.mob.getY() + 0.5D, largefireball.getZ() + vec3.z * 2.0D);
                    level.addFreshEntity(largefireball);
                }
                else {
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(pEnemy);
                }
                this.resetAttackCooldown();
            }
        }
    }

    int timer = 0;
    @Override
    public void tick() {
        super.tick();
        this.mob.setAggressive(this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);
        timer++;
        if (timer >= EYAlphabetConfig.INSTANCE.allLettersAttackEachOtherTimer.get() * 20) {
            this.mob.setShouldBeHostile(false);
            timer = 0;
        }
    }
}
