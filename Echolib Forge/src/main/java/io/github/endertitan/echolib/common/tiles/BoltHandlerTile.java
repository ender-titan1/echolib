package io.github.endertitan.echolib.common.tiles;

import io.github.endertitan.echolib.ModManager;
import io.github.endertitan.echolib.resoucenetworks.Connection;
import io.github.endertitan.echolib.resoucenetworks.Network;
import io.github.endertitan.echolib.resoucenetworks.enums.ResourceNetworkMode;
import io.github.endertitan.echolib.resoucenetworks.interfaces.INetworkMember;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BoltHandlerTile extends TileEntity implements INetworkMember {

    public BoltHandlerTile(TileEntityType<?> type) {
        super(type);
    }

    long amount = 0;
    Network<BoltHandlerTile> network;

    @SuppressWarnings("unchecked")
    @Override
    public void setNetwork(int id) {
        this.network = (Network<BoltHandlerTile>) ModManager.MAIN.getNetwork(id);
        this.network.addMember(this);
    }

    @Override
    public BlockPos getPosition() {
        return worldPosition;
    }

    @Override
    public Network<BoltHandlerTile> getNetwork() {
        return network;
    }

    @Override
    public ResourceNetworkMode getNetworkMode() {
        return ResourceNetworkMode.EQUAL;
    }

    @Override
    public long getCapacity() {
        return 1000000;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public Connection[] getConnections() {
        return new Connection[0];
    }

    ////////////////////////////////////

    @Override
    public void add(long amount) {
        network.total += Math.max(0, Math.min(getCapacity(), amount));
        network.reDistribute();
    }

    @Override
    public void remove(long amount) {
        network.total -= Math.max(0, Math.min(getCapacity(), amount));
        network.reDistribute();
    }

    @Override
    public void setAmount(long toAdd) {
        this.amount = toAdd;
    }

    ////////////////////////////////////

    @Override
    public @NotNull CompoundNBT save(CompoundNBT compound) {
        compound.put("network", serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(@NotNull BlockState state, CompoundNBT nbt) {
        deserializeNBT(nbt.getCompound("network"));
        super.load(state, nbt);
    }

    ///////////////////////////////////

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("networkId", getNetwork().id);
        nbt.putLong("amount", amount);

        return nbt;
    }

    @SuppressWarnings("unchecked")
    public void deserializeNBT(CompoundNBT networkNBT) {

        int id = networkNBT.getInt("networkId");
        long amount = networkNBT.getLong("amount");

        if (ModManager.MAIN.getNetwork(id) == null) {
            ModManager.MAIN.registerNetwork(this, "bolt", id);
        }
        else {
            network = (Network<BoltHandlerTile>) ModManager.MAIN.getNetwork(id);
            network.addMember(this);
        }

        network.members.removeIf(mem -> getPosition() == mem.getPosition() && mem != this);
        network.participants.removeIf(mem -> getPosition() == mem.getPosition() && mem != this);

        add(amount);

    }
}
