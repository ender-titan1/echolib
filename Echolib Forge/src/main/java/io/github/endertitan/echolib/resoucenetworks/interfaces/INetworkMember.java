package io.github.endertitan.echolib.resoucenetworks.interfaces;

import io.github.endertitan.echolib.resoucenetworks.Connection;
import io.github.endertitan.echolib.resoucenetworks.Network;
import io.github.endertitan.echolib.resoucenetworks.enums.ResourceNetworkMode;
import io.github.endertitan.echolib.resoucenetworks.enums.SideAccesibility;
import io.github.endertitan.echolib.util.EchoDirection;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public interface INetworkMember {
    void setNetwork(int id);
    Network<?> getNetwork();
    ResourceNetworkMode getNetworkMode();

    long getCapacity();
    long getAmount();

    Connection[] getConnections();
    BlockPos getPosition();

    void add(long amount);
    void remove(long amount);

    void  setAmount(long amount);

    default HashMap<EchoDirection, SideAccesibility> getSides(String netsign) {
        HashMap<EchoDirection, SideAccesibility> map = new HashMap<EchoDirection, SideAccesibility>();

        if (!netsign.equals("bolt")) {
            map.put(EchoDirection.BOTTOM, SideAccesibility.INPUT);
            map.put(EchoDirection.TOP, SideAccesibility.OUTPUT);
        } else {
            map.put(EchoDirection.BOTTOM, SideAccesibility.INPUT_OUTPUT);
            map.put(EchoDirection.TOP, SideAccesibility.INPUT_OUTPUT);
        }

        map.put(EchoDirection.NORTH, SideAccesibility.INPUT_OUTPUT);
        map.put(EchoDirection.EAST, SideAccesibility.INPUT_OUTPUT);
        map.put(EchoDirection.SOUTH, SideAccesibility.INPUT_OUTPUT);
        map.put(EchoDirection.WEST, SideAccesibility.INPUT_OUTPUT);

        return map;
    }

}
