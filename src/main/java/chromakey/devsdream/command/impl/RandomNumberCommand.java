package chromakey.devsdream.command.impl;

import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RandomNumberCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> literalCommandNode = dispatcher.register(Commands.literal("random").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.argument("maximum", IntegerArgumentType.integer(0, 2147483647)).executes((integer) -> {
            Random rand = new Random();
            return rand.nextInt(IntegerArgumentType.getInteger(integer, "maximum"));
        })));
        
        dispatcher.register(Commands.literal("rng").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).redirect(literalCommandNode));
    }
}