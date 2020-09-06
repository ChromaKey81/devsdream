package chromakey.devsdream.deserialization;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.custom.CustomArmorMaterial;
import chromakey.devsdream.custom.CustomItemTier;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.BoatEntity.Type;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
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
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.FireworkStarItem;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Food;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.HangingEntityItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFrameItem;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.KnowledgeBookItem;
import net.minecraft.item.LeadItem;
import net.minecraft.item.LilyPadItem;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.MapItem;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.item.MinecartItem;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.NameTagItem;
import net.minecraft.item.OnAStickItem;
import net.minecraft.item.OperatorOnlyItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.Rarity;
import net.minecraft.item.SaddleItem;
import net.minecraft.item.ScaffoldingItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SignItem;
import net.minecraft.item.SimpleFoiledItem;
import net.minecraft.item.SkullItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SoupItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.item.TieredItem;
import net.minecraft.item.TippedArrowItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.TridentItem;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

public class ItemDeserializer {

    public static Item deserializeItem(JsonObject object, Map<ResourceLocation, IArmorMaterial> armorMaterialMap,
            Map<ResourceLocation, IItemTier> itemTierMap) throws JsonSyntaxException {

        String type = JSONUtils.getString(object, "type");
        switch (type) {
            case "simple": {
                return new Item(deserializeProperties(object));
            }
            case "complex": {
                return ComplexItemDeserializer.deserializeComplexItem(object);
            }
            case "air": {
                return new AirItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
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
                        return new ArmorItem(
                                armorMaterialMap
                                        .get(new ResourceLocation(JSONUtils.getString(object, "armor_material"))),
                                slot, deserializeProperties(object));
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
                        return new AxeItem(deserializeItemTier(itemTier), attackDamage, attackSpeed,
                                deserializeProperties(object));
                    } else {
                        return new AxeItem(itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "tier"))),
                                attackDamage, attackSpeed, deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing armor material, expected to find a JsonObject or a String");
                }
            }
            case "banner": {
                return new BannerItem(JSONHelper.getBlock(JSONUtils.getString(object, "floor_banner")),
                        JSONHelper.getBlock(JSONUtils.getString(object, "wall_banner")), deserializeProperties(object));
            }
            case "banner_pattern": {
                return new BannerPatternItem(BannerPattern.byHash(JSONUtils.getString(object, "pattern_hash")),
                        deserializeProperties(object));
            }
            case "bed": {
                return new BedItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
            }
            case "block": {
                return new BlockItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
            }
            case "block_named": {
                return new BlockNamedItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
            }
            case "boat": {
                return new BoatItem(deserializeBoatType(JSONUtils.getString(object, "boat_type")),
                        deserializeProperties(object));
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
                return new DyeItem(DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE),
                        deserializeProperties(object));
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
                        return new DyeableArmorItem(
                                armorMaterialMap
                                        .get(new ResourceLocation(JSONUtils.getString(object, "armor_material"))),
                                slot, deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing armor material, expected to find a JsonObject or a String");
                }
            }
            case "dyeable_horse_armor": {
                return new DyeableHorseArmorItem(JSONUtils.getInt(object, "armor"), JSONUtils.getString(object, "tier"),
                        deserializeProperties(object));
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
            case "filled_map": {
                return new FilledMapItem(deserializeProperties(object));
            }
            case "fire_charge": {
                return new FireChargeItem(deserializeProperties(object));
            }
            case "firework_rocket": {
                return new FireworkRocketItem(deserializeProperties(object));
            }
            case "firework_star": {
                return new FireworkStarItem(deserializeProperties(object));
            }
            case "fish_bucket": {
                return new FishBucketItem(() -> {
                    return JSONHelper.getEntity(JSONUtils.getString(object, "entity"));
                }, () -> {
                    return JSONHelper.getFluid(JSONUtils.getString(object, "fluid"));
                }, deserializeProperties(object));
            }
            case "fishing_rod": {
                return new FishingRodItem(deserializeProperties(object));
            }
            case "flint_and_steel": {
                return new FlintAndSteelItem(deserializeProperties(object));
            }
            case "glass_bottle": {
                return new GlassBottleItem(deserializeProperties(object));
            }
            case "hanging_entity": {
                String argument = JSONUtils.getString(object, "hanging_entity");
                EntityType<?> entity = JSONHelper.getEntity(argument);
                try {
                    return new HangingEntityItem((EntityType<? extends HangingEntity>) entity,
                            deserializeProperties(object));
                } catch (ClassCastException e) {
                    throw new JsonSyntaxException("The entity " + argument + " is not a hanging entity");
                }
            }
            case "hoe": {
                if (object.has("item_tier")) {
                    if (object.get("item_tier").isJsonObject()) {
                        JsonObject itemTier = JSONUtils.getJsonObject(object, "item_tier");
                        return new HoeItem(deserializeItemTier(itemTier), JSONUtils.getInt(object, "attack_damage"),
                                JSONUtils.getFloat(object, "attack_speed"), deserializeProperties(object));
                    } else {
                        return new HoeItem(
                                itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "item_tier"))),
                                JSONUtils.getInt(object, "attack_damage"), JSONUtils.getFloat(object, "attack_speed"),
                                deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing item tier, expected to find a JsonObject or a String");
                }
            }
            case "honey_bottle": {
                return new HoneyBottleItem(deserializeProperties(object));
            }
            case "horse_armor": {
                return new HorseArmorItem(JSONUtils.getInt(object, "armor"), JSONUtils.getString(object, "tier"),
                        deserializeProperties(object));
            }
            case "item_frame": {
                return new ItemFrameItem(deserializeProperties(object));
            }
            case "knowledge_book": {
                return new KnowledgeBookItem(deserializeProperties(object));
            }
            case "lead": {
                return new LeadItem(deserializeProperties(object));
            }
            case "lily_pad": {
                return new LilyPadItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
            }
            case "lingering_potion": {
                return new LingeringPotionItem(deserializeProperties(object));
            }
            case "map": {
                return new MapItem(deserializeProperties(object));
            }
            case "milk_bucket": {
                return new MilkBucketItem(deserializeProperties(object));
            }
            case "minecart": {
                return new MinecartItem(deserializeMinecartType(JSONUtils.getString(object, "minecart_type")),
                        deserializeProperties(object));
            }
            case "music_disc": {
                return new MusicDiscItem(JSONUtils.getInt(object, "comparator_signal"), () -> {
                    return JSONHelper.setRequiredSoundElement(object, "sound");
                }, deserializeProperties(object));
            }
            case "name_tag": {
                return new NameTagItem(deserializeProperties(object));
            }
            case "on_a_stick": {
                return new OnAStickItem(deserializeProperties(object),
                        JSONHelper.getEntity(JSONUtils.getString(object, "mount")),
                        JSONUtils.getInt(object, "damage_per_use"));
            }
            case "operator_only": {
                return new OperatorOnlyItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
            }
            case "pickaxe": {
                if (object.has("item_tier")) {
                    if (object.get("item_tier").isJsonObject()) {
                        JsonObject itemTier = JSONUtils.getJsonObject(object, "item_tier");
                        return new PickaxeItem(deserializeItemTier(itemTier), JSONUtils.getInt(object, "attack_damage"),
                                JSONUtils.getFloat(object, "attack_speed"), deserializeProperties(object));
                    } else {
                        return new PickaxeItem(
                                itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "item_tier"))),
                                JSONUtils.getInt(object, "attack_damage"), JSONUtils.getFloat(object, "attack_speed"),
                                deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing item tier, expected to find a JsonObject or a String");
                }
            }
            case "potion": {
                return new PotionItem(deserializeProperties(object));
            }
            case "saddle": {
                return new SaddleItem(deserializeProperties(object));
            }
            case "scaffolding": {
                return new ScaffoldingItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
            }
            case "shears": {
                return new ShearsItem(deserializeProperties(object));
            }
            case "shield": {
                return new ShieldItem(deserializeProperties(object));
            }
            case "shovel": {
                if (object.has("item_tier")) {
                    if (object.get("item_tier").isJsonObject()) {
                        JsonObject itemTier = JSONUtils.getJsonObject(object, "item_tier");
                        return new ShovelItem(deserializeItemTier(itemTier),
                                JSONUtils.getFloat(object, "attack_damage"), JSONUtils.getFloat(object, "attack_speed"),
                                deserializeProperties(object));
                    } else {
                        return new ShovelItem(
                                itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "item_tier"))),
                                JSONUtils.getInt(object, "attack_damage"), JSONUtils.getFloat(object, "attack_speed"),
                                deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing item tier, expected to find a JsonObject or a String");
                }
            }
            case "sign": {
                return new SignItem(deserializeProperties(object),
                        JSONHelper.getBlock(JSONUtils.getString(object, "floor_block")),
                        JSONHelper.getBlock(JSONUtils.getString(object, "wall_block")));
            }
            case "simple_foiled": {
                return new SimpleFoiledItem(deserializeProperties(object));
            }
            case "skull": {
                return new SkullItem(JSONHelper.getBlock(JSONUtils.getString(object, "floor_block")),
                        JSONHelper.getBlock(JSONUtils.getString(object, "wall_block")), deserializeProperties(object));
            }
            case "snowball": {
                return new SnowballItem(deserializeProperties(object));
            }
            case "soup": {
                return new SoupItem(deserializeProperties(object));
            }
            case "spawn_egg": {
                return new SpawnEggItem(JSONHelper.getEntity(JSONUtils.getString(object, "entity")),
                        JSONUtils.getInt(object, "primary_color"), JSONUtils.getInt(object, "secondary_color"),
                        deserializeProperties(object));
            }
            case "spectral_arrow": {
                return new SpectralArrowItem(deserializeProperties(object));
            }
            case "splash_potion": {
                return new SplashPotionItem(deserializeProperties(object));
            }
            case "suspicious_stew": {
                return new SuspiciousStewItem(deserializeProperties(object));
            }
            case "sword": {
                if (object.has("item_tier")) {
                    if (object.get("item_tier").isJsonObject()) {
                        JsonObject itemTier = JSONUtils.getJsonObject(object, "item_tier");
                        return new SwordItem(deserializeItemTier(itemTier), JSONUtils.getInt(object, "attack_damage"),
                                JSONUtils.getFloat(object, "attack_speed"), deserializeProperties(object));
                    } else {
                        return new SwordItem(
                                itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "item_tier"))),
                                JSONUtils.getInt(object, "attack_damage"), JSONUtils.getFloat(object, "attack_speed"),
                                deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing item tier, expected to find a JsonObject or a String");
                }
            }
            case "tall_block": {
                return new TallBlockItem(JSONHelper.getBlock(JSONUtils.getString(object, "block")),
                        deserializeProperties(object));
            }
            case "throwable_potion": {
                return new ThrowablePotionItem(deserializeProperties(object));
            }
            case "tiered": {
                if (object.has("item_tier")) {
                    if (object.get("item_tier").isJsonObject()) {
                        JsonObject itemTier = JSONUtils.getJsonObject(object, "item_tier");
                        return new TieredItem(deserializeItemTier(itemTier), deserializeProperties(object));
                    } else {
                        return new TieredItem(
                                itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "item_tier"))),
                                deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing item tier, expected to find a JsonObject or a String");
                }
            }
            case "tipped_arrow": {
                return new TippedArrowItem(deserializeProperties(object));
            }
            case "tool": {
                Set<Block> effectiveBlocks = Sets.newHashSet();
                JSONUtils.getJsonArray(object, "effective_blocks").forEach((element) -> {
                    effectiveBlocks.add(JSONHelper.getBlock(JSONUtils.getString(element, "block")));
                });
                if (object.has("item_tier")) {
                    if (object.get("item_tier").isJsonObject()) {
                        JsonObject itemTier = JSONUtils.getJsonObject(object, "item_tier");
                        return new ToolItem(JSONUtils.getFloat(object, "attack_damage"),
                                JSONUtils.getFloat(object, "attack_speed"), deserializeItemTier(itemTier),
                                effectiveBlocks, deserializeProperties(object));
                    } else {
                        return new ToolItem(JSONUtils.getFloat(object, "attack_damage"),
                                JSONUtils.getFloat(object, "attack_speed"),
                                itemTierMap.get(new ResourceLocation(JSONUtils.getString(object, "item_tier"))),
                                effectiveBlocks, deserializeProperties(object));
                    }
                } else {
                    throw new JsonSyntaxException("Missing item tier, expected to find a JsonObject or a String");
                }
            }
            case "trident": {
                return new TridentItem(deserializeProperties(object));
            }
            case "wall_or_floor": {
                return new WallOrFloorItem(JSONHelper.getBlock(JSONUtils.getString(object, "floor_block")),
                        JSONHelper.getBlock(JSONUtils.getString(object, "wall_block")), deserializeProperties(object));
            }
            case "writable_book": {
                return new WritableBookItem(deserializeProperties(object));
            }
            case "written_book": {
                return new WrittenBookItem(deserializeProperties(object));
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

    public static AbstractMinecartEntity.Type deserializeMinecartType(String minecartType) throws JsonSyntaxException {
        switch (minecartType) {
            case "rideable": {
                return AbstractMinecartEntity.Type.RIDEABLE;
            }
            case "chest": {
                return AbstractMinecartEntity.Type.CHEST;
            }
            case "furnace": {
                return AbstractMinecartEntity.Type.FURNACE;
            }
            case "tnt": {
                return AbstractMinecartEntity.Type.TNT;
            }
            case "spawner": {
                return AbstractMinecartEntity.Type.SPAWNER;
            }
            case "hopper": {
                return AbstractMinecartEntity.Type.HOPPER;
            }
            case "command_block": {
                return AbstractMinecartEntity.Type.COMMAND_BLOCK;
            }
            default: {
                throw new JsonSyntaxException("Unknown minecart type '" + minecartType + "'");
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
            properties.addToolType(ToolType.get(JSONUtils.getString(harvesting, "tool_type")),
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
            Food.Builder food = new Food.Builder().hunger(JSONUtils.getInt(newFood, "restores"))
                    .saturation(JSONUtils.getFloat(newFood, "saturation"));
            if (newFood.has("meat")) {
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
                        return new EffectInstance(JSONHelper.setRequiredEffectElement(effectObj, "effect"),
                                JSONUtils.getInt(effectObj, "duration"), JSONUtils.getInt(effectObj, "amplifier"));
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
        if (propertiesObj.has("repairable")) {
            if (JSONUtils.getBoolean(propertiesObj, "repairable") == false) {
                properties.setNoRepair();
            }
        }
        return properties;
    }

}