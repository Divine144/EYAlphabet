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

    public static String getLetterFromID(int id) {
        return switch (id) {
            case 1 -> "a"; case 2 -> "c"; case 3 -> "e"; case 4 -> "f";
            case 5 -> "g"; case 6 -> "h"; case 7 -> "m"; case 8 -> "n";
            case 9 -> "o"; case 10 -> "p"; case 11 -> "r"; case 12 -> "s";
            case 13 -> "t"; case 14 -> "u"; case 15 -> "w"; case 16 -> "i";
            default -> "y";
        };
    }
}
