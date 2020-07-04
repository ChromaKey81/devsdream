package com.chromakey.devsdream;

import com.chromakey.devsdream.command.impl.AdvancedEffectCommand;
import com.chromakey.devsdream.command.impl.AirCommand;
import com.chromakey.devsdream.command.impl.DamageCommand;
import com.chromakey.devsdream.command.impl.ExhaustCommand;
import com.chromakey.devsdream.command.impl.FeedCommand;
import com.chromakey.devsdream.command.impl.HealthCommand;
import com.chromakey.devsdream.command.impl.IgniteCommand;
import com.chromakey.devsdream.item.DevsFoods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    }

    @Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
    public static class events {
      
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
          event.getRegistry().registerAll(
            BlockList.blank_slate_stone = new Block(Block.Properties.create(Material.ROCK).sound(SoundType.STONE)).setRegistryName(new ResourceLocation(modid, "blank_slate_stone")),
            BlockList.blank_slate_hard_stone = new Block(Block.Properties.create(Material.ROCK).sound(SoundType.STONE)).setRegistryName(new ResourceLocation(modid, "blank_slate_hard_stone")),
            BlockList.blank_slate_ore = new Block(Block.Properties.create(Material.ROCK).sound(SoundType.STONE)).setRegistryName(new ResourceLocation(modid, "blank_slate_ore"))
          );
          logger.info("Successfully registered blocks");
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
          event.getRegistry().registerAll(
            ItemList.blank_slate_stone = new BlockItem(BlockList.blank_slate_stone, new Item.Properties().maxStackSize(64)).setRegistryName(new ResourceLocation(modid, "blank_slate_stone")),
            ItemList.blank_slate_food = new Item(new Item.Properties().maxStackSize(64).food(DevsFoods.BLANK_SLATE_FOOD)).setRegistryName(new ResourceLocation(modid, "blank_slate_food")),
            ItemList.blank_slate_fast_food = new Item(new Item.Properties().maxStackSize(64).food(DevsFoods.BLANK_SLATE_FAST_FOOD)).setRegistryName(new ResourceLocation(modid, "blank_slate_fast_food")),
            ItemList.blank_slate_food_unstackable = new Item(new Item.Properties().maxStackSize(1).food(DevsFoods.BLANK_SLATE_FOOD)).setRegistryName(new ResourceLocation(modid, "blank_slate_food_unstackable"))
          );
          logger.info("Successfully registered items");
        }
    }
}