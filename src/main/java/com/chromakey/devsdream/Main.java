package com.chromakey.devsdream;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.chromakey.devsdream.command.impl.AdvancedEffectCommand;

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
    }

    @Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
    public static class events {
      
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
          event.getRegistry().registerAll(
            BlockList.blank_slate_stone = new Block(Block.Properties.create(Material.ROCK).sound(SoundType.STONE)).setRegistryName(new ResourceLocation(modid, "blank_slate_stone"))
          );
          logger.info("Successfully registered blocks");
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
          event.getRegistry().registerAll(
            ItemList.blank_slate_stone = new BlockItem(BlockList.blank_slate_stone, new Item.Properties().maxStackSize(64)).setRegistryName(new ResourceLocation(modid, "blank_slate_stone"))
          );
          logger.info("Successfully registered items");
        }
    }
}