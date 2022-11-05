package com.nyfaria.eyalphabet.ability;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.init.ItemInit;
import com.nyfaria.hmutility.utils.HMUVectorUtils;
import dev._100media.hundredmediaabilities.ability.Ability;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

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
        // Just need to add "dont go inside block" logic, idk how to do that
        if (livingEntity != null) {
            livingEntity.setPos(player.position().add(look.x * distance, look.y * distance, look.z * distance));
            livingEntity.hurtMarked = true;
            livingEntity.fallDistance = 0;
        }
    }

    public static LivingEntity getTarget(LivingEntity shooter, float range) {
        EntityHitResult ehr = HMUVectorUtils.rayTraceEntities(shooter.level, shooter, range, e -> true);
        if (ehr != null) {
            if(ehr.getEntity() instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }
        return null;
    }
}
