package chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import java.util.Collection;
import java.util.List;

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
        }).then(Commands.argument("targets", EntityArgument.players())
                .then(Commands.argument("foodLevel", IntegerArgumentType.integer()).executes((feed) -> {
                    return feedPlayer(feed.getSource(), EntityArgument.getPlayers(feed, "targets"),
                            IntegerArgumentType.getInteger(feed, "foodLevel"), 0);
                }).then(Commands.argument("saturation", FloatArgumentType.floatArg()).executes((feedWithSaturation) -> {
                    return feedPlayer(feedWithSaturation.getSource(),
                            EntityArgument.getPlayers(feedWithSaturation, "targets"),
                            IntegerArgumentType.getInteger(feedWithSaturation, "foodLevel"),
                            FloatArgumentType.getFloat(feedWithSaturation, "saturation"));
                })))));
    }

    private static int feedPlayer(CommandSource source, Collection<? extends PlayerEntity> targets, int foodLevel,
            float saturation) throws CommandSyntaxException {
        List<PlayerEntity> list = Lists.newArrayListWithCapacity(targets.size());

        for (PlayerEntity player : targets) {
            if (player instanceof PlayerEntity) {
                ((PlayerEntity) player).getFoodStats().addStats(foodLevel, saturation);
                list.add(player);
            }
        }

        if (list.isEmpty()) {
            throw FEED_FAILED_EXCEPTION.create();
        } else {
            if (list.size() == 1) {
                source.sendFeedback(new TranslationTextComponent("commands.devsdream.feed.success.single",
                        list.iterator().next().getDisplayName(), foodLevel, saturation), true);
            } else {
                source.sendFeedback(new TranslationTextComponent("commands.devsdream.feed.success.multiple",
                        list.size(), foodLevel, saturation), true);
            }

            return list.size();
        }
    }
}