package com.nyfaria.eyalphabet.network.packet.clientbound;

import com.nyfaria.eyalphabet.network.ClientNetworkHandler;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record StartJumpscareSoundPacket(int entityId) implements IPacket {
    public StartJumpscareSoundPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, StartJumpscareSoundPacket.class, StartJumpscareSoundPacket::new);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> ClientNetworkHandler.startJumpscareSound(this.entityId));
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeVarInt(this.entityId);
    }
}
