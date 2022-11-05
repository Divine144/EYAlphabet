package com.nyfaria.eyalphabet.cap;

import com.nyfaria.eyalphabet.config.EYAlphabetConfig;
import com.nyfaria.eyalphabet.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.EntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.LevelCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerHolder extends EntityCapability {

    private final Queue<Map<BlockPos, BlockState>> firstQueue;
    private final Queue<Map<BlockPos, BlockState>> secondQueue;
    private int timer;
    private int timerStagger;

    public PlayerHolder(Entity player) {
        super(player);
        firstQueue = new ArrayDeque<>();
        secondQueue = new ArrayDeque<>();
        timer = EYAlphabetConfig.INSTANCE.wallsClosingInTimer.get() * 20;
        timerStagger = timer / 5;
    }

    public @NotNull Queue<Map<BlockPos, BlockState>> getFirstQueue() {
        return this.firstQueue;
    }

    public @NotNull Queue<Map<BlockPos, BlockState>> getSecondQueue() {
        return this.secondQueue;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getTimerStagger() {
        return this.timerStagger;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag nbt = new CompoundTag();
        for (int i = 0; i < this.firstQueue.size(); i++) {
            var polledItem = new ArrayDeque<>(firstQueue).poll();
            var polledItemTwo = new ArrayDeque<>(secondQueue).poll();
            if (polledItem != null) {
                ListTag blockPosList = new ListTag();
                ListTag blockStateList = new ListTag();
                if (polledItem.size() > 0) {
                    polledItem.keySet().forEach(bp -> blockPosList.add(NbtUtils.writeBlockPos(bp)));
                    polledItem.values().forEach(bs -> blockStateList.add(NbtUtils.writeBlockState(bs)));
                    nbt.put("blockPosList" + i, blockPosList);
                    nbt.put("blockStateList" + i, blockStateList);
                }
            }
            if (polledItemTwo != null) {
                ListTag blockPosList = new ListTag();
                ListTag blockStateList = new ListTag();
                if (polledItemTwo.size() > 0) {
                    polledItemTwo.keySet().forEach(bp -> blockPosList.add(NbtUtils.writeBlockPos(bp)));
                    polledItemTwo.values().forEach(bs -> blockStateList.add(NbtUtils.writeBlockState(bs)));
                    nbt.put("blockPosListTwo" + i, blockPosList);
                    nbt.put("blockStateListTwo" + i, blockStateList);
                }
            }
        }
        nbt.putInt("timer", this.timer);
        nbt.putInt("timerStagger", this.timerStagger);
        nbt.putInt("queueSize", this.firstQueue.size());
        System.out.println(firstQueue.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.firstQueue.clear();
        this.secondQueue.clear();
        for (int i = 0; i < nbt.getInt("queueSize"); i++) {
            var blockPosList = nbt.getList("blockPosList" + i, Tag.TAG_LIST);
            var blockStateList = nbt.getList("blockStateList" + i, Tag.TAG_LIST);
            if (!blockPosList.isEmpty()) {
                this.firstQueue.add(getMapFromListTags(blockPosList, blockStateList));
                var blockPosListTwo = nbt.getList("blockPosListTwo" + i, Tag.TAG_LIST);
                var blockStateListTwo = nbt.getList("blockStateListTwo" + i, Tag.TAG_LIST);
                this.secondQueue.add(getMapFromListTags(blockPosListTwo, blockStateListTwo));
            }
        }
        this.timer = nbt.getInt("timer");
        this.timerStagger = nbt.getInt("timerStagger");
        System.out.println(Arrays.toString(firstQueue.toArray()));
        System.out.println(Arrays.toString(secondQueue.toArray()));
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(entity.getId(), PlayerHolderAttacher.PLAYER_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }

    private static Map<BlockPos, BlockState> getMapFromListTags(ListTag a, ListTag b) {
        Map<BlockPos, BlockState> tempMap = new HashMap<>();
        List<BlockPos> tempList = new ArrayList<>();
        List<BlockState> tempListState = new ArrayList<>();
        a.forEach(t -> tempList.add(NbtUtils.readBlockPos((CompoundTag) t)));
        b.forEach(t -> tempListState.add(NbtUtils.readBlockState((CompoundTag) t)));
        Iterator<BlockPos> blockPosIterator = tempList.iterator();
        Iterator<BlockState> blockStateIterator = tempListState.iterator();
        while (blockPosIterator.hasNext() && blockStateIterator.hasNext()) {
            tempMap.put(blockPosIterator.next(), blockStateIterator.next());
        }
        return tempMap;
    }
}
