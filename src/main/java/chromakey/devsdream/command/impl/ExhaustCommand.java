package chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import java.util.Collection;

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
        
        int i = 0;

        for (PlayerEntity entity : targets) {
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity)entity).getFoodStats().addExhaustion(amount);
                i++;
            }
        }

        if (i == 0) {
            throw EXHAUST_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.exhaust.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.exhaust.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
    }
}