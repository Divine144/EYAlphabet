package com.nyfaria.eyalphabet.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.F2Entity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EYAlphabet.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("f_eats_i")
                        .then(Commands.argument("relativePlayer", EntityArgument.player())
                            .executes(context -> {
                                var f2EntityList = Util.getEntitiesInRange(EntityArgument.getPlayer(context, "relativePlayer"), F2Entity.class, 30, 30, 30, p -> true);
                                f2EntityList.forEach(p -> p.setShouldAttack(true));
                                return Command.SINGLE_SUCCESS;
                })));

    }
}
