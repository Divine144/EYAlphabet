package com.nyfaria.eyalphabet.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.config.EYAlphabetConfig;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.F2Entity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

@Mod.EventBusSubscriber(modid = EYAlphabet.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("f_eats_i")
                        .then(Commands.argument("relativePlayer", EntityArgument.player())
                            .executes(context -> {
                                var f2EntityList = Util.getEntitiesInRange(EntityArgument.getPlayer(context, "relativePlayer"), F2Entity.class, 30, 30, 30, p -> true);
                                f2EntityList.forEach(p -> p.setShouldAttackI(true));
                                return Command.SINGLE_SUCCESS;
                })));
        dispatcher.register(Commands.literal("alphabets")
                    .then(Commands.literal("hostile")
                            .then(Commands.argument("shouldBeHostile", BoolArgumentType.bool())
                                    .then(Commands.argument("relativePlayer", EntityArgument.player())
                                        .executes(context -> {
                                            int range = EYAlphabetConfig.INSTANCE.allLettersAttackEachOtherRadius.get();
                                            var alphabetList = Util.getEntitiesInRange(EntityArgument.getPlayer(context, "relativePlayer"), AlphabetEntity.class, range, range, range, p -> true);
                                            alphabetList.forEach(p -> p.setShouldBeHostile(BoolArgumentType.getBool(context, "shouldBeHostile")));
                                            return Command.SINGLE_SUCCESS;
                                        }))
                            )
                    )
        );
        dispatcher.register(Commands.literal("giant_pit")
                .then(Commands.argument("relativePlayer", EntityArgument.player())
                        .executes(context -> {
                            Queue<Map<BlockPos, BlockState>> wtf = new ArrayDeque<>(); // One queue for half side another for the other side?
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
