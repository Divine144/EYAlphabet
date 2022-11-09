package com.nyfaria.eyalphabet.ability;

import com.nyfaria.eyalphabet.init.AbilityInit;
import com.nyfaria.eyalphabet.init.ItemInit;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.capability.AbilityHolderAttacher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class UnfreezeAlphabetAbility extends Ability {

    // Random change
    @Override
    public boolean isHiddenAbility() {
        return true;
    }

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (item.is(ItemInit.ALPHABET_WAND.get())) {
            int id = FreezeAlphabetAbility.HELD_ENTITIES.getInt(player.getUUID());
            LivingEntity livingEntity = (LivingEntity) level.getEntity(id);
            if (livingEntity != null) livingEntity.setNoGravity(false);
            FreezeAlphabetAbility.HELD_ENTITIES.remove(player.getUUID());
            AbilityHolderAttacher.getAbilityHolder(player)
                    .filter(p -> p.isAbilityActive(AbilityInit.FREEZE_ABILITY.get()))
                    .ifPresent(p -> p.removeActiveAbility(AbilityInit.FREEZE_ABILITY.get(), true));
        }
    }
}
