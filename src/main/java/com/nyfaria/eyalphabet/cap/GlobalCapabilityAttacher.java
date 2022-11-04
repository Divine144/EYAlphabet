package com.nyfaria.eyalphabet.cap;

import com.nyfaria.eyalphabet.EYAlphabet;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class GlobalCapabilityAttacher extends CapabilityAttacher {

    private static final Class<GlobalCapability> CAPABILITY_CLASS = GlobalCapability.class;
    public static final Capability<GlobalCapability> GLOBAL_LEVEL_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation GLOBAL_LEVEL_CAPABILITY_RL = new ResourceLocation(EYAlphabet.MODID, "global_level_capability");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static GlobalCapability getGlobalLevelCapabilityUnwrap(Level level) {
        return getGlobalLevelCapability(level).orElse(null);
    }

    public static LazyOptional<GlobalCapability> getGlobalLevelCapability(Level level) {
        return level.getCapability(GLOBAL_LEVEL_CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Level> event, Level level) {
        genericAttachCapability(event, new GlobalCapability(level), GLOBAL_LEVEL_CAPABILITY, GLOBAL_LEVEL_CAPABILITY_RL);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerGlobalLevelAttacher(GlobalCapabilityAttacher::attach, GlobalCapabilityAttacher::getGlobalLevelCapability);
        SimpleLevelCapabilityStatusPacket.registerRetriever(GLOBAL_LEVEL_CAPABILITY_RL, GlobalCapabilityAttacher::getGlobalLevelCapabilityUnwrap);
    }
}
