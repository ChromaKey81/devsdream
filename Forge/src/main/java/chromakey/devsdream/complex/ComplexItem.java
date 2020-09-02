package chromakey.devsdream.complex;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ComplexItem extends Item {

    private final List<ITextComponent> tooltip;

    public ComplexItem(Properties properties, List<ITextComponent> tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        this.tooltip.iterator().forEachRemaining((line) -> {
            tooltip.add(line);
        });
    }
    
}
