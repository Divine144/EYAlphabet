package com.nyfaria.eyalphabet.ability;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.init.ItemInit;
import dev._100media.hundredmediaabilities.ability.Ability;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FreezeAlphabetAbility extends Ability {

    public static final Object2IntMap<Player> ENTITY = new Object2IntOpenHashMap<>();
    private static final Int2FloatMap DISTANCE = new Int2FloatOpenHashMap();

    @Override
    public boolean isHiddenAbility() {
        return true;
    }

    @Override
    public boolean isToggleAbility() {
        return true;
    }

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
        LivingEntity livingEntity = getTarget(player, 25);
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (item.is(ItemInit.ALPHABET_WAND.get())) {
            if (livingEntity instanceof AlphabetEntity mob) {
                if (!ENTITY.values().contains(livingEntity.getId())) {
                    ENTITY.put(player, livingEntity.getId());
                    DISTANCE.put(livingEntity.getId(), player.distanceTo(livingEntity));
                    mob.setShouldFreeze(true);
                }
            }
        }
    }

    @Override
    public void executeHeld(ServerLevel level, ServerPlayer player, int tick) {
        super.executeHeld(level, player, tick);
        int id = ENTITY.getInt(player);
        LivingEntity livingEntity = (LivingEntity) level.getEntity(id);
        float distance = DISTANCE.get(id);
        Vec3 look = player.getLookAngle();
        if (livingEntity != null) {
            Vec3 targetPos = player.position().add(look.x * distance, look.y * distance, look.z * distance);
            List<VoxelShape> blockCollisions = new ArrayList<>();
            AABB aabb = livingEntity.getBoundingBox();
            level.getBlockCollisions(livingEntity, aabb.expandTowards(targetPos)).forEach(blockCollisions::add);
            livingEntity.setPos(collideWithShapes(targetPos, aabb, blockCollisions));
            livingEntity.hurtMarked = true;
            livingEntity.fallDistance = 0;
        }
    }

    private static Vec3 collideWithShapes(Vec3 pDeltaMovement, AABB pEntityBB, List<VoxelShape> pShapes) {
        if (pShapes.isEmpty()) {
            return pDeltaMovement;
        } else {
            double d0 = pDeltaMovement.x;
            double d1 = pDeltaMovement.y;
            double d2 = pDeltaMovement.z;
            if (d1 != 0.0D) {
                d1 = Shapes.collide(Direction.Axis.Y, pEntityBB, pShapes, d1);
                if (d1 != 0.0D) {
                    pEntityBB = pEntityBB.move(0.0D, d1, 0.0D);
                }
            }

            boolean flag = Math.abs(d0) < Math.abs(d2);
            if (flag && d2 != 0.0D) {
                d2 = Shapes.collide(Direction.Axis.Z, pEntityBB, pShapes, d2);
                if (d2 != 0.0D) {
                    pEntityBB = pEntityBB.move(0.0D, 0.0D, d2);
                }
            }

            if (d0 != 0.0D) {
                d0 = Shapes.collide(Direction.Axis.X, pEntityBB, pShapes, d0);
                if (!flag && d0 != 0.0D) {
                    pEntityBB = pEntityBB.move(d0, 0.0D, 0.0D);
                }
            }

            if (!flag && d2 != 0.0D) {
                d2 = Shapes.collide(Direction.Axis.Z, pEntityBB, pShapes, d2);
            }

            return new Vec3(d0, d1, d2);
        }
    }

    public static LivingEntity getTarget(LivingEntity shooter, float range) {
        EntityHitResult ehr = rayTraceEntities(shooter.level, shooter, range, e -> true);
        if (ehr != null) {
            if (ehr.getEntity() instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }
        return null;
    }

    public static EntityHitResult rayTraceEntities(Level level, Entity origin, float range, Predicate<Entity> filter) {
        Vec3 look = origin.getLookAngle();
        Vec3 startVec = origin.getEyePosition();
        Vec3 endVec = startVec.add(look.x * range, look.y * range, look.z * range);
        AABB box = new AABB(startVec, endVec);
        return rayTraceEntities(level, origin, startVec, endVec, box, filter);
    }

    public static EntityHitResult rayTraceEntities(Level level, @Nullable Entity origin, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        for (Entity entity1 : level.getEntities(origin, boundingBox, filter)) {
            if (entity1.isSpectator()) {
                continue;
            }
            AABB aabb = entity1.getBoundingBox().inflate(1.0D);
            Optional<Vec3> optional = aabb.clip(startVec, endVec);
            if (optional.isPresent()) {
                double d1 = startVec.distanceToSqr(optional.get());
                if (d1 < d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity);

    }
}
