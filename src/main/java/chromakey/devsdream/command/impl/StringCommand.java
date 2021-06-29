package chromakey.devsdream.command.impl;

import java.util.Collection;
import java.util.Iterator;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.NBTPathArgument;
import net.minecraft.command.arguments.NBTPathArgument.NBTPath;
import net.minecraft.command.impl.data.EntityDataAccessor;
import net.minecraft.command.impl.data.IDataAccessor;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class StringCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("string").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.literal("get").then(Commands.literal("entity").then(Commands.argument("target", EntityArgument.entity()).then(Commands.argument("nbtPath", NBTPathArgument.nbtPath()).executes((context) -> {
            return getString(context, getTextComponent(new EntityDataAccessor(EntityArgument.getEntity(context, "target")), NBTPathArgument.getNBTPath(context, "nbtPath"), null, -1, true));
        }))))));
    }

    private static int getString(CommandContext<CommandSource> context, ITextComponent text) {
        context.getSource().sendFeedback(text, true);
        return text.getString().length();
    }

    private static ITextComponent getTextComponent(IDataAccessor accessor, NBTPath nbtPath, String jsonPath, int index, boolean interpret) throws CommandSyntaxException {
        INBT data = getData(nbtPath, accessor);
        if (data.getId() == 8) {
            if (interpret) {
                return ITextComponent.Serializer.getComponentFromJson(data.copy().getString());
            } else {
                return new StringTextComponent(data.copy().getString());
            }
        } else {
            throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.devsdream.string.get.not_a_string")).create();
        }
    }

    private static INBT getData(NBTPathArgument.NBTPath p_218928_0_, IDataAccessor p_218928_1_) throws CommandSyntaxException {
        Collection<INBT> collection = p_218928_0_.func_218071_a(p_218928_1_.getData());
        Iterator<INBT> iterator = collection.iterator();
        INBT inbt = iterator.next();
        if (iterator.hasNext()) {
           throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.data.get.multiple")).create();
        } else {
           return inbt;
        }
     }


}
