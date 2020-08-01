package chromakey.devsdream.deserialization;

import java.io.File;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.util.CustomArmorMaterial;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.item.AirItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Food;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;

public class ItemDeserializer {
    public static Item deserializeItem(JsonObject object) throws JsonSyntaxException {
        String type = JSONUtils.getString(object, "type");
        switch (type) {
            case "simple": {
                return new Item(deserializeProperties(object));
            }
            case "air": {
                return new AirItem(JSONHelper.setRequiredBlockElement(object, "block"), deserializeProperties(object));
            }
            case "armor": {
                return new ArmorItem(deserializeArmorMaterial(object),
                        JSONHelper.setRequiredSlotElement(object, JSONUtils.getString(object, "slot")),
                        deserializeProperties(object));
            }
            default: {
                throw new JsonSyntaxException("Unknown type '" + type + "'");
            }
        }
    }

    private static Properties deserializeProperties(JsonObject object) throws JsonSyntaxException {
        Properties properties = new Item.Properties();
        JsonObject propertiesObj = JSONUtils.getJsonObject(object, "properties");
        if (propertiesObj.has("tool_types")) {
            JSONUtils.getJsonArray(object, "tool_types").forEach((toolType) -> {
                JsonObject toolTypeObj = JSONUtils.getJsonObject(toolType, "tool type object");
                properties.addToolType(JSONHelper.deserializeToolType(toolTypeObj, "type"),
                        JSONUtils.getInt(toolTypeObj, "level"));
            });
        }
        if (propertiesObj.has("container_item")) {
            properties.containerItem(JSONUtils.getItem(propertiesObj, "container_item"));
        }
        if (propertiesObj.has("default_max_damage")) {
            properties.defaultMaxDamage(JSONUtils.getInt(propertiesObj, "default_max_damage"));
        }
        if (propertiesObj.has("food")) {
            JsonObject foodObj = JSONUtils.getJsonObject(propertiesObj, "food");
            Food.Builder food = new Food.Builder();
            food.hunger(JSONUtils.getInt(foodObj, "hunger"));
            food.saturation(JSONUtils.getFloat(foodObj, "saturation"));
            JSONUtils.getJsonArray(foodObj, "effects").iterator().forEachRemaining((effect) -> {
                JsonObject effectObj = JSONUtils.getJsonObject(effect, "effect object");
                food.effect(() -> {
                    return new EffectInstance(JSONHelper.setRequiredEffectElement(effectObj, "effect"), JSONUtils.getInt(effectObj, "duration"), JSONUtils.getInt(effectObj, "amplifier"));
                }, JSONUtils.getFloat(effectObj, "probability"));
            });
        }
        return properties;
    }

    private static IArmorMaterial deserializeArmorMaterial(JsonObject object) throws JsonSyntaxException {
        String armorMaterialString = JSONUtils.getString(object, "material");
        switch (armorMaterialString) {
            case "leather": {
                return ArmorMaterial.LEATHER;
            }
            case "chain": {
                return ArmorMaterial.CHAIN;
            }
            case "iron": {
                return ArmorMaterial.IRON;
            }
            case "gold": {
                return ArmorMaterial.GOLD;
            }
            case "diamond": {
                return ArmorMaterial.DIAMOND;
            }
            case "netherite": {
                return ArmorMaterial.NETHERITE;
            }
            case "turtle": {
                return ArmorMaterial.TURTLE;
            }
            default: {
                JsonObject customArmorMaterial = JSONHelper.getObjectFromFile(new File(armorMaterialString),
                        "armor material");
                JsonObject damageReductionAmountObject = JSONUtils.getJsonObject(customArmorMaterial, "armor");
                return new CustomArmorMaterial(JSONUtils.getString(customArmorMaterial, "name"),
                        JSONUtils.getInt(customArmorMaterial, "max_damage_factor"),
                        new int[] { JSONUtils.getInt(damageReductionAmountObject, "feet"),
                                JSONUtils.getInt(damageReductionAmountObject, "legs"),
                                JSONUtils.getInt(damageReductionAmountObject, "chest"),
                                JSONUtils.getInt(damageReductionAmountObject, "head") },
                        JSONUtils.getInt(customArmorMaterial, "enchantability"),
                        JSONHelper.setRequiredSoundElement(customArmorMaterial, "sound"),
                        JSONUtils.getFloat(customArmorMaterial, "armor_toughness"),
                        JSONUtils.getFloat(customArmorMaterial, "knockback_resistance"), () -> {
                            return Ingredient.deserialize(customArmorMaterial.get("repair_ingredient"));
                        });
            }
        }
    }
}