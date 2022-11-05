package com.nyfaria.eyalphabet.init;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.ability.DragEntityWhereLookingAbility;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.init.HMAAbilityInit;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AbilityInit {
    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(HMAAbilityInit.ABILITIES.getRegistryKey(), EYAlphabet.MODID);

    public static final RegistryObject<Ability> STRANGLE_ABILITY = ABILITIES.register("strangle", DragEntityWhereLookingAbility::new);
}
