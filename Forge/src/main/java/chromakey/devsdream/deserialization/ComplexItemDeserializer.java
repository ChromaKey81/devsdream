package chromakey.devsdream.deserialization;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import chromakey.devsdream.complex.ComplexItem;
import chromakey.devsdream.deserialization.ComplexItemDeserializer;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.item.Item;
import net.minecraft.item.UseAction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ComplexItemDeserializer {
    
    public static ComplexItem deserializeComplexItem(JsonObject object) throws JsonSyntaxException {
        List<ITextComponent> tooltips = Lists.newArrayList();
        boolean hasEffect = false;
        int enchantability = 0;
        boolean canBreakBlocks = true;
        ResourceLocation onUseFunction = null;
        ResourceLocation rightClickFunctionMainhand = null;
        ResourceLocation rightClickFunctionOffhand = null;
        ResourceLocation rightClickPredicate = null;
        String appendToKeyTag = null;
        int useDuration = 0;
        UseAction useAction = UseAction.NONE;
        ResourceLocation onItemUseFinishFunction = null;
        List<JsonObject> tagTooltips = Lists.newArrayList();
        Item incrementRightClickStatistic = null;
        if (object.has("information")) {
            JSONUtils.getJsonArray(object, "information").iterator().forEachRemaining((tooltip) -> {
                tooltips.add(ITextComponent.Serializer.func_240641_a_(tooltip));
            });
        }
        if (object.has("information_from_tags")) {
            JSONUtils.getJsonArray(object, "information_from_tags").iterator().forEachRemaining((tooltip) -> {
                tagTooltips.add(JSONUtils.getJsonObject(tooltip, "JsonObject"));
            });
        }
        if (object.has("has_effect")) {
            hasEffect = JSONUtils.getBoolean(object, "has_effect");
        }
        if (object.has("enchantability")) {
            enchantability = JSONUtils.getInt(object, "enchantability");
        }
        if (object.has("can_break_blocks")) {
            canBreakBlocks = JSONUtils.getBoolean(object, "can_break_blocks");
        }
        if (object.has("right_click")) {
            JsonObject rightClick = JSONUtils.getJsonObject(object, "right_click");
            JsonElement function = rightClick.get("function");
            if (function == null) {
                throw new JsonSyntaxException("Missing function, expected to find a JsonObject or a String");
            } else {
                if (function.isJsonObject()) {
                    JsonObject functionObj = JSONUtils.getJsonObject(rightClick, "function");
                    if (functionObj.has("mainhand")) {
                        rightClickFunctionMainhand = new ResourceLocation(JSONUtils.getString(functionObj, "mainhand"));
                    }
                    if (functionObj.has("offhand")) {
                        rightClickFunctionOffhand = new ResourceLocation(JSONUtils.getString(functionObj, "offhand"));
                    }
                } else {
                    ResourceLocation rightClickFunction = new ResourceLocation(JSONUtils.getString(function, "function"));
                    rightClickFunctionMainhand = rightClickFunction;
                    rightClickFunctionOffhand = rightClickFunction;
                }
            }
            if (rightClick.has("predicate")) {
                rightClickPredicate = new ResourceLocation(JSONUtils.getString(rightClick, "predicate"));
            }
            if (rightClick.has("increment_statistic")) {
                incrementRightClickStatistic = JSONHelper.setRequiredItemElement(rightClick, "increment_statistic");
            }
        }
        if (object.has("append_to_key_tag")) {
            appendToKeyTag = JSONUtils.getString(object, "append_to_key_tag");
        }
        if (object.has("use_duration")) {
            useDuration = JSONUtils.getInt(object, "use_duration");
        }
        if (object.has("use_action")) {
            String action = JSONUtils.getString(object, "use_action");
            switch (action) {
                case "none": {
                    useAction = UseAction.NONE;
                    break;
                }
                case "eat": {
                    useAction = UseAction.EAT;
                    break;
                }
                case "drink": {
                    useAction = UseAction.DRINK;
                    break;
                }
                case "block": {
                    useAction = UseAction.BLOCK;
                    break;
                }
                case "bow": {
                    useAction = UseAction.BOW;
                    break;
                }
                case "spear": {
                    useAction = UseAction.SPEAR;
                    break;
                }
                case "crossbow": {
                    useAction = UseAction.CROSSBOW;
                    break;
                }
                default: {
                    throw new JsonSyntaxException("Unknown use action '" + action + "'");
                }
            }
        }
        if (object.has("on_item_use_finish")) {
            onItemUseFinishFunction = new ResourceLocation(JSONUtils.getString(object, "on_item_use_finish"));
        }
        return new ComplexItem(ItemDeserializer.deserializeProperties(object), tooltips, hasEffect, enchantability, canBreakBlocks, onUseFunction, rightClickFunctionMainhand, rightClickFunctionOffhand, rightClickPredicate, appendToKeyTag, useDuration, useAction, onItemUseFinishFunction, incrementRightClickStatistic);
    }
}
