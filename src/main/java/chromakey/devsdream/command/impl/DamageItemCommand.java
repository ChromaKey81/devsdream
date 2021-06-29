package chromakey.devsdream.command.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class DamageItemCommand {
    private static final SimpleCommandExceptionType DAMAGEITEM_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.damageitem.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("damageitem").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.argument("targets", EntityArgument.players())
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .then(Commands.literal("mainhand").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgument.getPlayers(damage, "targets"),
                                    EquipmentSlotType.MAINHAND, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(Commands.literal("offhand").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgument.getPlayers(damage, "targets"),
                                    EquipmentSlotType.OFFHAND, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(Commands.literal("head").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgument.getPlayers(damage, "targets"),
                                    EquipmentSlotType.HEAD, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(Commands.literal("chest").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgument.getPlayers(damage, "targets"),
                                    EquipmentSlotType.CHEST, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(Commands.literal("legs").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgument.getPlayers(damage, "targets"),
                                    EquipmentSlotType.LEGS, IntegerArgumentType.getInteger(damage, "amount"));
                        })).then(Commands.literal("feet").executes((damage) -> {
                            return damageItem(damage.getSource(), EntityArgument.getPlayers(damage, "targets"),
                                    EquipmentSlotType.FEET, IntegerArgumentType.getInteger(damage, "amount"));
                        })))));
    }

    private static int damageItem(CommandSource source, Collection<? extends ServerPlayerEntity> targets,
            EquipmentSlotType slot, int amount) throws CommandSyntaxException {
        Map<ServerPlayerEntity, ItemStack> map = Maps.newHashMapWithExpectedSize(targets.size());

        for (ServerPlayerEntity player : targets) {
            ItemStack targetItem = player.getItemStackFromSlot(slot);
            targetItem.damageItem(amount, player, (p) -> {
                p.sendBreakAnimation(slot);
            });
            map.put(player, targetItem);
        }

        if (map.isEmpty()) {
            throw DAMAGEITEM_FAILED_EXCEPTION.create();
        } else {
            if (map.size() == 1) {
                map.forEach((player, itemStack) -> {
                    source.sendFeedback(new TranslationTextComponent("commands.devsdream.damageitem.success.single",
                            player.getDisplayName(), itemStack.getDisplayName(), amount), true);
                });
            } else {
                source.sendFeedback(new TranslationTextComponent("commands.devsdream.damageitem.success.multiple",
                        map.size(), amount), true);
            }

            return map.size();
        }
    }
}