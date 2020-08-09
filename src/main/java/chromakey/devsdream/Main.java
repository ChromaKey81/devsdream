package chromakey.devsdream;

import net.minecraft.block.Block;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
import chromakey.devsdream.deserialization.EffectDeserializer;
import chromakey.devsdream.deserialization.ItemDeserializer;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
    MinecraftForge.EVENT_BUS.register(this);
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
        File[] objectpacks = new File(System.getProperty("user.dir") + "/objectpacks").listFiles();
        for (final File namespace : objectpacks) {
          for (final File block : new File(namespace.getPath() + "/blocks").listFiles()) {
            String id = namespace.getName() + ":" + FilenameUtils.getBaseName(block.getName());
            try {
              Block newBlock = BlockDeserializer.deserializeBlock(JSONHelper.getObjectFromFile(block))
                  .setRegistryName(id);
              blockList.add(newBlock);
            } catch (JsonSyntaxException e) {
              logger.error("Couldn't load block '" + id + "': " + e.getMessage());
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

    @SubscribeEvent
    public static void registerEffects(final RegistryEvent.Register<Effect> event) {
      List<Effect> effectList = Lists.newArrayList();
      try {
        File[] objectpacks = new File(System.getProperty("user.dir") + "/objectpacks").listFiles();
        for (final File namespace : objectpacks) {
          for (final File effect : new File(namespace.getPath() + "/effects").listFiles()) {
            String id = namespace.getName() + ":" + FilenameUtils.getBaseName(effect.getName());
            try {
              Effect newEffect = EffectDeserializer.deserializeEffect(JSONHelper.getObjectFromFile(effect))
                  .setRegistryName(id);
              effectList.add(newEffect);
            } catch (JsonSyntaxException e) {
              logger.error("Couldn't load effect '" + id + "': " + e.getMessage());
            }
          }
        }
      } catch (NullPointerException e) {
      }
      effectList.iterator().forEachRemaining((effect) -> {
          event.getRegistry().register(effect);
      });
      logger.info("Registered " + effectList.size() + " effects");
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
      List<Item> itemList = Lists.newArrayList();
      Map<String, IArmorMaterial> armorMaterialMap = Maps.newHashMap();
      Map<String, IItemTier> itemTierMap = Maps.newHashMap();
      try {
        File[] objectpacks = new File(System.getProperty("user.dir") + "/objectpacks").listFiles();
        for (final File namespace : objectpacks) {
          for (final File armorMaterial : new File(namespace.getPath() + "/items/armor_materials").listFiles()) {
            String name = FilenameUtils.getBaseName(armorMaterial.getName());
            String id = namespace.getName() + ":" + name;
            try {
              IArmorMaterial newMaterial = ItemDeserializer.deserializeArmorMaterial(name, JSONHelper.getObjectFromFile(armorMaterial));
              armorMaterialMap.put(id, newMaterial);
            } catch (JsonSyntaxException e) {
              logger.error("Couldn't load armor material '" + id + "': " + e.getMessage());
            }
          }
          for (final File item : new File(namespace.getPath() + "/items").listFiles()) {
            String id = namespace.getName() + ":" + FilenameUtils.getBaseName(item.getName());
            try {
              Item newItem = ItemDeserializer.deserializeItem(JSONHelper.getObjectFromFile(item), armorMaterialMap, itemTierMap)
                  .setRegistryName(id);
              itemList.add(newItem);
            } catch (JsonSyntaxException e) {
              logger.error("Couldn't load item '" + id + "': " + e.getMessage());
            }
          }
        }
      } catch (NullPointerException e) {
      }
      itemList.iterator().forEachRemaining((item) -> {
          event.getRegistry().register(item);
      });
      logger.info("Registered " + itemList.size() + " items");
    }

  }
}