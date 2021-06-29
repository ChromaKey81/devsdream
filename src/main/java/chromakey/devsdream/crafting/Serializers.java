package chromakey.devsdream.crafting;

import net.minecraft.item.crafting.IRecipeSerializer;

public class Serializers {

    public static IRecipeSerializer<ShapelessNBTRecipe> CRAFTING_SHAPELESS_NBT = new ShapelessNBTRecipe.Serializer();
    public static IRecipeSerializer<ShapedNBTRecipe> CRAFTING_SHAPED_NBT = new ShapedNBTRecipe.Serializer();
    public static IRecipeSerializer<SmithingNBTRecipe> SMITHING_NBT = new SmithingNBTRecipe.Serializer();

    
}