package io.github.endertitan.echolib.event;

import io.github.endertitan.echolib.EchoLib;
import io.github.endertitan.echolib.ModManager;
import io.github.endertitan.echolib.resoucenetworks.Network;
import io.github.endertitan.echolib.resoucenetworks.interfaces.INetworkMember;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.HashSet;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = EchoLib.MOD_ID, bus = Bus.FORGE)
public class EventHandler {

    private static final BlockPos ZERO = new BlockPos(0, 0, 0);

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent tickEvent) {
        for (Network<?> network : ModManager.MAIN.registeredNetworks) {

            for (INetworkMember member : network.members) {
                network.positions.add(member.getPosition());
            }

            //fixme: will not work if there actually is a network member on 0, 0, 0
            network.positions.removeIf((pos) -> Objects.equals(pos, ZERO));
        }
    }
}
