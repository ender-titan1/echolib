package io.github.endertitan.echolib.resoucenetworks.interfaces;

import io.github.endertitan.echolib.ModManager;
import io.github.endertitan.echolib.resoucenetworks.Network;
import io.github.endertitan.echolib.resoucenetworks.enums.SideAccesibility;
import io.github.endertitan.echolib.util.EchoDirection;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public interface INetworkBlock<T extends INetworkMember> {

    default void update(World world, BlockPos pos, String netsign) {

        if (world.isClientSide())
            return;

        T te = (T) world.getBlockEntity(pos);
        assert te != null;

        HashSet<T> entities = new HashSet<>();

        for (EchoDirection dir : EchoDirection.all()) {

            TileEntity target = world.getBlockEntity(dir.toPos(pos));


            if (target instanceof INetworkMember
                    && ((INetworkMember) target).getNetwork().netsign.equals(netsign)
                    && te.getSides(netsign).get(dir) == SideAccesibility.INPUT_OUTPUT
                    && ((INetworkMember) target).getSides(netsign).get(dir.getRelativeBack()) == SideAccesibility.INPUT_OUTPUT)
                entities.add((T) world.getBlockEntity(dir.toPos(pos)));
        }

        if (entities.toArray().length == 0)
            ModManager.MAIN.<T>registerNetwork(te, netsign);
        else {

            boolean flag = true;
            int firstID = ((T)entities.toArray()[0]).getNetwork().id;

            for (int i = 0; i < entities.toArray().length && flag; i++)
                if (((T) entities.toArray()[i]).getNetwork().id != firstID) flag = false;

            if (flag)
                te.setNetwork(((T) entities.toArray()[0]).getNetwork().id);
            else {

                HashSet<Network<T>> networks = new HashSet<Network<T>>();
                for (T ent : entities)
                    networks.add((Network<T>) ent.getNetwork());

                Network<T> main = null;

                int size = 0;
                for (Network<T> net : networks) {
                    if (net.members.toArray().length > size) { size = net.members.toArray().length; main = net; System.out.println(net.id);}
                }

                networks.remove(main);
                te.setNetwork(main.merge(networks).id);
            }
        }
    }

    default void removeUpdate(World world, BlockPos pos, BlockState state, String netsign) {

        if (world.isClientSide)
            return;

        T te = (T) world.getBlockEntity(pos);
        assert te != null;

        te.getNetwork().removeMember(te);

        try {
            te.getNetwork().refresh(world);
        }
        catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            // ignore
        }
    }

}
