package com.nyfaria.eyalphabet.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Util {
    /**
     * Raytrace the player look vector to return what entity is looked at. Returns null if not found
     */
    public static Entity rayTrace(Level world, Player player, double range) {
        Vec3 pos = player.position();
        Vec3 cam1 = player.getLookAngle();
        Vec3 cam2 = cam1.add(cam1.x * range, cam1.y * range, cam1.z * range);
        AABB aabb = player.getBoundingBox().expandTowards(cam1.scale(range)).inflate(1.0F, 1.0F, 1.0F);
        EntityHitResult ray = findEntity(world, player, pos, cam2, aabb, range);
        if (ray != null && ray.getType() == HitResult.Type.ENTITY) {
            return ray.getEntity() instanceof LivingEntity && !(ray.getEntity() instanceof Player) ? ray.getEntity() : null;
        }
        return null;
    }

    public static Direction findHorizontalDirection(BlockPos pos, Vec3 vector) {
        Vec3 center = Vec3.atCenterOf(pos);
        Vec3 direction = vector.subtract(center);
        boolean eastWest = (Math.abs(direction.x()) > Math.abs(direction.z()));
        if (eastWest) {
            if (direction.x >= 0) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }
        } else {
            if (direction.z >= 0) {
                return Direction.SOUTH;
            } else {
                return Direction.NORTH;
            }
        }
    }

    /**
     * Returns a list of entities (targets) from a relative entity within the specified x, y, and z bounds.
     */
    public static <T extends LivingEntity> List<T> getEntitiesInRange(LivingEntity relativeEntity, Class<T> targets, double xBound, double yBound, double zBound, Predicate<T> filter) {
        return relativeEntity.level.getEntitiesOfClass(targets,
                        new AABB(relativeEntity.getX() - xBound, relativeEntity.getY() - yBound, relativeEntity.getZ() - zBound,
                                relativeEntity.getX() + xBound, relativeEntity.getY() + yBound, relativeEntity.getZ() + zBound))
                .stream().sorted(getEntityComparator(relativeEntity)).filter(filter).collect(Collectors.toList());
    }

    /**
     * Returns a comparator which compares entities' distances to a given LivingEntity
     */
    private static Comparator<Entity> getEntityComparator(LivingEntity other) {
        return Comparator.comparing(entity -> entity.distanceToSqr(other.getX(), other.getY(), other.getZ()));
    }

    /**
     * Raytrace the player look vector to return EntityRayTraceResult
     */
    private static EntityHitResult findEntity(Level world, Player player, Vec3 pos, Vec3 look, AABB aabb, double range) {
        for (Entity entity1 : world.getEntities(player, aabb)) {
            AABB mob = entity1.getBoundingBox().inflate(1.0F);
            if (intersect(pos, look, mob, range)) {
                return new EntityHitResult(entity1);
            }
        }
        return null;
    }

    private static boolean intersect(Vec3 pos, Vec3 look, AABB mob, double range) {
        Vec3 invDir = new Vec3(1f / look.x, 1f / look.y, 1f / look.z);

        boolean signDirX = invDir.x < 0;
        boolean signDirY = invDir.y < 0;
        boolean signDirZ = invDir.z < 0;

        Vec3 max = new Vec3(mob.maxX, mob.maxY, mob.maxZ);
        Vec3 min = new Vec3(mob.minX, mob.minY, mob.minZ);

        Vec3 bBox = signDirX ? max : min;
        double tMin = (bBox.x - pos.x) * invDir.x;
        bBox = signDirX ? min : max;
        double tMax = (bBox.x - pos.x) * invDir.x;
        bBox = signDirY ? max : min;
        double tYMin = (bBox.y - pos.y) * invDir.y;
        bBox = signDirY ? min : max;
        double tYMax = (bBox.y - pos.y) * invDir.y;

        if ((tMin > tYMax) || (tYMin > tMax)) {
            return false;
        }

        if (tYMin > tMin) {
            tMin = tYMin;
        }

        if (tYMax < tMax) {
            tMax = tYMax;
        }

        bBox = signDirZ ? max : min;
        double tZMin = (bBox.z - pos.z) * invDir.z;
        bBox = signDirZ ? min : max;
        double tZMax = (bBox.z - pos.z) * invDir.z;

        if ((tMin > tZMax) || (tZMin > tMax)) {
            return false;
        }
        if (tZMin > tMin) {
            tMin = tZMin;
        }
        if (tZMax < tMax) {
            tMax = tZMax;
        }
        return (tMin < range) && (tMax > 0);
    }
}
