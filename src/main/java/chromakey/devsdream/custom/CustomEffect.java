package chromakey.devsdream.custom;

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

    public CustomEffect(EffectType typeIn, int liquidColorIn, boolean instant, Attribute attribute, String modifierUUID, double modifierAmount, AttributeModifier.Operation operation) {
        super(typeIn, liquidColorIn);
        this.instant = instant;
        this.addAttributesModifier(attribute, modifierUUID, modifierAmount, operation);
    }

    public boolean isInstant() {
        return this.instant;
    }

}