package io.github.endertitan.echolib;


import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EchoLib.MOD_ID)
public class EchoLib
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID  = "echolib";

    public EchoLib() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.addListener(this::onWorldLoaded);

        MinecraftForge.EVENT_BUS.register(this);
    }


    private void onWorldLoaded(final WorldEvent.Load event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerWorld) {
            ModManager.MAIN.nextNetworkID = 1;
            ModManager.MAIN.registeredNetworks.clear();
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("EchoLib Running");
    }

}
