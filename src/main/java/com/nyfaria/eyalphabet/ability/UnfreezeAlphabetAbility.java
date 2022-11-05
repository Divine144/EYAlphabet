package com.nyfaria.eyalphabet.ability;

import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.init.AbilityInit;
import com.nyfaria.eyalphabet.init.ItemInit;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.capability.AbilityHolderAttacher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import static com.nyfaria.eyalphabet.ability.FreezeAlphabetAbility.getTarget;

public class UnfreezeAlphabetAbility extends Ability {

    @Override
    public boolean isHiddenAbility() {
        return true;
    }

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        super.executePressed(level, player);
        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (item.is(ItemInit.ALPHABET_WAND.get())) {
            LivingEntity livingEntity = getTarget(player, 25);
            if (livingEntity instanceof AlphabetEntity mob) {
                mob.setShouldFreeze(false);
                FreezeAlphabetAbility.ENTITY.remove(player);
                AbilityHolderAttacher.getAbilityHolder(player).ifPresent(p -> p.removeActiveAbility(AbilityInit.FREEZE_ABILITY.get(), true));
            }
        }
    }
}
