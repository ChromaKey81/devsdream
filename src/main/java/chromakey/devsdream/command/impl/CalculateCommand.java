package chromakey.devsdream.command.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class CalculateCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("calculate").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.literal("value")
                        .then(Commands.argument("value", IntegerArgumentType.integer()).then(add("value"))
                                .then(subtract("value")).then(multiply("value")).then(divide("value"))
                                .then(modulo("value")).then(equals("value")))).then(Commands.literal("command").then(Commands.argument("command", StringArgumentType.string()).then(add("command")).then(subtract("command")).then(multiply("command")).then(divide("command")).then(modulo("command")))));
    }

    private static LiteralArgumentBuilder<CommandSource> add(String firstArgumentType) {
        return Commands
                .literal(
                        "+")
                .then(Commands.literal("value")
                        .then(Commands.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "add");
                        }))).then(Commands.literal("command").then(Commands.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "add");
                        })));
    }

    private static LiteralArgumentBuilder<CommandSource> subtract(String firstArgumentType) {
        return Commands
                .literal(
                        "-")
                .then(Commands.literal("value")
                        .then(Commands.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "subtract");
                        }))).then(Commands.literal("command").then(Commands.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "subtract");
                        })));
    }

    private static LiteralArgumentBuilder<CommandSource> multiply(String firstArgumentType) {
        return Commands
                .literal(
                        "*")
                .then(Commands.literal("value")
                        .then(Commands.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "multiply");
                        }))).then(Commands.literal("command").then(Commands.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "multiply");
                        })));
    }

    private static LiteralArgumentBuilder<CommandSource> divide(String firstArgumentType) {
        return Commands
                .literal(
                        "/")
                .then(Commands.literal("value")
                        .then(Commands.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "divide");
                        }))).then(Commands.literal("command").then(Commands.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "divide");
                        })));
    }

    private static LiteralArgumentBuilder<CommandSource> modulo(String firstArgumentType) {
        return Commands
                .literal(
                        "%")
                .then(Commands.literal("value")
                        .then(Commands.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "modulo");
                        }))).then(Commands.literal("command").then(Commands.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "modulo");
                        })));
    }
    private static LiteralArgumentBuilder<CommandSource> equals(String firstArgumentType) {
        return Commands
                .literal(
                        "=")
                .then(Commands.literal("value")
                        .then(Commands.argument("value2", IntegerArgumentType.integer()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType),
                                    IntegerArgumentType.getInteger(context, "value2"), "equals");
                        }))).then(Commands.literal("command").then(Commands.argument("command2", StringArgumentType.string()).executes((context) -> {
                            return performOperation(context.getSource(), findFirstArgument(context, firstArgumentType), getValueFromCommand(context, "command2"), "equals");
                        })));
    }

    private static int findFirstArgument(CommandContext<CommandSource> context, String firstArgumentType)
            throws CommandSyntaxException {
        switch (firstArgumentType) {
            case "value": {
                return IntegerArgumentType.getInteger(context, "value");
            }
            case "command": {
                return getValueFromCommand(context, "command");
            }
            default: {
                return 69;
            }
        }
    }

    private static int getValueFromCommand(CommandContext<CommandSource> context, String argument) throws CommandSyntaxException {
        return context.getSource().getServer().getCommandManager().getDispatcher().execute(StringArgumentType.getString(context, argument), context.getSource());
    }

    private static int performOperation(CommandSource source, int valueSoFar, int input, String type) {
        int newVal = 0;
        switch (type) {
            case "add": {
                newVal = valueSoFar + input;
                break;
            }
            case "subtract": {
                newVal = valueSoFar - input;
                break;
            }
            case "multiply": {
                newVal = valueSoFar * input;
                break;
            }
            case "divide": {
                newVal = valueSoFar / input;
                break;
            }
            case "modulo": {
                newVal = valueSoFar % input;
                break;
            }
            case "equals": {
                newVal = valueSoFar == input ? 1 : 0;
                break;
            }
        }
        source.sendFeedback(new TranslationTextComponent("commands.devsdream.calculate.success", newVal), true);
        return newVal;
    }
}