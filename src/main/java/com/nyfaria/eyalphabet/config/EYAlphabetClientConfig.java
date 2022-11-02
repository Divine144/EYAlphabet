package com.nyfaria.eyalphabet.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class EYAlphabetClientConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final EYAlphabetClientConfig CLIENT;

    static {
        Pair<EYAlphabetClientConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(EYAlphabetClientConfig::new);
        CLIENT_SPEC = pair.getRight();
        CLIENT = pair.getLeft();
    }

    public ForgeConfigSpec.BooleanValue example;

    public EYAlphabetClientConfig(ForgeConfigSpec.Builder builder) {
        example = builder.define("example", true);
    }
}

