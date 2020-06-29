package com.chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import java.util.Collection;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class HealthCommand {
    private static final SimpleCommandExceptionType HEALTH_ADJUST_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.health.add.failed"));
    private static final SimpleCommandExceptionType HEALTH_SET_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.health.set.failed"));
    

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("health").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.literal("add").then(Commands.argument("targets", EntityArgument.entities()).then(Commands.argument("amount", FloatArgumentType.floatArg(0)).executes((heal) -> {
            return healEntity(heal.getSource(), EntityArgument.getEntities(heal, "targets"), FloatArgumentType.getFloat(heal, "amount"));
        })))).then(Commands.literal("set").then(Commands.argument("amount", FloatArgumentType.floatArg(0)).executes((set) -> {
            return setEntityHealth(set.getSource(), EntityArgument.getEntities(set, "targets"), FloatArgumentType.getFloat(set, "amount"));
        }))));
    }

    private static int healEntity(CommandSource source, Collection<? extends Entity> targets, float amount) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).heal(amount);
                i++;
            }
        }

        if (i == 0) {
            throw HEALTH_ADJUST_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.health.add.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.health.add.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
     }

     private static int setEntityHealth(CommandSource source, Collection<? extends Entity> targets, float amount) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setHealth(amount);
                i++;
            }
        }

        if (i == 0) {
            throw HEALTH_SET_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.health.set.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.health.set.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
     }
}