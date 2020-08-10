package chromakey.devsdream.custom;

import java.util.Map;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class CustomEffect extends Effect {
    
    private final boolean instant;

    public CustomEffect(EffectType typeIn, int liquidColorIn, boolean instant) {
        super(typeIn, liquidColorIn);
        this.instant = instant;
    }

    public CustomEffect(EffectType typeIn, int liquidColorIn, boolean instant, Map<Attribute, AttributeModifier> modifierMap) {
        super(typeIn, liquidColorIn);
        this.instant = instant;
        modifierMap.forEach((attribute, modifier) -> {
            this.addAttributesModifier(attribute, modifier.getID().toString(), modifier.getAmount(), modifier.getOperation());
        });
    }

    public boolean isInstant() {
        return this.instant;
    }



}