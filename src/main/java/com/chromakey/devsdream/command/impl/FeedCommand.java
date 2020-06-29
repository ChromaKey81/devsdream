package com.chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import java.util.Collection;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class FeedCommand {
    private static final SimpleCommandExceptionType FEED_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.feed.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("feed").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("foodLevel", IntegerArgumentType.integer()).executes((feed) -> {
            return feedPlayer(feed.getSource(), EntityArgument.getPlayers(feed, "targets"), IntegerArgumentType.getInteger(feed, "foodLevel"), 0);
        }).then(Commands.argument("saturation", FloatArgumentType.floatArg()).executes((feedWithSaturation) -> {
            return feedPlayer(feedWithSaturation.getSource(), EntityArgument.getPlayers(feedWithSaturation, "targets"), IntegerArgumentType.getInteger(feedWithSaturation, "foodLevel"), FloatArgumentType.getFloat(feedWithSaturation, "saturation"));
        })))));
    }

    private static int feedPlayer(CommandSource source, Collection<? extends PlayerEntity> targets, int foodLevel, float saturation) throws CommandSyntaxException {
        
        int i = 0;

        for (PlayerEntity entity : targets) {
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity)entity).getFoodStats().addStats(foodLevel, saturation);
                i++;
            }
        }

        if (i == 0) {
            throw FEED_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.feed.success.single", targets.iterator().next().getDisplayName(), foodLevel, saturation), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.feed.success.multiple", targets.size(), foodLevel, saturation), true);
            }
   
            return i;
         }
    }
}