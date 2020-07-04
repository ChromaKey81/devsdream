package com.chromakey.devsdream.command.impl;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.SlotArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.text.TranslationTextComponent;

public class DamageItemCommand {
    private static final SimpleCommandExceptionType DAMAGEITEM_FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.devsdream.exhaust.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("damageitem").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("slot", SlotArgument.slot()).executes((damage) -> {
            return damageItem(damage.getSource(), EntityArgument.getPlayers(damage, "targets"), EquipmentSlotType.MAINHAND, 1);
        }))));
    }

    private static int damageItem(CommandSource source, Collection<? extends ServerPlayerEntity> targets, EquipmentSlotType slot, int amount) throws CommandSyntaxException {
        List<ServerPlayerEntity> list = Lists.newArrayListWithCapacity(targets.size());
  
        for(ServerPlayerEntity entity : targets) {
           entity.getItemStackFromSlot(slot).setDamage(amount);
           list.add(entity);
        }
  
        if (list.isEmpty()) {
           throw DAMAGEITEM_FAILED_EXCEPTION.create();
        } else {
           if (list.size() == 1) {
              source.sendFeedback(new TranslationTextComponent("commands.devsdream.damageitem.success.single", list.iterator().next().getDisplayName(), amount), true);
           } else {
              source.sendFeedback(new TranslationTextComponent("commands.devsdream.damageitem.success.multiple", list.size(), amount), true);
           }
  
           return list.size();
        }
    }
}