package chromakey.devsdream.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.JsonUtils;

public class ShapelessNBTRecipe extends ShapelessRecipe {

    private final ItemStack recipeOutput;
    private final CompoundNBT nbt;

    public ShapelessNBTRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn,
            NonNullList<Ingredient> recipeItemsIn, CompoundNBT nbt) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
        this.recipeOutput = recipeOutputIn;
        this.nbt = nbt;
    }

    public IRecipeSerializer<?> getSerializer() {
        return Serializers.CRAFTING_SHAPELESS_NBT;
    }

    public ItemStack getRecipeOutput() {
        ItemStack output = this.recipeOutput;
        output.setTag(this.nbt);
        return output;
     }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<ShapelessNBTRecipe> {

        @Override
        public ShapelessNBTRecipe read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 3 * 3) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (3 * 3));
            } else {
                CompoundNBT nbt = JsonUtils.readNBT(JSONUtils.getJsonObject(json, "result"), "nbt");
                ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
                return new ShapelessNBTRecipe(recipeId, s, itemstack, nonnulllist, nbt);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray p_199568_0_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(p_199568_0_.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

      public ShapelessNBTRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
         String s = buffer.readString(32767);
         int i = buffer.readVarInt();
         NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

         for(int j = 0; j < nonnulllist.size(); ++j) {
            nonnulllist.set(j, Ingredient.read(buffer));
         }

         ItemStack itemstack = buffer.readItemStack();
         return new ShapelessNBTRecipe(recipeId, s, itemstack, nonnulllist, itemstack.getTag());
      }

        public void write(PacketBuffer buffer, ShapelessNBTRecipe recipe) {
            buffer.writeString(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.getRecipeOutput());
        }
    }
}