package chromakey.devsdream.deserialization;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.util.JSONUtils;

public class AttributeModifierDeserializer {

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

    public static AttributeModifier deserializeAttributeModifier(JsonObject object) {
        return new AttributeModifier(UUID.randomUUID(), "missingno", (double) JSONUtils.getFloat(object, "amount"), deserializeOperation(object));
    }
}