package chromakey.devsdream.deserialization;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeDeserializer {

    public static Attribute deserializeAttribute(JsonObject object) throws JsonSyntaxException {
        String argument = JSONUtils.getString(object, "attribute");
        ResourceLocation resourcelocation = new ResourceLocation(argument);
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(resourcelocation);
        if (attribute == null) {
            throw new JsonSyntaxException("Unknown attribute '" + argument + "'");
        } else {
            return attribute;
        }
    }

    public static Operation deserializeOperation(JsonObject object) throws JsonSyntaxException {
        String operation = JSONUtils.getString(object, "operation");
        switch (operation) {
            case "add": {
                return Operation.ADDITION;
            }
            case "multiply_base": {
                return Operation.MULTIPLY_BASE;
            }
            case "multiply": {
                return Operation.MULTIPLY_TOTAL;
            }
            default: {
                throw new JsonSyntaxException("Unknown operation '" + operation + "'");
            }
        }
    }
}