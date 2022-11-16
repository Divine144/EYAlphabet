package com.nyfaria.eyalphabet.util;

import com.nyfaria.eyalphabet.config.EYAlphabetConfig;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.H2Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

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

    public static BlockPos getPosFromConfig(AlphabetEntity entity) {
        BlockPos pairPos = new BlockPos(0, 0, 0);
        Level level = entity.level;
        GlobalPos globalPos = entity instanceof H2Entity ? EYAlphabetConfig.INSTANCE.h2blockPosition.apply(level) : EYAlphabetConfig.INSTANCE.e2blockPosition.apply(level);
        if (globalPos != null && level.getServer() != null) {
            ServerLevel otherDimLevel = level.getServer().getLevel(globalPos.dimension());
            if (otherDimLevel != null) {
                pairPos = globalPos.pos();
            }
        }
        return pairPos;
    }

    /**
     * Returns a comparator which compares entities' distances to a given LivingEntity
     */
    private static Comparator<Entity> getEntityComparator(LivingEntity other) {
        return Comparator.comparing(entity -> entity.distanceToSqr(other.getX(), other.getY(), other.getZ()));
    }
}
