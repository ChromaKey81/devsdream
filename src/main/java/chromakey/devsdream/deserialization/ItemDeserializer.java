package chromakey.devsdream.deserialization;

import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.custom.CustomArmorMaterial;
import chromakey.devsdream.custom.CustomItemTier;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;

public class ItemDeserializer {

    public static Item deserializeItem(JsonObject object, Map<String, IArmorMaterial> armorMaterialMap, Map<String, IItemTier> itemTierMap) throws JsonSyntaxException {

        String type = JSONUtils.getString(object, "type");
        switch (type) {
            case "simple": {
                return new Item(deserializeProperties(object));
            }
            case "block": {
                return new BlockItem(JSONHelper.setRequiredBlockElement(object, "block"),
                        deserializeProperties(object));
            }
            case "armor": {
                if (object.has("armor_material")) {
                    if (object.get("armor_material").isJsonObject()) {
                        JsonObject armorMaterial = JSONUtils.getJsonObject(object, "armor_material");
                        return new ArmorItem(
                                deserializeArmorMaterial(JSONUtils.getString(armorMaterial, "name"), armorMaterial),
                                JSONHelper.setRequiredSlotElement(object.get("slot")), deserializeProperties(object));
                    } else {
                        return new ArmorItem(armorMaterialMap.get(JSONUtils.getString(object, "armor_material")), JSONHelper.setRequiredSlotElement(object.get("slot")), deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing armor material, expected to find a JsonObject or a String");
                }
            }
            default: {
                throw new JsonSyntaxException("Unknown item type '" + type + "'");
            }
        }

    }

    public static IArmorMaterial deserializeArmorMaterial(String name, JsonObject object) throws JsonSyntaxException {
        JsonObject damageReduction = JSONUtils.getJsonObject(object, "damage_reduction");
        return new CustomArmorMaterial(name,
                JSONUtils.getInt(object, "max_damage_factor"),
                new int[] { JSONUtils.getInt(damageReduction, "feet"), JSONUtils.getInt(damageReduction, "legs"),
                        JSONUtils.getInt(damageReduction, "chest"), JSONUtils.getInt(damageReduction, "head") },
                JSONUtils.getInt(object, "enchantability"), JSONHelper.setRequiredSoundElement(object, "sound"),
                JSONUtils.getFloat(object, "toughness"), JSONUtils.getFloat(object, "knockback_resistance"), () -> {
                    return Ingredient.fromItems(JSONHelper.setRequiredItemElement(object, "repair_material"));
                });
    }

    public static IItemTier deserializeItemTier(JsonObject object) throws JsonSyntaxException {
        return new CustomItemTier(JSONUtils.getInt(object, "harvest_level"), JSONUtils.getInt(object, "max_uses"),
                JSONUtils.getFloat(object, "efficiency"), JSONUtils.getFloat(object, "attack_damage"),
                JSONUtils.getInt(object, "enchantability"), () -> {
                    return Ingredient.fromItems(JSONHelper.setRequiredItemElement(object, "repair_material"));
                });
    }

    public static Properties deserializeProperties(JsonObject object) throws JsonSyntaxException {
        Properties properties = new Properties();
        JsonObject propertiesObj = JSONUtils.getJsonObject(object, "properties");
        if (propertiesObj.has("harvesting")) {
            JsonObject harvesting = JSONUtils.getJsonObject(propertiesObj, "harvesting");
            properties.addToolType(JSONHelper.setRequiredToolType(harvesting, "tool_type"),
                    JSONUtils.getInt(harvesting, "level"));
        }
        if (propertiesObj.has("container_item")) {
            properties.containerItem(JSONHelper.setRequiredItemElement(propertiesObj, "container_item"));
        }
        if (propertiesObj.has("default_max_damage")) {
            properties.defaultMaxDamage(JSONUtils.getInt(propertiesObj, "default_max_damage"));
        }
        if (propertiesObj.has("group")) {
            String group = JSONUtils.getString(propertiesObj, "group");
            switch (group) {
                case "building_blocks": {
                    properties.group(ItemGroup.BUILDING_BLOCKS);
                    break;
                }
                case "decorations": {
                    properties.group(ItemGroup.DECORATIONS);
                    break;
                }
                case "redstone": {
                    properties.group(ItemGroup.REDSTONE);
                    break;
                }
                case "transportation": {
                    properties.group(ItemGroup.TRANSPORTATION);
                    break;
                }
                case "misc": {
                    properties.group(ItemGroup.MISC);
                    break;
                }
                case "search": {
                    properties.group(ItemGroup.SEARCH);
                    break;
                }
                case "food": {
                    properties.group(ItemGroup.FOOD);
                    break;
                }
                case "tools": {
                    properties.group(ItemGroup.TOOLS);
                    break;
                }
                case "combat": {
                    properties.group(ItemGroup.COMBAT);
                    break;
                }
                case "brewing": {
                    properties.group(ItemGroup.BREWING);
                    break;
                }
                case "materials": {
                    properties.group(ItemGroup.MATERIALS);
                    break;
                }
                case "hotbar": {
                    properties.group(ItemGroup.HOTBAR);
                    break;
                }
                case "inventory": {
                    properties.group(ItemGroup.INVENTORY);
                    break;
                }
                default: {
                    throw new JsonSyntaxException("Unknown item group '" + group + "'");
                }
            }
        }
        return properties;
    }
}