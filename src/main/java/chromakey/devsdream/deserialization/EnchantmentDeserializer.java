package chromakey.devsdream.deserialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.util.CustomEnchantment;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.JSONUtils;

public class EnchantmentDeserializer {

    public static Enchantment deserializeEnchantment(JsonObject object) throws JsonSyntaxException {

        String rarityJson = JSONUtils.getString(object, "rarity");
        String typeJson = JSONUtils.getString(object, "type");
        JsonArray slotsJson = JSONUtils.getJsonArray(object, "slots");

        Rarity rarity;
        EnchantmentType type;
        EquipmentSlotType[] slots = new EquipmentSlotType[slotsJson.size()];

        switch (rarityJson) {
            case "common": {
                rarity = Rarity.COMMON;
                break;
            }
            case "uncommon": {
                rarity = Rarity.UNCOMMON;
                break;
            }
            case "rare": {
                rarity = Rarity.RARE;
                break;
            }
            case "very_rare": {
                rarity = Rarity.VERY_RARE;
                break;
            }
            default: {
                throw new JsonSyntaxException("Unknown rarity '" + rarityJson + "'");
            }
        }

        switch (JSONUtils.getString(object, "type")) {
            case "armor": {
                type = EnchantmentType.ARMOR;
                break;
            }
            case "armor_feet": {
                type = EnchantmentType.ARMOR_FEET;
                break;
            }
            case "armor_chest": {
                type = EnchantmentType.ARMOR_CHEST;
                break;
            }
            case "armor_head": {
                type = EnchantmentType.ARMOR_HEAD;
                break;
            }
            case "weapon": {
                type = EnchantmentType.WEAPON;
                break;
            }
            case "digger": {
                type = EnchantmentType.DIGGER;
                break;
            }
            case "fishing_rod": {
                type = EnchantmentType.FISHING_ROD;
                break;
            }
            case "trident": {
                type = EnchantmentType.TRIDENT;
                break;
            }
            case "breakable": {
                type = EnchantmentType.BREAKABLE;
                break;
            }
            case "bow": {
                type = EnchantmentType.BOW;
                break;
            }
            case "wearable": {
                type = EnchantmentType.WEARABLE;
                break;
            }
            case "crossbow": {
                type = EnchantmentType.CROSSBOW;
                break;
            }
            case "vanishable": {
                type = EnchantmentType.VANISHABLE;
                break;
            }
            default: {
                throw new JsonSyntaxException("Unknown type '" + typeJson + "'");
            }
        }

        for (int i = 0; i < slots.length; i++) {
            slots[i] = JSONHelper.setRequiredSlotElement(slotsJson.get(i));
        }

        return new CustomEnchantment(rarity, type, slots, false, 0, 0, 0, 0, null, false, false);

    }
}