package chromakey.devsdream.deserialization;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.util.CustomEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.JSONUtils;

public class EffectDeserializer {
    
    public static Effect deserializeEffect(JsonObject object) throws JsonSyntaxException {
        final EffectType effectType;
        final int liquidColor = JSONUtils.getInt(object, "color");
        boolean instant = false;
        if (object.has("instant")) {
            instant = JSONUtils.getBoolean(object, "instant");
        }
        String type = JSONUtils.getString(object, "type");
        switch (type) {
            case "beneficial": {
                effectType = EffectType.BENEFICIAL;
                break;
            }
            case "harmful": {
                effectType = EffectType.HARMFUL;
                break;
            }
            case "neutral": {
                effectType = EffectType.NEUTRAL;
                break;
            }
            default: {
                throw new JsonSyntaxException("Unknown effect type '" + type + "'");
            }
        }
        if (object.has("modifier")) {
            JsonObject modifier = JSONUtils.getJsonObject(object, "modifier");
            return new CustomEffect(effectType, liquidColor, instant, AttributeDeserializer.deserializeAttribute(modifier), JSONUtils.getString(modifier, "uuid"), (double) JSONUtils.getFloat(modifier, "amount"), AttributeDeserializer.deserializeOperation(modifier));
        } else {
            return new CustomEffect(effectType, liquidColor, instant);
        }
    }
}