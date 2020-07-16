package chromakey.devsdream.command.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class ItemCommand {
    private static final SimpleCommandExceptionType ITEMCOMMAND_FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.devsdream.item.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("item"));
    }
}