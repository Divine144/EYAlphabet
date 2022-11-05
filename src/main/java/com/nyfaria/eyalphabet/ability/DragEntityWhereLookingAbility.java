package com.nyfaria.eyalphabet.ability;

import com.nyfaria.hmutility.utils.HMUVectorUtils;
import dev._100media.hundredmediaabilities.ability.Ability;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

import java.util.Optional;

public class DragEntityWhereLookingAbility extends Ability {
    private static final Object2IntMap<Player> ENTITY = new Object2IntOpenHashMap<>();
    private static final Int2FloatMap DISTANCE = new Int2FloatOpenHashMap();

    @Override
    public boolean isHeldAbility() {
        return true;
    }

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
        LivingEntity livingEntity = getTarget(player, 25);
        if (livingEntity != null) {
            if (!ENTITY.values().contains(livingEntity.getId())) {
                ENTITY.put(player, livingEntity.getId());
                DISTANCE.put(livingEntity.getId(), player.distanceTo(livingEntity));
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
            Vec3 vec = player.position().add(look.x * distance, look.y * distance, look.z * distance);
            AABB aabb = livingEntity.getBoundingBox();
            System.out.println(vec);
            System.out.println(level.noCollision(livingEntity, aabb.expandTowards(vec)));



            livingEntity.absMoveTo(vec.x, vec.y, vec.z);

            livingEntity.fallDistance = 0;
        }
    }

    @Override
    public void executeReleased(ServerLevel level, ServerPlayer player) {
        super.executeReleased(level, player);
        int id = ENTITY.getInt(player);
        LivingEntity livingEntity = (LivingEntity) level.getEntity(id);
        if (livingEntity != null) {
            livingEntity.fallDistance = 0;
        }
        ENTITY.remove(player);
    }
    public static LivingEntity getTarget(LivingEntity shooter, float range) {
        EntityHitResult ehr = HMUVectorUtils.rayTraceEntities(shooter.level,shooter,range,(e) -> true);
        if (ehr != null) {
            if(ehr.getEntity() instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }
        return null;
    }
}
