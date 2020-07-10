package chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class ExhaustCommand {
    private static final SimpleCommandExceptionType EXHAUST_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.exhaust.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("exhaust").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", FloatArgumentType.floatArg(0)).executes((exhaust) -> {
            return exhaustPlayer(exhaust.getSource(), EntityArgument.getPlayers(exhaust, "targets"), FloatArgumentType.getFloat(exhaust, "amount"));
        }))));
    }

    private static int exhaustPlayer(CommandSource source, Collection<? extends PlayerEntity> targets, float amount) throws CommandSyntaxException {
        
        List<PlayerEntity> list = Lists.newArrayListWithCapacity(targets.size());

        for (PlayerEntity entity : targets) {
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity)entity).getFoodStats().addExhaustion(amount);
                list.add(entity);
            }
        }

        if (list.isEmpty()) {
            throw EXHAUST_FAILED_EXCEPTION.create();
         } else {
            if (list.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.exhaust.success.single", list.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.exhaust.success.multiple", list.size(), amount), true);
            }
   
            return list.size();
         }
    }
}