package io.github.endertitan.echolib.resoucenetworks;

import io.github.endertitan.echolib.ModManager;
import io.github.endertitan.echolib.resoucenetworks.enums.ConnectionMode;
import io.github.endertitan.echolib.resoucenetworks.interfaces.INetworkMember;
import io.github.endertitan.echolib.util.EchoDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Network<T extends INetworkMember> {

    public HashSet<T> members = new HashSet<>();
    public HashSet<T> participants = new HashSet<>();
    public HashSet<BlockPos> positions = new HashSet<>();

    public HashSet<Network<T>> pullNetworks = new HashSet<>();
    public HashSet<Network<T>> pushNetworks = new HashSet<>();

    public int id;
    public long capacity;
    public long total;

    // Energy: bolt
    public String netsign;

    /////////////////////////

    public Network(int id, long capacity, long total, String netsign) {
        this.id = id;
        this.capacity = capacity;
        this.total = total;
        this.netsign = netsign;
    }

    public Network<T> merge(HashSet<Network<T>> networks ) {

        assert networks != null;

        ArrayList<T> toMerge = new ArrayList<T>();
        for(Network<T> net : networks) {
            toMerge.addAll(net.members);
        }

        for(T mem : toMerge) {
            mem.setNetwork(id);
            addMember(mem);
        }

        return this;

    }

    @SuppressWarnings("unchecked")
    public T addMember(INetworkMember member) {

        positions.removeIf(pos -> member.getPosition() == pos);


        if (positions.contains(member.getPosition()))
            return (T)member;

        members.add((T)member);

        positions.add(member.getPosition());
        if (member.getNetworkMode().isParticipant()) {
            participants.add((T)member);
            capacity += member.getCapacity();
        }

        return (T)member;
    }


    @SuppressWarnings("unchecked")
    public void updateConnections(IWorld world, T member) {

        for (Connection con : member.getConnections()) {
            if (!con.other.netsign.equals(this.netsign))
                return;

            if (con.mode == ConnectionMode.PULL)
                pullNetworks.add((Network<T>) con.other);
            else if (con.mode == ConnectionMode.PUSH)
                pushNetworks.add((Network<T>) con.other);
        }
    }

    @SuppressWarnings("unchecked")
    public void removeMember(INetworkMember mem) {

        members.remove((T)mem);
        positions.remove(mem.getPosition());

        if (participants.contains((T)mem)) {
            participants.remove((T)mem);
            capacity -= mem.getCapacity();
        }

    }

    //////////////////////////////////////

    public void refresh(World world) {

        Set<BlockPos> membersFound = new HashSet<>();
        membersFound.addAll(positions);


        do {

            BlockPos pos = Arrays.copyOf(positions.toArray(), positions.toArray().length, BlockPos[].class)[0];

            Set<BlockPos> found = getNeighbours(pos, new HashSet<>());

            found.add(pos);

            membersFound.removeAll(found);

            int targetId = ModManager.MAIN.getNextNetworkID();

            for (BlockPos position : found) {

                INetworkMember te = (INetworkMember) world.getBlockEntity(position);
                assert te != null;

                removeMember(te);

                if (ModManager.MAIN.getNetwork(targetId) == null) {
                    ModManager.MAIN.registerNetwork(te, netsign, targetId);
                }
                else {
                    te.setNetwork(targetId);
                    ModManager.MAIN.getNetwork(targetId).addMember(te);
                }
            }

        } while (membersFound.toArray().length != 0);
    }

    private Set<BlockPos> getNeighbours(BlockPos pos, Set<BlockPos> limit) {

        Set<BlockPos> found = new HashSet<>();

        for (EchoDirection dir : EchoDirection.all()) {
            BlockPos p = dir.toPos(pos);
            if (positions.contains(p) && !limit.contains(p)) {
                found.add(p);
                limit.add(p);
                found.addAll(getNeighbours(p, limit));
            }
        }
        return found;
    }

    //////////////////////////////////////

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public void reDistribute() {

        long forEach = (long) Math.floor(total / participants.toArray().length);

        for (INetworkMember member : participants) {
            member.setAmount(forEach);
        }

    }
}
