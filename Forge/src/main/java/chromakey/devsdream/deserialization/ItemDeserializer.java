package chromakey.devsdream.deserialization;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.custom.CustomArmorMaterial;
import chromakey.devsdream.custom.CustomItemTier;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.BoatEntity.Type;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AirItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.BoatItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.BookItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.CompassItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.DebugStickItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.DyeableHorseArmorItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.EnchantedGoldenAppleItem;
import net.minecraft.item.EnderCrystalItem;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Food;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ItemDeserializer {

    public static Item deserializeItem(JsonObject object, Map<ResourceLocation, IArmorMaterial> armorMaterialMap, Map<ResourceLocation, IItemTier> itemTierMap) throws JsonSyntaxException {

        String type = JSONUtils.getString(object, "type");
        switch (type) {
            case "simple": {
                return new Item(deserializeProperties(object));
            }
            case "air": {
                return new AirItem(JSONHelper.setRequiredBlockElement(object, "block"), deserializeProperties(object));
            }
            case "armor": {
                EquipmentSlotType slot = JSONHelper.setRequiredSlotElement(object, "slot");
                if (object.has("armor_material")) {
                    if (object.get("armor_material").isJsonObject()) {
                        JsonObject armorMaterial = JSONUtils.getJsonObject(object, "armor_material");
                        return new ArmorItem(
                                deserializeArmorMaterial(JSONUtils.getString(armorMaterial, "name"), armorMaterial),
                                slot, deserializeProperties(object));
                    } else {
                        return new ArmorItem(armorMaterialMap.get(new ResourceLocation(JSONUtils.getString(object, "armor_material"))), slot, deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing armor material, expected to find a JsonObject or a String");
                }
            }
            case "armor_stand": {
                return new ArmorStandItem(deserializeProperties(object));
            }
            case "arrow": {
                return new ArrowItem(deserializeProperties(object));
            }
            case "axe": {
                float attackDamage = JSONUtils.getFloat(object, "attack_damage");
                float attackSpeed = JSONUtils.getFloat(object, "attack_speed");
                if (object.has("tier")) {
                    if (object.get("tier").isJsonObject()) {
                        JsonObject itemTier = JSONUtils.getJsonObject(object, "tier");
                        return new AxeItem(deserializeItemTier(itemTier), attackDamage, attackSpeed, deserializeProperties(object));
                    } else {
                        return new AxeItem(itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "tier"))), attackDamage, attackSpeed, deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing armor material, expected to find a JsonObject or a String");
                }
            }
            case "banner": {
                return new BannerItem(JSONHelper.setRequiredBlockElement(object, "floor_banner"), JSONHelper.setRequiredBlockElement(object, "wall_banner"), deserializeProperties(object));
            }
            case "banner_pattern": {
                return new BannerPatternItem(BannerPattern.byHash(JSONUtils.getString(object, "pattern_hash")), deserializeProperties(object));
            }
            case "bed": {
                return new BedItem(JSONHelper.setRequiredBlockElement(object, "block"), deserializeProperties(object));
            }
            case "block": {
                return new BlockItem(JSONHelper.setRequiredBlockElement(object, "block"),
                        deserializeProperties(object));
            }
            case "block_named": {
                return new BlockNamedItem(JSONHelper.setRequiredBlockElement(object, "block"), deserializeProperties(object));
            }
            case "boat": {
                return new BoatItem(deserializeBoatType(JSONUtils.getString(object, "boat_type")), deserializeProperties(object));
            }
            case "bone_meal": {
                return new BoneMealItem(deserializeProperties(object));
            }
            case "book": {
                return new BookItem(deserializeProperties(object));
            }
            case "bow": {
                return new BowItem(deserializeProperties(object));
            }
            case "bucket": {
                return new BucketItem(() -> {
                    return JSONHelper.deserializeFluid(object, "fluid");
                }, deserializeProperties(object));
            }
            case "chorus_fruit": {
                return new ChorusFruitItem(deserializeProperties(object));
            }
            case "compass": {
                return new CompassItem(deserializeProperties(object));
            }
            case "crossbow": {
                return new CrossbowItem(deserializeProperties(object));
            }
            case "debug_stick": {
                return new DebugStickItem(deserializeProperties(object));
            }
            case "dye": {
                return new DyeItem(DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE), deserializeProperties(object));
            }
            case "dyeable_armor": {
                EquipmentSlotType slot = JSONHelper.setRequiredSlotElement(object, "slot");
                if (object.has("armor_material")) {
                    if (object.get("armor_material").isJsonObject()) {
                        JsonObject armorMaterial = JSONUtils.getJsonObject(object, "armor_material");
                        return new ArmorItem(
                                deserializeArmorMaterial(JSONUtils.getString(armorMaterial, "name"), armorMaterial),
                                slot, deserializeProperties(object));
                    } else {
                        return new DyeableArmorItem(armorMaterialMap.get(new ResourceLocation(JSONUtils.getString(object, "armor_material"))), slot, deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing armor material, expected to find a JsonObject or a String");
                }
            }
            case "dyeable_horse_armor": {
                return new DyeableHorseArmorItem(JSONUtils.getInt(object, "armor"), JSONUtils.getString(object, "material_name"), deserializeProperties(object));
            }
            case "egg": {
                return new EggItem(deserializeProperties(object));
            }
            case "elytra": {
                return new ElytraItem(deserializeProperties(object));
            }
            case "enchanted_book": {
                return new EnchantedBookItem(deserializeProperties(object));
            }
            case "enchanted_golden_apple": {
                return new EnchantedGoldenAppleItem(deserializeProperties(object));
            }
            case "ender_crystal": {
                return new EnderCrystalItem(deserializeProperties(object));
            }
            case "ender_eye": {
                return new EnderEyeItem(deserializeProperties(object));
            }
            case "ender_pearl": {
                return new EnderPearlItem(deserializeProperties(object));
            }
            case "experience_bottle": {
                return new ExperienceBottleItem(deserializeProperties(object));
            }
            default: {
                throw new JsonSyntaxException("Unknown item type '" + type + "'");
            }
        }

    }

    public static BoatEntity.Type deserializeBoatType(String boatType) throws JsonSyntaxException {
        switch (boatType) {
            case "oak": {
                return Type.OAK;
            }
            case "birch": {
                return Type.BIRCH;
            }
            case "spruce": {
                return Type.SPRUCE;
            }
            case "jungle": {
                return Type.JUNGLE;
            }
            case "acacia": {
                return Type.ACACIA;
            }
            case "dark_oak": {
                return Type.DARK_OAK;
            }
            default: {
                throw new JsonSyntaxException("Unknown boat type '" + boatType + "'");
            }
        }
    }

    public static IArmorMaterial deserializeArmorMaterial(String name, JsonObject object) throws JsonSyntaxException {
        JsonObject damageReduction = JSONUtils.getJsonObject(object, "damage_reduction");
        return new CustomArmorMaterial(name, JSONUtils.getInt(object, "max_damage_factor"),
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
        if (propertiesObj.has("food")) {
            JsonObject newFood = JSONUtils.getJsonObject(propertiesObj, "food");
            Food.Builder food = new Food.Builder().hunger(JSONUtils.getInt(newFood, "restores")).saturation(JSONUtils.getFloat(newFood, "saturation"));
            if(newFood.has("meat")) {
                boolean isMeat = JSONUtils.getBoolean(newFood, "meat");
                if (isMeat == true) {
                    food.meat();
                }
            }
            if (newFood.has("always_edible")) {
                boolean alwaysEdible = JSONUtils.getBoolean(newFood, "always_edible");
                if (alwaysEdible == true) {
                    food.setAlwaysEdible();
                }
            }
            if (newFood.has("fast_to_eat")) {
                boolean fastToEat = JSONUtils.getBoolean(newFood, "fast_to_eat");
                if (fastToEat == true) {
                    food.fastToEat();
                }
            }
            if (newFood.has("effects")) {
                JsonArray effects = JSONUtils.getJsonArray(newFood, "effects");
                effects.forEach((effect) -> {
                    JsonObject effectObj = JSONUtils.getJsonObject(effect, "effect");
                    food.effect(() -> {
                        return new EffectInstance(JSONHelper.setRequiredEffectElement(effectObj, "effect"), JSONUtils.getInt(effectObj, "duration"), JSONUtils.getInt(effectObj, "amplifier"));
                    }, JSONUtils.getFloat(effectObj, "probability"));
                });
            }
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
        if (propertiesObj.has("fireproof")) {
            if (JSONUtils.getBoolean(propertiesObj, "fireproof") == true) {
                properties.isImmuneToFire();
            }
        }
        if (propertiesObj.has("max_damage")) {
            properties.maxDamage(JSONUtils.getInt(propertiesObj, "max_damage"));
        }
        if (propertiesObj.has("max_stack_size")) {
            properties.maxStackSize(JSONUtils.getInt(propertiesObj, "max_stack_size"));
        }
        if (propertiesObj.has("rarity")) {
            String rarity = JSONUtils.getString(propertiesObj, "rarity");
            switch (rarity) {
                case "common": {
                    properties.rarity(Rarity.COMMON);
                    break;
                }
                case "uncommon": {
                    properties.rarity(Rarity.UNCOMMON);
                    break;
                }
                case "rare": {
                    properties.rarity(Rarity.RARE);
                    break;
                }
                case "epic": {
                    properties.rarity(Rarity.EPIC);
                    break;
                }
                default: {
                    throw new JsonSyntaxException("Unknown rarity '" + rarity + "'");
                }
            }

        }
        if(propertiesObj.has("reparable")) {
            if (JSONUtils.getBoolean(propertiesObj, "reparable") == false) {
                properties.setNoRepair();
            }
        }
        return properties;
    }
}