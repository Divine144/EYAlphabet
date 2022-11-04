package com.nyfaria.eyalphabet.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.nyfaria.eyalphabet.EYAlphabet;
import com.nyfaria.eyalphabet.cap.GlobalCapability;
import com.nyfaria.eyalphabet.cap.GlobalCapabilityAttacher;
import com.nyfaria.eyalphabet.config.EYAlphabetConfig;
import com.nyfaria.eyalphabet.entity.AlphabetEntity;
import com.nyfaria.eyalphabet.entity.F2Entity;
import com.nyfaria.eyalphabet.util.Util;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
                .then(Commands.literal("create")
                    .then(Commands.argument("relativePlayer", EntityArgument.player())
                            .executes(context -> {
                                Player player = EntityArgument.getPlayer(context, "relativePlayer");
                                var globalHolder = GlobalCapabilityAttacher.getGlobalLevelCapability(player.level);
                                var globalHolderQueueOne = globalHolder.map(GlobalCapability::getFirstQueue).orElse(new ArrayDeque<>());
                                var globalHolderQueueTwo = globalHolder.map(GlobalCapability::getSecondQueue).orElse(new ArrayDeque<>());

                                Map<BlockPos, BlockState> tempMap = new HashMap<>();
                                BlockPos playerPosition = player.getOnPos().immutable();
                                BlockPos relativePosOne = playerPosition.relative(Direction.SOUTH, 4);
                                BlockPos relativePosTwo = playerPosition.relative(Direction.NORTH, 5);

                                for (int z = relativePosOne.getZ(); z >= playerPosition.getZ(); z--) {
                                    for (int x = relativePosOne.getX() + 4; x >= relativePosOne.getX() - 5; x--) {
                                        for (int y = relativePosOne.getY() - 15; y <= relativePosOne.getY(); y++) {
                                            tempMap.put(new BlockPos(x, y, z), player.level.getBlockState(new BlockPos(x, y, z)));
                                            player.level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                                        }
                                    }
                                    globalHolderQueueOne.add(new HashMap<>(tempMap));
                                    tempMap.clear();
                                }
                                for (int z = relativePosTwo.getZ(); z < playerPosition.getZ(); z++) {
                                    for (int x = relativePosTwo.getX() + 4; x >= relativePosTwo.getX() - 5; x--) {
                                        for (int y = relativePosTwo.getY() - 15; y <= relativePosTwo.getY(); y++) {
                                            tempMap.put(new BlockPos(x, y, z), player.level.getBlockState(new BlockPos(x, y, z)));
                                            player.level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                                        }
                                    }
                                    globalHolderQueueTwo.add(new HashMap<>(tempMap));
                                    tempMap.clear();
                                }
                                return Command.SINGLE_SUCCESS;
                            })
                    ))
        );
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (!level.isClientSide && event.phase == TickEvent.Phase.END) {
            var globalHolder = GlobalCapabilityAttacher.getGlobalLevelCapability(level);
            if (globalHolder.filter(p -> p.getFirstQueue().size() != 0 && p.getSecondQueue().size() != 0).isPresent()) {
                var globalHolderQueueOne = globalHolder.map(GlobalCapability::getFirstQueue).orElse(new ArrayDeque<>());
                var globalHolderQueueTwo = globalHolder.map(GlobalCapability::getSecondQueue).orElse(new ArrayDeque<>());
                int timer = globalHolder.map(GlobalCapability::getTimer).orElse(0);
                int timerStagger = globalHolder.map(GlobalCapability::getTimerStagger).orElse(0);
                if (timer != 0) globalHolder.ifPresent(p -> p.setTimer(p.getTimer() - 1)); // Start timer
                if (timer % timerStagger == 0 && timer != 0) {
                    var onePoll = globalHolderQueueOne.poll();
                    var twoPoll = globalHolderQueueTwo.poll();
                    if (onePoll != null && twoPoll != null) {
                        onePoll.forEach(level::setBlockAndUpdate);
                        twoPoll.forEach(level::setBlockAndUpdate);
                    }
                }
                else if (timer == 0)  {
                    globalHolder.ifPresent(p -> p.setTimer(EYAlphabetConfig.INSTANCE.wallsClosingInTimer.get() * 20)); // Reset timer when done
                }
            }
        }
    }
}
