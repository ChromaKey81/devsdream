package chromakey.devsdream.deserialization;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.custom.CustomEffect;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
        if (object.has("modifiers")) {
            Map<Attribute, AttributeModifier> modifierMap = Maps.newHashMap();
            JsonArray modifiers = JSONUtils.getJsonArray(object, "modifiers");
            modifiers.iterator().forEachRemaining((modifier) -> {
                JsonObject modifierObj = JSONUtils.getJsonObject(modifier, "attribute modifier");
                modifierMap.put(JSONHelper.deserializeAttribute(modifierObj, "attribute"), AttributeModifierDeserializer.deserializeAttributeModifier(modifierObj));
            });
            return new CustomEffect(effectType, liquidColor, instant, modifierMap);
        } else {
            return new CustomEffect(effectType, liquidColor, instant);
        }
    }
}