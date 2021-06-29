package chromakey.devsdream.crafting;

import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.JsonUtils;

public class SmithingNBTRecipe extends SmithingRecipe {

    private final CompoundNBT nbt;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack recipeOutput;

    public SmithingNBTRecipe(ResourceLocation recipeId, Ingredient base, Ingredient addition, ItemStack result, CompoundNBT nbt) {
        super(recipeId, base, addition, result);
        this.base = base;
        this.addition = addition;
        this.nbt = nbt;
        this.recipeOutput = result;
    }

    public ItemStack getRecipeOutput() {
        ItemStack output = this.recipeOutput;
        output.setTag(this.nbt);
        return output;
    }

    public ItemStack getCraftingResult(IInventory inv) {
      ItemStack itemstack = this.getRecipeOutput().copy();
      CompoundNBT compoundnbt = inv.getStackInSlot(0).getTag();
      if (compoundnbt != null) {
         itemstack.getOrCreateTag().merge(compoundnbt.copy());
      }

      return itemstack;
   }

    public IRecipeSerializer<?> getSerializer() {
        return Serializers.SMITHING_NBT;
     }

     public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SmithingNBTRecipe> {
        public SmithingNBTRecipe read(ResourceLocation recipeId, JsonObject json) {
           Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "base"));
           Ingredient ingredient1 = Ingredient.deserialize(JSONUtils.getJsonObject(json, "addition"));
           ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
           CompoundNBT nbt = JsonUtils.readNBT(JSONUtils.getJsonObject(json, "result"), "nbt");
           return new SmithingNBTRecipe(recipeId, ingredient, ingredient1, itemstack, nbt);
        }
  
        public SmithingNBTRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
           Ingredient ingredient = Ingredient.read(buffer);
           Ingredient ingredient1 = Ingredient.read(buffer);
           ItemStack itemstack = buffer.readItemStack();
           return new SmithingNBTRecipe(recipeId, ingredient, ingredient1, itemstack, itemstack.getTag());
        }
  
        public void write(PacketBuffer buffer, SmithingNBTRecipe recipe) {
           recipe.base.write(buffer);
           recipe.addition.write(buffer);
           buffer.writeItemStack(recipe.getRecipeOutput());
        }
     }
    
}