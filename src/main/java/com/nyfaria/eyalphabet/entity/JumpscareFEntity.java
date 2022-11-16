package com.nyfaria.eyalphabet.entity;

import com.nyfaria.eyalphabet.entity.ai.goal.JumpscareTargetGoal;
import com.nyfaria.eyalphabet.network.NetworkHandler;
import com.nyfaria.eyalphabet.network.packet.clientbound.StartJumpscareSoundPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.Optional;

public class JumpscareFEntity extends AlphabetEntity implements IAnimatable, ISpecialAlphabet {
    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);
    public boolean tickJumpscareTicks = false;
    public int jumpscareTicks = 0;

    public JumpscareFEntity(EntityType<? extends JumpscareFEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level.isClientSide && this.tickJumpscareTicks && this.jumpscareTicks < 10) {
            this.jumpscareTicks++;
        }
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, new JumpscareTargetGoal(this));
        this.goalSelector.addGoal(0, new JumpscareGoal());
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationEvent));
    }

    protected <T extends IAnimatable> PlayState animationEvent(AnimationEvent<T> event) {
        if (this.tickJumpscareTicks) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public String getSpecialId() {
        return "f";
    }

    class JumpscareGoal extends Goal {
        private Vec3 startPos;
        private int tickCount = 0;
        private LivingEntity target;

        JumpscareGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return JumpscareFEntity.this.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() && this.tickCount < 15;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            JumpscareFEntity.this.noPhysics = true;
            JumpscareFEntity.this.setNoGravity(true);
            this.startPos = JumpscareFEntity.this.position();
            this.tickCount = 0;
            this.target = JumpscareFEntity.this.getTarget();
            Vec3 eyePos = this.target.getEyePosition();
            this.getYRot(eyePos).ifPresent(pYRot -> {
                JumpscareFEntity.this.setYRot(pYRot);
                JumpscareFEntity.this.setYBodyRot(pYRot);
                JumpscareFEntity.this.setYHeadRot(pYRot);
            });
            this.getXRot(eyePos).ifPresent(JumpscareFEntity.this::setXRot);
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> JumpscareFEntity.this), new StartJumpscareSoundPacket(JumpscareFEntity.this.getId()));
        }

        @Override
        public void tick() {
            this.tickCount++;
            if (this.tickCount > 10)
                return;

            double percent = this.tickCount / (0.5D * 20);
            // Go a little in front of them so the F doesn't clip!
            double scale = percent; //Math.max(0, percent - 0.05D);
            Vec3 newPos = this.startPos.add(this.target.getEyePosition().subtract(0, 0.4D, 0).subtract(this.startPos).scale(scale));

            JumpscareFEntity.this.noPhysics = true;
            JumpscareFEntity.this.setPos(newPos);
        }

        @Override
        public void stop() {
            JumpscareFEntity.this.discard();
        }

        private Optional<Float> getXRot(Vec3 lookPos) {
            double d0 = lookPos.x - JumpscareFEntity.this.getX();
            double d1 = lookPos.y - JumpscareFEntity.this.getEyeY();
            double d2 = lookPos.z - JumpscareFEntity.this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            return Math.abs(d1) <= 1.0E-5F && Math.abs(d3) <= 1.0E-5F
                    ? Optional.empty()
                    : Optional.of((float) (-(Mth.atan2(d1, d3) * (180F / (float) Math.PI))));
        }

        private Optional<Float> getYRot(Vec3 lookPos) {
            double d0 = lookPos.x - JumpscareFEntity.this.getX();
            double d1 = lookPos.z - JumpscareFEntity.this.getZ();
            return Math.abs(d1) <= 1.0E-5F && Math.abs(d0) <= 1.0E-5F
                    ? Optional.empty()
                    : Optional.of((float) (Mth.atan2(d1, d0) * (180F / (float) Math.PI)) - 90.0F);
        }
    }
}
