package chromakey.devsdream.complex;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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
    private final Item repairItem;
    private final Block useOnBlock;
    private final ResourceLocation useOnBlockFunction;
    private final ResourceLocation useOnBlockPredicate;

    public ComplexItem(Properties properties, List<ITextComponent> tooltip, boolean hasEffect, int enchantability,
            boolean canBreakBlocks, @Nullable ResourceLocation onUseFunction,
            @Nullable ResourceLocation rightClickFunctionMainhand, @Nullable ResourceLocation rightClickFunctionOffhand,
            @Nullable ResourceLocation rightClickPredicateMainhand,
            @Nullable ResourceLocation rightClickPredicateOffhand, String appendToKeyTag, int useDuration,
            UseAction useAction, @Nullable ResourceLocation onItemUseFinishFunction,
            @Nullable Item incrementRightClickStatistic, @Nullable ResourceLocation inventoryTickFunction,
            boolean inventoryTickSelected, int inventoryTickSlot, boolean inventoryTickSlotRequired, @Nullable Item repairItem, @Nullable Block useOnBlock, @Nullable ResourceLocation useOnBlockFunction, float compostChance, @Nullable ResourceLocation useOnBlockPredicate) {
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
        this.repairItem = repairItem;
        this.useOnBlock = useOnBlock;
        this.useOnBlockFunction = useOnBlockFunction;
        if(compostChance > 0) {
            ComposterBlock.CHANCES.put(this, compostChance);
        }
        this.useOnBlockPredicate = useOnBlockPredicate;
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
            runFunction(worldIn, livingEntityIn, this.onUseFunction, livingEntityIn.getPositionVec());
        }
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (this.repairItem != null) {
            return repair.getItem() == this.repairItem;
        } else {
            return super.getIsRepairable(toRepair, repair);
        }
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (this.useOnBlock != null && this.useOnBlockFunction != null && !world.isRemote()) {
            BlockPos blockpos = context.getPos();
            Entity entity = context.getPlayer();
            BlockState blockstate = world.getBlockState(blockpos);
            Vector3d blockVec = new Vector3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            if (blockstate.isIn(this.useOnBlock)) {
                if (this.useOnBlockPredicate == null) {
                    runFunction(world, entity, this.useOnBlockFunction, blockVec);
                    return ActionResultType.func_233537_a_(world.isRemote);
                } else if (evaluatePredicate(world, entity, this.useOnBlockPredicate, blockVec)) {
                    runFunction(world, entity, this.useOnBlockFunction, blockVec);
                    return ActionResultType.func_233537_a_(world.isRemote);
                } else {
                    return ActionResultType.PASS;
                }
            } else {
                return ActionResultType.PASS;
            }
        } else {
            return super.onItemUse(context);
        }
     }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if ((this.rightClickFunctionMainhand == null && handIn == Hand.MAIN_HAND)
                || (this.rightClickFunctionOffhand == null && handIn == Hand.OFF_HAND)) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        } else {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            boolean flag;
            if (!worldIn.isRemote) {
                if ((this.rightClickPredicateMainhand == null && handIn == Hand.MAIN_HAND)
                        || (this.rightClickPredicateOffhand == null && handIn == Hand.OFF_HAND)) {
                    flag = true;
                } else {
                    if (this.rightClickPredicateMainhand == this.rightClickPredicateOffhand
                            || handIn == Hand.MAIN_HAND) {
                        flag = evaluatePredicate(worldIn, playerIn, this.rightClickPredicateMainhand, playerIn.getPositionVec());
                    } else {
                        flag = evaluatePredicate(worldIn, playerIn, this.rightClickPredicateOffhand, playerIn.getPositionVec());
                    }
                }
                if (flag == true) {
                    if (this.incrementRightClickStatistic == null) {
                        playerIn.addStat(Stats.ITEM_USED.get(this));
                    } else {
                        playerIn.addStat(Stats.ITEM_USED.get(this.incrementRightClickStatistic));
                    }
                    if (handIn == Hand.MAIN_HAND) {
                        runFunction(worldIn, playerIn, this.rightClickFunctionMainhand, playerIn.getPositionVec());
                    }
                    if (handIn == Hand.OFF_HAND) {
                        runFunction(worldIn, playerIn, this.rightClickFunctionOffhand, playerIn.getPositionVec());
                    }
                    return ActionResult.resultSuccess(itemstack);
                } else {
                    return super.onItemRightClick(worldIn, playerIn, handIn);
                }
            } else {
                return ActionResult.resultPass(itemstack);
            }
        }
    }

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        EquipmentSlotType inventoryTickEquipmentSlot = getEquipmentSlotType(this.inventoryTickSlot);
        if (!worldIn.isRemote() && this.inventoryTickFunction != null) {
            if (this.inventoryTickSelected) {
                if (isSelected) {
                    if (this.inventoryTickSlotRequired) {
                        if (this.inventoryTickSlot == itemSlot) {
                            runFunction(worldIn, entityIn, this.inventoryTickFunction, entityIn.getPositionVec());
                        } else if (inventoryTickEquipmentSlot != null && entityIn instanceof LivingEntity
                                && ((LivingEntity) entityIn)
                                        .getItemStackFromSlot(inventoryTickEquipmentSlot) == stack) {
                            runFunction(worldIn, entityIn, this.inventoryTickFunction, entityIn.getPositionVec());
                        }
                    } else {
                        runFunction(worldIn, entityIn, this.inventoryTickFunction, entityIn.getPositionVec());
                    }
                }
            } else {
                if (this.inventoryTickSlotRequired) {
                    if (this.inventoryTickSlot == itemSlot) {
                        runFunction(worldIn, entityIn, this.inventoryTickFunction, entityIn.getPositionVec());
                    } else if (inventoryTickEquipmentSlot != null && entityIn instanceof LivingEntity
                            && ((LivingEntity) entityIn).getItemStackFromSlot(inventoryTickEquipmentSlot) == stack) {
                        runFunction(worldIn, entityIn, this.inventoryTickFunction, entityIn.getPositionVec());
                    }
                } else {
                    runFunction(worldIn, entityIn, this.inventoryTickFunction, entityIn.getPositionVec());
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
                runFunction(worldIn, entityLiving, this.onItemUseFinishFunction, entityLiving.getPositionVec());
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

    private static boolean evaluatePredicate(World worldIn, Entity entityIn, ResourceLocation predicate, Vector3d position) {
        try {
            return worldIn.getServer().func_229736_aP_().func_227517_a_(predicate).test(
                    new LootContext.Builder(worldIn.getServer().getWorld(entityIn.getEntityWorld().getDimensionKey()))
                            .withParameter(LootParameters.THIS_ENTITY, entityIn)
                            .withParameter(LootParameters.field_237457_g_, position)
                            .build(LootParameterSets.COMMAND));
        } catch (NullPointerException e) {
            return true;
        }
    }

    private static void runFunction(World worldIn, Entity source, ResourceLocation function, Vector3d position) {
        try {
            FunctionManager manager = worldIn.getServer().getFunctionManager();
            manager.execute(manager.get(function).get(), source.getCommandSource().withFeedbackDisabled().withPos(position));
        } catch (NoSuchElementException e) {
        }
    }

    private static EquipmentSlotType getEquipmentSlotType(int slotInt) {
        switch (slotInt) {
            case 100: {
                return EquipmentSlotType.FEET;
            }
            case 101: {
                return EquipmentSlotType.LEGS;
            }
            case 102: {
                return EquipmentSlotType.CHEST;
            }
            case 103: {
                return EquipmentSlotType.HEAD;
            }
            case -106: {
                return EquipmentSlotType.OFFHAND;
            }
            default: {
                return null;
            }
        }
    }
}
