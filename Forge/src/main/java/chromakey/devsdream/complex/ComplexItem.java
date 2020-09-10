package chromakey.devsdream.complex;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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
    private final ResourceLocation rightClickPredicateMainhand;
    private final ResourceLocation rightClickPredicateOffhand;
    private final String appendToKeyTag;
    private final int useDuration;
    private final UseAction useAction;
    private final ResourceLocation onItemUseFinishFunction;
    private final Item incrementRightClickStatistic;
    private final ResourceLocation inventoryTickFunction;
    private final int inventoryTickSlot;
    private final boolean inventoryTickSelected;
    private final boolean inventoryTickSlotRequired;

    public ComplexItem(Properties properties, List<ITextComponent> tooltip, boolean hasEffect, int enchantability,
            boolean canBreakBlocks, @Nullable ResourceLocation onUseFunction,
            @Nullable ResourceLocation rightClickFunctionMainhand, @Nullable ResourceLocation rightClickFunctionOffhand,
            @Nullable ResourceLocation rightClickPredicateMainhand, @Nullable ResourceLocation rightClickPredicateOffhand, String appendToKeyTag, int useDuration, UseAction useAction,
            @Nullable ResourceLocation onItemUseFinishFunction, @Nullable Item incrementRightClickStatistic, @Nullable ResourceLocation inventoryTickFunction, boolean inventoryTickSelected, int inventoryTickSlot, boolean inventoryTickSlotRequired) {
        super(properties);
        this.tooltip = tooltip;
        this.hasEffect = hasEffect;
        this.enchantability = enchantability;
        this.canBreakBlocks = canBreakBlocks;
        this.onUseFunction = onUseFunction;
        this.rightClickFunctionMainhand = rightClickFunctionMainhand;
        this.rightClickFunctionOffhand = rightClickFunctionOffhand;
        this.rightClickPredicateMainhand = rightClickPredicateMainhand;
        this.rightClickPredicateOffhand = rightClickPredicateOffhand;
        this.appendToKeyTag = appendToKeyTag;
        this.useDuration = useDuration;
        this.useAction = useAction;
        this.onItemUseFinishFunction = onItemUseFinishFunction;
        this.incrementRightClickStatistic = incrementRightClickStatistic;
        this.inventoryTickFunction = inventoryTickFunction;
        this.inventoryTickSlot = inventoryTickSlot;
        this.inventoryTickSlotRequired = inventoryTickSlotRequired;
        this.inventoryTickSelected = inventoryTickSelected;
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
            runFunction(worldIn, livingEntityIn, this.onUseFunction);
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
                if ((this.rightClickPredicateMainhand == null && handIn == Hand.MAIN_HAND) || (this.rightClickPredicateOffhand == null && handIn == Hand.OFF_HAND)) {
                    flag = true;
                } else {
                    if (this.rightClickPredicateMainhand == this.rightClickPredicateOffhand || handIn == Hand.MAIN_HAND) {
                        flag = evaluatePredicate(worldIn, playerIn, this.rightClickPredicateMainhand);
                    } else {
                        flag = evaluatePredicate(worldIn, playerIn, this.rightClickPredicateOffhand);
                    }
                }
                if (flag == true) {
                    if (this.incrementRightClickStatistic == null) {
                        playerIn.addStat(Stats.ITEM_USED.get(this));
                    } else {
                        playerIn.addStat(Stats.ITEM_USED.get(this.incrementRightClickStatistic));
                    }
                    if (handIn == Hand.MAIN_HAND) {
                        runFunction(worldIn, playerIn, this.rightClickFunctionMainhand);
                    }
                    if (handIn == Hand.OFF_HAND) {
                        runFunction(worldIn, playerIn, this.rightClickFunctionOffhand);
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

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!worldIn.isRemote() && this.inventoryTickFunction != null) {
            if (this.inventoryTickSelected) {
                if (isSelected) {
                    if (this.inventoryTickSlotRequired) {
                        if (this.inventoryTickSlot == itemSlot) {
                            runFunction(worldIn, entityIn, this.inventoryTickFunction);
                        }
                    } else {
                        runFunction(worldIn, entityIn, this.inventoryTickFunction);
                    }
                }
            } else {
                if (this.inventoryTickSlotRequired) {
                    if (this.inventoryTickSlot == itemSlot) {
                        runFunction(worldIn, entityIn, this.inventoryTickFunction);
                    }
                } else {
                    runFunction(worldIn, entityIn, this.inventoryTickFunction);
                }
            }
        } else {
            super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
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

    private static boolean evaluatePredicate(World worldIn, PlayerEntity playerIn, ResourceLocation predicate) {
        try {
            return worldIn.getServer().func_229736_aP_().func_227517_a_(predicate)
                    .test(new LootContext.Builder(
                            worldIn.getServer().getWorld(playerIn.getEntityWorld().getDimensionKey()))
                                    .withParameter(LootParameters.THIS_ENTITY, playerIn)
                                    .withParameter(LootParameters.field_237457_g_,
                                            playerIn.getPositionVec())
                                    .build(LootParameterSets.COMMAND));
        } catch (NullPointerException e) {
            return true;
        }
    }

    private static void runFunction(World worldIn, Entity source, ResourceLocation function) {
        try {
            FunctionManager manager = worldIn.getServer().getFunctionManager();
            manager.execute(manager.get(function).get(),
                    source.getCommandSource().withFeedbackDisabled());
        } catch (NoSuchElementException e) {
        }
    }
}
