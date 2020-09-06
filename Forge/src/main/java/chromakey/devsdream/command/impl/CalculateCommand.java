package chromakey.devsdream.command.impl;

import java.util.Collection;
import java.util.Iterator;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.NBTPathArgument;
import net.minecraft.command.arguments.ObjectiveArgument;
import net.minecraft.command.arguments.ScoreHolderArgument;
import net.minecraft.command.impl.data.BlockDataAccessor;
import net.minecraft.command.impl.data.EntityDataAccessor;
import net.minecraft.command.impl.data.IDataAccessor;
import net.minecraft.nbt.CollectionNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.scoreboard.Score;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

public class CalculateCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("calculate").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.literal("data")
                .then(Commands.literal("entity")
                        .then(Commands.argument("source", EntityArgument.entity())
                                .then(Commands.argument("path", NBTPathArgument.nbtPath())
                                        .then(Commands.argument("scale", DoubleArgumentType.doubleArg())
                                                .then(calculation("entityData"))))))
                .then(Commands.literal("block")
                        .then(Commands.argument("sourcePos", BlockPosArgument.blockPos())
                                .then(Commands.argument("path", NBTPathArgument.nbtPath())
                                        .then(Commands.argument("scale", DoubleArgumentType.doubleArg())
                                                .then(calculation("blockData")))))))
                .then(Commands.literal("score")
                        .then(Commands.argument("name", ScoreHolderArgument.scoreHolder())
                                .then(Commands.argument("objective", ObjectiveArgument.objective())
                                        .then(calculation("score")))))
                .then(Commands.literal("value")
                        .then(Commands.argument("value", IntegerArgumentType.integer()).then(calculation("value")))));
    }

    private static INBT getINBT(NBTPathArgument.NBTPath p_218928_0_, IDataAccessor p_218928_1_)
            throws CommandSyntaxException {
        Collection<INBT> collection = p_218928_0_.func_218071_a(p_218928_1_.getData());
        Iterator<INBT> iterator = collection.iterator();
        INBT inbt = iterator.next();
        if (iterator.hasNext()) {
            throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.data.get.multiple")).create();
        } else {
            return inbt;
        }
    }

    private static int getData(CommandSource source, IDataAccessor accessor, NBTPathArgument.NBTPath pathIn)
            throws CommandSyntaxException {
        INBT inbt = getINBT(pathIn, accessor);
        int i;
        if (inbt instanceof NumberNBT) {
            i = MathHelper.floor(((NumberNBT) inbt).getDouble());
        } else if (inbt instanceof CollectionNBT) {
            i = ((CollectionNBT) inbt).size();
        } else if (inbt instanceof CompoundNBT) {
            i = ((CompoundNBT) inbt).size();
        } else {
            if (!(inbt instanceof StringNBT)) {
                throw new DynamicCommandExceptionType((p_208919_0_) -> {
                    return new TranslationTextComponent("commands.data.get.unknown", p_208919_0_);
                }).create(pathIn.toString());
            }

            i = inbt.getString().length();
        }

        source.sendFeedback(accessor.getQueryMessage(inbt), false);
        return i;
    }

    private static LiteralArgumentBuilder<CommandSource> calculation(String firstArgumentType) {
        return (Commands.literal("+").then(secondArgument(firstArgumentType, "add")))
                .then(Commands.literal("-").then(secondArgument(firstArgumentType, "subtract")))
                .then(Commands.literal("*").then(secondArgument(firstArgumentType, "multiply")))
                .then(Commands.literal("/").then(secondArgument(firstArgumentType, "divide")))
                .then(Commands.literal("%").then(secondArgument(firstArgumentType, "modulo")));
    }

    private static LiteralArgumentBuilder<CommandSource> secondArgument(String firstArgumentType, String operation) {
        return (Commands.literal("data")
                .then(Commands.literal("entity").then(Commands.argument("source2", EntityArgument.entity())
                        .then(Commands.argument("path2", NBTPathArgument.nbtPath()).then(
                                Commands.argument("scale2", DoubleArgumentType.doubleArg()).executes((context) -> {
                                    return performOperation(context.getSource(),
                                            findFirstArgument(context, firstArgumentType),
                                            getValueFromEntityData(context, "source2", "path2", "scale2"), operation);
                                })))))
                .then(Commands.literal("block").then(Commands.argument("sourcePos2", BlockPosArgument.blockPos())
                        .then(Commands.argument("path2", NBTPathArgument.nbtPath()).then(
                                Commands.argument("scale2", DoubleArgumentType.doubleArg()).executes((context) -> {
                                    return performOperation(context.getSource(),
                                            findFirstArgument(context, firstArgumentType),
                                            getValueFromBlockData(context, "sourcePos2", "path2", "scale2"), operation);
                                })))))).then(Commands.literal("score")
                                        .then(Commands.argument("name2", ScoreHolderArgument.scoreHolder())
                                                .then(Commands.argument("objective2", ObjectiveArgument.objective())
                                                        .executes((context) -> {
                                                            return performOperation(context.getSource(),
                                                                    findFirstArgument(context, firstArgumentType),
                                                                    getValueFromScore(context, "objective2", "name2"),
                                                                    operation);
                                                        }))))
                                        .then(Commands.literal("value")
                                                .then(Commands.argument("value2", IntegerArgumentType.integer())
                                                        .executes((context) -> {
                                                            return performOperation(context.getSource(),
                                                                    findFirstArgument(context, firstArgumentType),
                                                                    IntegerArgumentType.getInteger(context, "value2"),
                                                                    operation);
                                                        })));
    }

    private static int findFirstArgument(CommandContext<CommandSource> context, String firstArgumentType)
            throws CommandSyntaxException {
        switch (firstArgumentType) {
            case "entityData": {
                return getValueFromEntityData(context, "source", "path", "scale");
            }
            case "blockData": {
                return getValueFromBlockData(context, "sourcePos", "path", "scale");
            }
            case "score": {
                return getValueFromScore(context, "objective", "name");
            }
            case "value": {
                return IntegerArgumentType.getInteger(context, "value");
            }
            default: {
                return 69;
            }
        }
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
        }
        source.sendFeedback(new TranslationTextComponent("commands.devsdream.calculate.success", newVal), true);
        return newVal;
    }

    private static int getValueFromEntityData(CommandContext<CommandSource> context, String argument,
            String pathArgument, String scaleArgument) throws CommandSyntaxException {
        return (int) (getData(context.getSource(), new EntityDataAccessor(EntityArgument.getEntity(context, argument)),
                NBTPathArgument.getNBTPath(context, pathArgument))
                * DoubleArgumentType.getDouble(context, scaleArgument));
    }

    private static int getValueFromBlockData(CommandContext<CommandSource> context, String argument,
            String pathArgument, String scaleArgument) throws CommandSyntaxException {
        BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(context, argument);
        TileEntity tileEntity = (context.getSource()).getWorld().getTileEntity(blockPos);
        if (tileEntity == null) {
            throw new SimpleCommandExceptionType(new TranslationTextComponent("commands.data.block.invalid")).create();
        } else {
            return (int) (getData(context.getSource(), new BlockDataAccessor(tileEntity, blockPos),
                    NBTPathArgument.getNBTPath(context, pathArgument))
                    * DoubleArgumentType.getDouble(context, scaleArgument));
        }
    }

    private static int getValueFromScore(CommandContext<CommandSource> context, String objectiveArgument, String player)
            throws CommandSyntaxException {
        return new Score(context.getSource().getServer().getScoreboard(),
                ObjectiveArgument.getObjective(context, objectiveArgument),
                ScoreHolderArgument.getSingleScoreHolderNoObjectives(context, player)).getScorePoints();
    }
}