package chromakey.devsdream.custom;

import net.minecraft.block.PoweredRailBlock;

public class CustomPoweredRail extends PoweredRailBlock {

    public CustomPoweredRail(Properties builder, boolean isActivatorRail) {
        super(builder, !isActivatorRail);
    }
    
}