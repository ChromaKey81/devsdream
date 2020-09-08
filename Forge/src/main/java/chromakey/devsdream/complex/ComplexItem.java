package chromakey.devsdream.complex;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ComplexItem extends Item {

    private final List<ITextComponent> tooltip;
    private final boolean hasEffect;
    private final int enchantability;
    private final boolean canBreakBlocks;
    private final ResourceLocation onUseFunction;
    private final ResourceLocation rightClickFunctionMainhand;
    private final ResourceLocation rightClickFunctionOffhand;
    private final ResourceLocation rightClickPredicate;
    private final String appendToKeyTag;
    private final int useDuration;
    private final UseAction useAction;
    private final ResourceLocation onItemUseFinishFunction;
    private final boolean incrementRightClickStatistic;

    public ComplexItem(Properties properties, List<ITextComponent> tooltip, boolean hasEffect, int enchantability,
            boolean canBreakBlocks, @Nullable ResourceLocation onUseFunction,
            @Nullable ResourceLocation rightClickFunctionMainhand, @Nullable ResourceLocation rightClickFunctionOffhand,
            @Nullable ResourceLocation rightClickPredicate, String appendToKeyTag, int useDuration, UseAction useAction,
            @Nullable ResourceLocation onItemUseFinishFunction, boolean incrementRightClickStatistic) {
        super(properties);
        this.tooltip = tooltip;
        this.hasEffect = hasEffect;
        this.enchantability = enchantability;
        this.canBreakBlocks = canBreakBlocks;
        this.onUseFunction = onUseFunction;
        this.rightClickFunctionMainhand = rightClickFunctionMainhand;
        this.rightClickFunctionOffhand = rightClickFunctionOffhand;
        this.rightClickPredicate = rightClickPredicate;
        this.appendToKeyTag = appendToKeyTag;
        this.useDuration = useDuration;
        this.useAction = useAction;
        this.onItemUseFinishFunction = onItemUseFinishFunction;
        this.incrementRightClickStatistic = incrementRightClickStatistic;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
            ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        this.tooltip.iterator().forEachRemaining((line) -> {
            tooltip.add(line);
        });
    }

    public boolean hasEffect(ItemStack stack) {
        if (this.hasEffect == false) {
            return super.hasEffect(stack);
        } else {
            return this.hasEffect;
        }
    }

    public int getItemEnchantability() {
        return this.enchantability;
    }

    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return this.canBreakBlocks;
    }

    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (this.onUseFunction != null && worldIn.isRemote()) {
            FunctionManager manager = worldIn.getServer().getFunctionManager();
            try {
                manager.execute(manager.get(this.onUseFunction).get(),
                        livingEntityIn.getCommandSource().withFeedbackDisabled());
            } catch (NoSuchElementException e) {
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if ((this.isFood() && playerIn.canEat(this.getFood().canEatWhenFull()))
                || (this.rightClickFunctionMainhand == null && handIn == Hand.MAIN_HAND)
                || (this.rightClickFunctionOffhand == null && handIn == Hand.OFF_HAND)) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        } else {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            boolean flag;
            if (!worldIn.isRemote) {
                if (this.rightClickPredicate == null) {
                    flag = true;
                } else {
                    try {
                        flag = worldIn.getServer().func_229736_aP_().func_227517_a_(this.rightClickPredicate)
                                .test(new LootContext.Builder(
                                        worldIn.getServer().getWorld(playerIn.getEntityWorld().getDimensionKey()))
                                                .withParameter(LootParameters.THIS_ENTITY, playerIn)
                                                .withParameter(LootParameters.field_237457_g_,
                                                        playerIn.getPositionVec())
                                                .build(LootParameterSets.COMMAND));
                    } catch (NullPointerException e) {
                        flag = true;
                    }
                }
                if (flag == true) {
                    if (this.incrementRightClickStatistic) {
                        playerIn.addStat(Stats.ITEM_USED.get(this));
                    }
                    FunctionManager manager = worldIn.getServer().getFunctionManager();
                    if (handIn == Hand.MAIN_HAND) {
                        try {
                            manager.execute(manager.get(this.rightClickFunctionMainhand).get(),
                                    playerIn.getCommandSource().withFeedbackDisabled());
                        } catch (NoSuchElementException e) {
                        }
                    }
                    if (handIn == Hand.OFF_HAND) {
                        try {
                            manager.execute(manager.get(this.rightClickFunctionOffhand).get(),
                                    playerIn.getCommandSource().withFeedbackDisabled());
                        } catch (NoSuchElementException e) {
                        }
                    }
                    return ActionResult.resultSuccess(itemstack);
                } else {
                    return ActionResult.resultPass(itemstack);
                }
            } else {
                return ActionResult.resultPass(itemstack);
            }
        }
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (this.isFood() || this.onItemUseFinishFunction == null) {
            return entityLiving.onFoodEaten(worldIn, stack);
        } else {
            if (!worldIn.isRemote()) {
                FunctionManager manager = worldIn.getServer().getFunctionManager();
                try {
                    manager.execute(manager.get(this.onItemUseFinishFunction).get(),
                            entityLiving.getCommandSource().withFeedbackDisabled());
                } catch (NoSuchElementException e) {
                }
            }
            return stack;
        }
    }

    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }

    public UseAction getUseAction(ItemStack stack) {
        return this.useAction;
    }

    public String getTranslationKey(ItemStack stack) {
        if (this.appendToKeyTag == null) {
            return super.getTranslationKey(stack);
        } else {
            try {
                return this.getTranslationKey() + "." + this.appendToKeyTag + "."
                        + stack.getTag().getString(this.appendToKeyTag);
            } catch (NullPointerException e) {
                return super.getTranslationKey(stack);
            }
        }
    }
}
