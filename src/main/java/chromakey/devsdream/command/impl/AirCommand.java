package chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class AirCommand {
    private static final SimpleCommandExceptionType AIR_SET_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.air.set.failed"));
    private static final SimpleCommandExceptionType AIR_ADD_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.air.add.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("air").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((run) -> {
            return setAir(run.getSource(), EntityArgument.getEntities(run, "targets"), IntegerArgumentType.getInteger(run, "amount"));
        }))).then(Commands.literal("add").then(Commands.argument("amount", IntegerArgumentType.integer()).executes((run) -> {
            return increaseAir(run.getSource(), EntityArgument.getEntities(run, "targets"), IntegerArgumentType.getInteger(run, "amount"));
        })))));
    }

    private static int setAir(CommandSource source, Collection<? extends Entity> targets, int amount) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (amount <= ((LivingEntity)entity).getMaxAir()) {
                    ((LivingEntity)entity).setAir(amount);
                    i++;
                } else {
                    ((LivingEntity)entity).setAir(((LivingEntity)entity).getMaxAir());
                }
            }
        }

        if (i == 0) {
            throw AIR_SET_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.air.set.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.air.set.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
    }

    private static int increaseAir(CommandSource source, Collection<? extends Entity> targets, int amount) throws CommandSyntaxException {
        List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity)entity).getAir() + amount <= ((LivingEntity)entity).getMaxAir()) {
                    ((LivingEntity)entity).setAir(((LivingEntity)entity).getAir() + amount);
                    list.add(entity);
                } else {
                    ((LivingEntity)entity).setAir(((LivingEntity)entity).getMaxAir());
                    list.add(entity);
                }
            }
        }

        if (list.isEmpty()) {
            throw AIR_ADD_FAILED_EXCEPTION.create();
         } else {
            if (list.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.air.add.success.single", list.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.air.add.success.multiple", list.size(), amount), true);
            }
   
            return amount;
         }
    }
}