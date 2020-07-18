package chromakey.devsdream.command.impl;

import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class RandomNumberCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> literalCommandNode = dispatcher.register(Commands.literal("randomnumber").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.argument("maximum", IntegerArgumentType.integer(0, 2147483647)).executes((rng) -> {
            return rng(rng.getSource(), IntegerArgumentType.getInteger(rng, "maximum"));
        })));
        
        dispatcher.register(Commands.literal("rng").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).redirect(literalCommandNode));
    }

    private static int rng(CommandSource source, int maximum) {
        Random rand = new Random();
        int result = rand.nextInt(maximum);
        source.sendFeedback(new TranslationTextComponent("commands.devsdream.rng.result", result), true);
        return result;
    }
}