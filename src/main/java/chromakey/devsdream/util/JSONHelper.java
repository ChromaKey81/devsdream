package chromakey.devsdream.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effect;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

public class JSONHelper {

    public static Block setRequiredBlockElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        Block block = ForgeRegistries.BLOCKS.getValue(resourcelocation);
        if (block == null) {
            throw new JsonSyntaxException("Unknown block '" + argument + "'");
        } else {
            return block;
        }
    }

    public static Effect setRequiredEffectElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        Effect effect = ForgeRegistries.POTIONS.getValue(resourcelocation);
        if (effect == null) {
            throw new JsonSyntaxException("Unknown effect '" + argument + "'");
        } else {
            return effect;
        }
    }

    public static SoundEvent setRequiredSoundElement(JsonObject object, String element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, element);
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(resourcelocation);
        if (sound == null) {
            throw new JsonSyntaxException("Unknown sound event '" + argument + "'");
        } else {
            return sound;
        }
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

    public static EquipmentSlotType setRequiredSlotElement(JsonElement element) throws JsonSyntaxException {
        String argument = JSONUtils.getString(element, "equipment slot");
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

    public static ToolType setRequiredToolType(JsonObject object, String element) throws JsonSyntaxException {
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