package com.nyfaria.eyalphabet.config;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.function.Function;

public class EYAlphabetConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final EYAlphabetConfig INSTANCE;
    static {
        Pair<EYAlphabetConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(EYAlphabetConfig::new);
        CONFIG_SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public ForgeConfigSpec.IntValue allLettersAttackEachOtherRadius;
    public ForgeConfigSpec.IntValue allLettersAttackEachOtherTimer;
    public ForgeConfigSpec.DoubleValue hostileLettersExplosionChance;
    public ForgeConfigSpec.DoubleValue hostileLettersFireChargeChance;
    public final Function<Level, GlobalPos> h2AndE2blockPosition;

    private EYAlphabetConfig(ForgeConfigSpec.Builder builder) {
        //                    GlobalPos redstonePos = EYBuildIRLConfig.INSTANCE.miningCompleteRedstoneSpawnPos.apply(player.level);
        //                    if (redstonePos != null && player.level.getServer() != null) {
        //                        ServerLevel otherDimLevel = player.level.getServer().getLevel(redstonePos.dimension());
        //                        if (otherDimLevel != null)
        //                            otherDimLevel.setBlockAndUpdate(redstonePos.pos(), Blocks.REDSTONE_BLOCK.defaultBlockState());
        //                    }
        builder.push("EYAlphabet Config");
        h2AndE2blockPosition = createBlockPosEntry(builder, "H2 and E2 Block Position");
        allLettersAttackEachOtherRadius = builder.comment("Radius that all letters start attacking each other for").defineInRange("attackRadius", 30, 5, 1000);
        allLettersAttackEachOtherTimer = builder.comment("Time (in seconds) before all letters that are hostile turn non-hostile").defineInRange("hostileTimer", 80, 1, 1000);
        hostileLettersExplosionChance = builder.comment("Chance on each attack for a hostile letter to cause an explosion").defineInRange("hostileExplosionChance", 10.0, 0.0, 100.0);
        hostileLettersFireChargeChance = builder.comment("Chance on each attack for a hostile letter to shoot a fire charge").defineInRange("hostileFireChargeChance", 10.0, 0.0, 100.0);
        builder.pop();
    }

    private static Function<Level, GlobalPos> createBlockPosEntry(ForgeConfigSpec.Builder builder, String name) {
        ForgeConfigSpec.ConfigValue<String> posValue = builder
                .comment("Block position in the form \"X,Y,Z\" of the block position H2 and E2 should start walking to")
                .define(name + "BlockPos", "none", EYAlphabetConfig::validateBlockPos);
        ForgeConfigSpec.ConfigValue<String> dimensionValue = builder
                .comment("Dimension of the block position H2 and E2 should start walking to")
                .define(name + "Dimension", Level.OVERWORLD.location().toString(),  EYAlphabetConfig::validateDimension);
        return level -> getGlobalPos(level, posValue, dimensionValue);
    }

    private static boolean validateDimension(Object obj) {
        return obj instanceof String str && ResourceLocation.isValidResourceLocation(str);
    }

    private static boolean validateBlockPos(Object obj) {
        if (!(obj instanceof String str))
            return false;

        if (str.equalsIgnoreCase("none"))
            return true;

        String[] parts = str.split(",");
        if (parts.length != 3)
            return false;

        for (int i = 0; i < parts.length; i++) {
            try {
                Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static GlobalPos getGlobalPos(Level level, ForgeConfigSpec.ConfigValue<String> posValue, ForgeConfigSpec.ConfigValue<String> dimensionValue) {
        String str = posValue.get();
        if (str.equalsIgnoreCase("none"))
            return null;

        ResourceLocation dimLoc = ResourceLocation.tryParse(dimensionValue.get());
        if (dimLoc == null)
            return null;

        ResourceKey<Level> dimKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimLoc);
        if (level.getServer() == null || !level.getServer().levelKeys().contains(dimKey))
            return null;

        String[] parts = str.split(",");
        try {
            return GlobalPos.of(dimKey, new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        } catch (NumberFormatException e) {
            return null;
        }
    }

}