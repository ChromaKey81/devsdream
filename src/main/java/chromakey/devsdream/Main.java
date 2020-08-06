package chromakey.devsdream;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import chromakey.devsdream.command.impl.AdvancedEffectCommand;
import chromakey.devsdream.command.impl.AirCommand;
import chromakey.devsdream.command.impl.DamageCommand;
import chromakey.devsdream.command.impl.ExhaustCommand;
import chromakey.devsdream.command.impl.FeedCommand;
import chromakey.devsdream.command.impl.HealthCommand;
import chromakey.devsdream.command.impl.IgniteCommand;
import chromakey.devsdream.command.impl.RandomNumberCommand;
import chromakey.devsdream.util.JSONHelper;
import chromakey.devsdream.command.impl.DamageItemCommand;
import chromakey.devsdream.deserialization.BlockDeserializer;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.modid)
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
  public void onCommandsRegister(RegisterCommandsEvent event) {
    logger.info("Successfully registered commands");
    AdvancedEffectCommand.register(event.getDispatcher());
    HealthCommand.register(event.getDispatcher());
    DamageCommand.register(event.getDispatcher());
    FeedCommand.register(event.getDispatcher());
    ExhaustCommand.register(event.getDispatcher());
    AirCommand.register(event.getDispatcher());
    IgniteCommand.register(event.getDispatcher());
    DamageItemCommand.register(event.getDispatcher());
    RandomNumberCommand.register(event.getDispatcher());
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
      List<Block> blockList = Lists.newArrayList();
      try {
        final File[] REGISTRY = new File("C:/Users/willi/AppData/Roaming/.minecraft/devsdream-registry").listFiles();
        for (final File namespace : REGISTRY) {
          for (final File block : new File(namespace.getPath() + "/blocks").listFiles()) {
            try {
              Block newBlock = BlockDeserializer.deserializeBlock(JSONHelper.getObjectFromFile(block, "block"))
                  .setRegistryName(namespace.getName(), FilenameUtils.getBaseName(block.getName()));
              blockList.add(newBlock);
            } catch (JsonSyntaxException e) {
              logger.catching(e);
            }
          }
        }
      } catch (NullPointerException e) {
      }
      blockList.iterator().forEachRemaining((block) -> {
        event.getRegistry().register(block);
      });
      logger.info("Registered " + blockList.size() + " blocks");
    }
  }
}