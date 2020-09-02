package chromakey.devsdream.complex;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ComplexItem extends Item {

    private static final Logger logger = LogManager.getLogger("devsdream");

    private final List<ITextComponent> tooltip;
    private final boolean hasEffect;
    private final int enchantability;
    private final boolean canBreakBlocks;
    private final ResourceLocation onUseFunction;
    private final ResourceLocation rightClickFunctionMainhand;
    private final ResourceLocation rightClickFunctionOffhand;
    private final ResourceLocation rightClickPredicate;

    public ComplexItem(Properties properties, List<ITextComponent> tooltip, boolean hasEffect, int enchantability, boolean canBreakBlocks, @Nullable ResourceLocation onUseFunction, @Nullable ResourceLocation rightClickFunctionMainhand, @Nullable ResourceLocation rightClickFunctionOffhand, @Nullable ResourceLocation rightClickPredicate) {
        super(properties);
        this.tooltip = tooltip;
        this.hasEffect = hasEffect;
        this.enchantability = enchantability;
        this.canBreakBlocks = canBreakBlocks;
        this.onUseFunction = onUseFunction;
        this.rightClickFunctionMainhand = rightClickFunctionMainhand;
        this.rightClickFunctionOffhand = rightClickFunctionOffhand;
        this.rightClickPredicate = rightClickPredicate;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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

    public int getEnchantability() {
        return this.enchantability;
    }

    public boolean canPlayerBreakBlockWhileHolding() {
        return this.canBreakBlocks;
    }

    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (this.onUseFunction != null && worldIn.isRemote()) {
            FunctionManager manager = worldIn.getServer().getFunctionManager();
            try {
                manager.execute(manager.get(this.onUseFunction).orElseThrow(), livingEntityIn.getCommandSource());
            } catch (NoSuchElementException e) {
                logger.error("Unknown function '" + this.onUseFunction.getPath() + "'");
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if ((this.isFood() && playerIn.canEat(this.getFood().canEatWhenFull())) || (this.rightClickFunctionMainhand == null && handIn == Hand.MAIN_HAND) || (this.rightClickFunctionOffhand == null && handIn == Hand.OFF_HAND)) {
            logger.debug("Food");
            return super.onItemRightClick(worldIn, playerIn, handIn);
         } else {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            boolean flag;
            if (!worldIn.isRemote) {
                if (this.rightClickPredicate == null) {
                    flag = true;
                    logger.debug("Predicate is null, setting to true");
                } else {
                    flag = worldIn.getServer().func_229736_aP_().func_227517_a_(this.rightClickPredicate).test(new LootContext.Builder(worldIn.getServer().getWorld(playerIn.getEntityWorld().getDimensionKey())).withParameter(LootParameters.THIS_ENTITY, playerIn).withParameter(LootParameters.field_237457_g_, playerIn.getPositionVec()).build(LootParameterSets.COMMAND));
                    logger.debug("Predicate tested, setting to " + flag);
                }
                if (flag == true) {
                    playerIn.addStat(Stats.ITEM_USED.get(this));
                    logger.debug("Player stat added");
                    FunctionManager manager = worldIn.getServer().getFunctionManager();
                    logger.debug("Got function manager");
                    if (handIn == Hand.MAIN_HAND) {
                        try {
                            worldIn.getServer().getFunctionManager().execute(manager.get(this.rightClickFunctionMainhand).orElseThrow(), playerIn.getCommandSource());
                            logger.debug("Executing mainhand function");
                        } catch (NoSuchElementException e) {
                            logger.error("Unknown function '" + this.onUseFunction.getPath() + "'");
                        }
                    }
                    if (handIn == Hand.OFF_HAND) {
                        try {
                            worldIn.getServer().getFunctionManager().execute(manager.get(this.rightClickFunctionOffhand).orElseThrow(), playerIn.getCommandSource());
                            logger.debug("Executing offhand function");
                        } catch (NoSuchElementException e) {
                            logger.error("Unknown function '" + this.onUseFunction.getPath() + "'");
                        }
                    }
                    logger.debug("Success");
                    return ActionResult.resultSuccess(itemstack);
                } else {
                    logger.debug("Pass");
                    return ActionResult.resultPass(itemstack);
                }
            } else {
                return ActionResult.resultPass(itemstack);
            }
         }
    }
}
