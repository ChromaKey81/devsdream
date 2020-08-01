package chromakey.devsdream.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class JSONHelper {
    public static Block setRequiredBlockElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        Block block = RegistryObject.of(resourcelocation, ForgeRegistries.BLOCKS).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown block '" + argument + "'");
        });
        return block;
    }

    public static Effect setRequiredEffectElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        Effect effect = RegistryObject.of(resourcelocation, ForgeRegistries.POTIONS).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown block '" + argument + "'");
        });
        return effect;
    }
    
    public static SoundEvent setRequiredSoundElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        SoundEvent sound = RegistryObject.of(resourcelocation, ForgeRegistries.SOUND_EVENTS).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown block '" + argument + "'");
        });
        return sound;
    }

    public static Item setRequiredItemElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        Item item = RegistryObject.of(resourcelocation, ForgeRegistries.ITEMS).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + argument + "'");
        });
        return item;
    }

    public static JsonObject getObjectFromFile(File file, String expected) throws JsonSyntaxException {
        JsonSyntaxException exception = new JsonSyntaxException("Unknown " + expected + " '" + file + "'");
        try (FileReader reader = new FileReader(file)) {
            return JSONUtils.fromJson(reader, false);
        } catch (FileNotFoundException e) {
            throw exception;
        } catch (IOException e) {
            throw exception;
        }
    }

    public static EquipmentSlotType setRequiredSlotElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        switch (argument) {
            case "feet": {
                return EquipmentSlotType.FEET;
            }
            case "legs": {
                return EquipmentSlotType.LEGS;
            }
            case "chest": {
                return EquipmentSlotType.CHEST;
            }
            case "head": {
                return EquipmentSlotType.HEAD;
            }
            case "mainhand": {
                return EquipmentSlotType.MAINHAND;
            }
            case "offhand": {
                return EquipmentSlotType.OFFHAND;
            }
            default: {
                throw new JsonSyntaxException("Unknown equipment slot type '" + argument + "'");
            }
        }
    }

    public static ToolType deserializeToolType(JsonObject object, String element) throws JsonSyntaxException {
        String toolType = JSONUtils.getString(object, element);
        switch (toolType) {
          case "pickaxe": {
            return ToolType.PICKAXE;
          }
          case "axe": {
            return ToolType.AXE;
          }
          case "shovel": {
            return ToolType.SHOVEL;
          }
          case "hoe": {
            return ToolType.HOE;
          }
          default: {
            throw new JsonSyntaxException(
                "Unknown tool type '" + toolType + "'; only 'pickaxe', 'axe', 'shovel', or 'hoe' are accepted");
          }
        }
    }
}