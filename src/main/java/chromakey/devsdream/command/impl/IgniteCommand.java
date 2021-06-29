package chromakey.devsdream.command.impl;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;

public class IgniteCommand {
   private static SimpleCommandExceptionType IGNITE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslationTextComponent("commands.devsdream.ignite.failed"));

   public static void register(CommandDispatcher<CommandSource> dispatcher) {
      dispatcher.register(Commands.literal("ignite").requires((user) -> {
         return user.hasPermissionLevel(2);
      }).then(Commands.argument("targets", EntityArgument.entities())
            .then(Commands.argument("seconds", IntegerArgumentType.integer(0)).executes((ignite) -> {
               return burnEntity(ignite.getSource(), EntityArgument.getEntities(ignite, "targets"),
                     IntegerArgumentType.getInteger(ignite, "seconds"));
            }))));
   }

   private static int burnEntity(CommandSource source, Collection<? extends Entity> targets, int burnTime)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         ((Entity) entity).setFire(burnTime);
         list.add(entity);
      }

      if (list.isEmpty()) {
         throw IGNITE_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslationTextComponent("commands.devsdream.ignite.success.single",
                  list.iterator().next().getDisplayName(), burnTime), true);
         } else {
            source.sendFeedback(
                  new TranslationTextComponent("commands.devsdream.ignite.success.multiple", list.size(), burnTime),
                  true);
         }

         return (int) burnTime;
      }
   }
}