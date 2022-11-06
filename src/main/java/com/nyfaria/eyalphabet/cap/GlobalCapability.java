package com.nyfaria.eyalphabet.cap;

import com.nyfaria.eyalphabet.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.GlobalLevelCapability;
import dev._100media.capabilitysyncer.network.LevelCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GlobalCapability extends GlobalLevelCapability {

    private UUID fireLightUUID;

    public GlobalCapability(Level level) {
        super(level);
        this.fireLightUUID = UUID.fromString("c4eb26e9-a49b-4d7b-93c0-10d7b314bea6");
    }

    public @NotNull UUID getFireLightUUID() {
        return this.fireLightUUID;
    }

    public void setFireLightUUID(UUID newUUID) {
        this.fireLightUUID = newUUID;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("uuid", this.fireLightUUID);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.fireLightUUID = nbt.getUUID("uuid");
    }

    @Override
    public LevelCapabilityStatusPacket createUpdatePacket() {
        return new SimpleLevelCapabilityStatusPacket(GlobalCapabilityAttacher.GLOBAL_LEVEL_CAPABILITY_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }
}
