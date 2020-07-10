package chromakey.devsdream;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import chromakey.devsdream.command.impl.AdvancedEffectCommand;
import chromakey.devsdream.command.impl.AirCommand;
import chromakey.devsdream.command.impl.DamageCommand;
import chromakey.devsdream.command.impl.ExhaustCommand;
import chromakey.devsdream.command.impl.FeedCommand;
import chromakey.devsdream.command.impl.HealthCommand;
import chromakey.devsdream.command.impl.IgniteCommand;
import chromakey.devsdream.command.impl.DamageItemCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("devsdream")

public class Main {

    private static final Logger logger = LogManager.getLogger("devsdream");
    public static Main instance;
    public static final String modid = "devsdream";
    public Main() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
      logger.info("Common setup complete");
    }

    private void clientRegistries(final FMLClientSetupEvent event) {
      logger.info("Successfully set up clientRegistries");
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
      logger.info("Successfully set up server start");
      AdvancedEffectCommand.register(event.getCommandDispatcher());
      HealthCommand.register(event.getCommandDispatcher());
      DamageCommand.register(event.getCommandDispatcher());
      FeedCommand.register(event.getCommandDispatcher());
      ExhaustCommand.register(event.getCommandDispatcher());
      AirCommand.register(event.getCommandDispatcher());
      IgniteCommand.register(event.getCommandDispatcher());
      DamageItemCommand.register(event.getCommandDispatcher());
    }
}