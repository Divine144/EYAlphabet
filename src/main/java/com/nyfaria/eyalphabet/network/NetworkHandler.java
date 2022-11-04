package com.nyfaria.eyalphabet.network;

import com.google.common.collect.ImmutableList;
import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.cap.ExampleHolderAttacher;
import com.nyfaria.eyalphabet.cap.GlobalCapabilityAttacher;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EYAlphabet.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;

    public static void register() {
        List<BiConsumer<SimpleChannel, Integer>> packets = ImmutableList.<BiConsumer<SimpleChannel, Integer>>builder()
                .add(SimpleEntityCapabilityStatusPacket::register)
                .add(SimpleLevelCapabilityStatusPacket::register)
                .build();

        packets.forEach(consumer -> consumer.accept(INSTANCE, getNextId()));
    }

    private static int getNextId() {
        return nextId++;
    }
}