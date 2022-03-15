package io.github.endertitan.echolib;

import io.github.endertitan.echolib.resoucenetworks.Network;
import io.github.endertitan.echolib.resoucenetworks.interfaces.INetworkMember;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;

import java.util.HashSet;

public class ModManager {
    public static final ModManager MAIN = new ModManager();

    public HashSet<Network<?>> registeredNetworks = new HashSet<Network<?>>();
    public int nextNetworkID = 1;


    public int getNextNetworkID() {
        int id = nextNetworkID;
        nextNetworkID++;
        return id;
    }

    public < T extends INetworkMember > Network<T> registerNetwork(T origin, String netsign) {
        int id = getNextNetworkID();
        Network<T> net = new Network<T>(id, origin.getCapacity(), origin.getAmount(), netsign);
        registeredNetworks.add(net);
        origin.setNetwork(id);
        return net;
    }

    public < T extends INetworkMember > Network<T> registerNetwork(T origin, String netsign, int id) {
        Network<T> net = new Network<T>(id, origin.getCapacity(), origin.getAmount(), netsign);
        registeredNetworks.add(net);
        origin.setNetwork(id);

        if (id > nextNetworkID)
            nextNetworkID = id;
        else if (id == nextNetworkID)
            nextNetworkID = id + 1;

        return net;
    }

    public Network<?> getNetwork(int id) {
        for (Network<?> net : registeredNetworks) {
            if (net.id == id) return net;
        }
        return null;
    }

}
