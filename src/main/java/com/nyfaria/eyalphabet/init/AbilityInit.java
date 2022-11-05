package com.nyfaria.eyalphabet.init;

import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.ability.FreezeAlphabetAbility;
import com.nyfaria.eyalphabet.ability.UnfreezeAlphabetAbility;
import dev._100media.hundredmediaabilities.ability.Ability;
import dev._100media.hundredmediaabilities.init.HMAAbilityInit;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AbilityInit {
    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(HMAAbilityInit.ABILITIES.getRegistryKey(), EYAlphabet.MODID);

    public static final RegistryObject<Ability> FREEZE_ABILITY = ABILITIES.register("freeze", FreezeAlphabetAbility::new);
    public static final RegistryObject<Ability> UNFREEZE_ABILITY = ABILITIES.register("unfreeze", UnfreezeAlphabetAbility::new);

}
