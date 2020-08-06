package chromakey.devsdream.util;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class CustomEnchantment extends Enchantment {

    private final int minLevel;
    private final int maxLevel;
    private final int minEnchantability;
    private final int maxEnchantability;
    private final boolean isAllowedOnBooks;
    private boolean enchantabilityMultiply = false;

    public CustomEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots, boolean curse,
            int minLevel, int maxLevel, int minEnchantability, int maxEnchantability,
            List<Enchantment> incompatibleWith, boolean isAllowedOnBooks, boolean enchantabilityMultiply) {
        super(rarityIn, typeIn, slots);
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.minEnchantability = minEnchantability;
        this.maxEnchantability = maxEnchantability;
        this.isAllowedOnBooks = isAllowedOnBooks;
        this.enchantabilityMultiply = enchantabilityMultiply;
    }

    @Override
    public int getMinLevel() {
        return this.minLevel;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public int getMinEnchantability(int enchantmentLevel) {
        if (this.enchantabilityMultiply == true) {
            return enchantmentLevel * (1 + this.minEnchantability);
        } else {
            return enchantmentLevel + this.minEnchantability;
        }
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        if (this.enchantabilityMultiply == true) {
            return this.getMinEnchantability(enchantmentLevel) * (1 + this.maxEnchantability);
        } else {
            return this.getMinEnchantability(enchantmentLevel) + this.maxEnchantability;
        }
    }

    public boolean isAllowedOnBooks() {
        return this.isAllowedOnBooks;
    }
}