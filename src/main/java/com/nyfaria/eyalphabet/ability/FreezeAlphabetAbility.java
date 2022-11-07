package com.nyfaria.eyalphabet.ability;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.init.ItemInit;
import dev._100media.hundredmediaabilities.ability.Ability;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class FreezeAlphabetAbility extends Ability {
    public static final Object2IntMap<UUID> HELD_ENTITIES = new Object2IntOpenHashMap<>();
    private static final Int2DoubleMap DISTANCE = new Int2DoubleOpenHashMap();

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
                if (!HELD_ENTITIES.containsValue(livingEntity.getId())) {
                    HELD_ENTITIES.put(player.getUUID(), livingEntity.getId());
                    DISTANCE.put(livingEntity.getId(), player.getEyePosition().distanceTo(livingEntity.getEyePosition()));
                    mob.setShouldFreeze(true);
                    mob.setNoGravity(true);
                }
            }
        }
    }

    @Override
    public void executeHeld(ServerLevel level, ServerPlayer player, int tick) {
        super.executeHeld(level, player, tick);
        int id = HELD_ENTITIES.getInt(player.getUUID());
        LivingEntity livingEntity = (LivingEntity) level.getEntity(id);
        if (livingEntity != null) {
            Vec3 lookVec = player.getLookAngle();
            Vec3 targetPos = player.getEyePosition().add(lookVec.scale(DISTANCE.get(id))).subtract(0, 0.3D, 0);
            livingEntity.move(MoverType.SELF, targetPos.subtract(livingEntity.position()));
            livingEntity.hurtMarked = true;
            livingEntity.fallDistance = 0;
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
