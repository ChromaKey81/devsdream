package chromakey.devsdream.command.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.PotionArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TranslationTextComponent;

public class AdvancedEffectCommand {
   private static final SimpleCommandExceptionType GIVE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslationTextComponent("commands.effect.give.failed"));
   private static final SimpleCommandExceptionType CLEAR_EVERYTHING_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslationTextComponent("commands.effect.clear.everything.failed"));
   private static final SimpleCommandExceptionType CLEAR_SPECIFIC_FAILED_EXCEPTION = new SimpleCommandExceptionType(
         new TranslationTextComponent("commands.effect.clear.specific.failed"));

   public static void register(CommandDispatcher<CommandSource> dispatcher) {
      LiteralCommandNode<CommandSource> literalCommandNode = dispatcher
            .register(Commands.literal("advancedeffect").requires((user) -> {
               return user.hasPermissionLevel(2);
            }).then(Commands.literal("clear").executes((clearAllUser) -> {
               return clearAllEffects(clearAllUser.getSource(),
                     ImmutableList.of(clearAllUser.getSource().assertIsEntity()));
            }).then(Commands.argument("targets", EntityArgument.entities()).executes((clearAllTargets) -> {
               return clearAllEffects(clearAllTargets.getSource(),
                     EntityArgument.getEntities(clearAllTargets, "targets"));
            }).then(Commands.argument("effect", PotionArgument.mobEffect()).executes((clearEffect) -> {
               return clearEffect(clearEffect.getSource(), EntityArgument.getEntities(clearEffect, "targets"),
                     PotionArgument.getMobEffect(clearEffect, "effect"));
            })))).then(Commands.literal("give").then(Commands.argument("targets", EntityArgument.entities())
                  .then(Commands.argument("effect", PotionArgument.mobEffect()).executes((p_198357_0_) -> {
                     return addEffect(p_198357_0_.getSource(), EntityArgument.getEntities(p_198357_0_, "targets"),
                           PotionArgument.getMobEffect(p_198357_0_, "effect"), (Integer) null, 0, true, true, false);
                  }).then(Commands.argument("seconds", IntegerArgumentType.integer(1, 1000000))
                        .executes((p_198350_0_) -> {
                           return addEffect(p_198350_0_.getSource(), EntityArgument.getEntities(p_198350_0_, "targets"),
                                 PotionArgument.getMobEffect(p_198350_0_, "effect"),
                                 IntegerArgumentType.getInteger(p_198350_0_, "seconds"), 0, true, true, false);
                        }).then(Commands.argument("amplifier", IntegerArgumentType.integer(0, 255))
                              .executes((p_198358_0_) -> {
                                 return addEffect(p_198358_0_.getSource(),
                                       EntityArgument.getEntities(p_198358_0_, "targets"),
                                       PotionArgument.getMobEffect(p_198358_0_, "effect"),
                                       IntegerArgumentType.getInteger(p_198358_0_, "seconds"),
                                       IntegerArgumentType.getInteger(p_198358_0_, "amplifier"), true, true, false);
                              }).then(Commands.argument("hideParticles", BoolArgumentType.bool())
                                    .executes((p_229759_0_) -> {
                                       return addEffect(p_229759_0_.getSource(),
                                             EntityArgument.getEntities(p_229759_0_, "targets"),
                                             PotionArgument.getMobEffect(p_229759_0_, "effect"),
                                             IntegerArgumentType.getInteger(p_229759_0_, "seconds"),
                                             IntegerArgumentType.getInteger(p_229759_0_, "amplifier"),
                                             !BoolArgumentType.getBool(p_229759_0_, "hideParticles"), false, false);
                                    }).then(Commands.argument("hideIcon", BoolArgumentType.bool())
                                          .executes((p_229759_0_) -> {
                                             return addEffect(p_229759_0_.getSource(),
                                                   EntityArgument.getEntities(p_229759_0_, "targets"),
                                                   PotionArgument.getMobEffect(p_229759_0_, "effect"),
                                                   IntegerArgumentType.getInteger(p_229759_0_, "seconds"),
                                                   IntegerArgumentType.getInteger(p_229759_0_, "amplifier"),
                                                   !BoolArgumentType.getBool(p_229759_0_, "hideParticles"),
                                                   !BoolArgumentType.getBool(p_229759_0_, "hideIcon"), false);
                                          }).then(Commands.argument("isAmbient", BoolArgumentType.bool())
                                                .executes((p_229759_0_) -> {
                                                   return addEffect(p_229759_0_.getSource(),
                                                         EntityArgument.getEntities(p_229759_0_, "targets"),
                                                         PotionArgument.getMobEffect(p_229759_0_, "effect"),
                                                         IntegerArgumentType.getInteger(p_229759_0_, "seconds"),
                                                         IntegerArgumentType.getInteger(p_229759_0_, "amplifier"),
                                                         !BoolArgumentType.getBool(p_229759_0_, "hideParticles"),
                                                         !BoolArgumentType.getBool(p_229759_0_, "hideIcon"),
                                                         BoolArgumentType.getBool(p_229759_0_, "isAmbient"));
                                                }))))))))));
      dispatcher.register(Commands.literal("adveffect").requires((user) -> {
         return user.hasPermissionLevel(2);
      }).redirect(literalCommandNode));
   }

   private static int addEffect(CommandSource source, Collection<? extends Entity> targets, Effect effect,
         @Nullable Integer seconds, int amplifier, boolean showParticles, boolean showIcon, boolean ambient)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());
      int j;
      if (seconds != null) {
         if (effect.isInstant()) {
            j = seconds;
         } else {
            j = seconds * 20;
         }
      } else if (effect.isInstant()) {
         j = 1;
      } else {
         j = 600;
      }

      for (Entity entity : targets) {
         if (entity instanceof LivingEntity) {
            EffectInstance effectinstance = new EffectInstance(effect, j, amplifier, ambient, showParticles, showIcon);
            if (((LivingEntity) entity).addPotionEffect(effectinstance)) {
               list.add(entity);
            }
         }
      }

      if (list.isEmpty()) {
         throw GIVE_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslationTextComponent("commands.effect.give.success.single",
                  effect.getDisplayName(), targets.iterator().next().getDisplayName(), j / 20), true);
         } else {
            source.sendFeedback(new TranslationTextComponent("commands.effect.give.success.multiple",
                  effect.getDisplayName(), targets.size(), j / 20), true);
         }

         return list.size();
      }
   }

   private static int clearAllEffects(CommandSource source, Collection<? extends Entity> targets)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         if (entity instanceof LivingEntity && ((LivingEntity) entity).clearActivePotions()) {
            list.add(entity);
         }
      }

      if (list.isEmpty()) {
         throw CLEAR_EVERYTHING_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslationTextComponent("commands.effect.clear.everything.success.single",
                  targets.iterator().next().getDisplayName()), true);
         } else {
            source.sendFeedback(
                  new TranslationTextComponent("commands.effect.clear.everything.success.multiple", targets.size()),
                  true);
         }

         return list.size();
      }
   }

   private static int clearEffect(CommandSource source, Collection<? extends Entity> targets, Effect effect)
         throws CommandSyntaxException {
      List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

      for (Entity entity : targets) {
         if (entity instanceof LivingEntity && ((LivingEntity) entity).removePotionEffect(effect)) {
            list.add(entity);
         }
      }

      if (list.isEmpty()) {
         throw CLEAR_SPECIFIC_FAILED_EXCEPTION.create();
      } else {
         if (list.size() == 1) {
            source.sendFeedback(new TranslationTextComponent("commands.effect.clear.specific.success.single",
                  effect.getDisplayName(), list.iterator().next().getDisplayName()), true);
         } else {
            source.sendFeedback(new TranslationTextComponent("commands.effect.clear.specific.success.multiple",
                  effect.getDisplayName(), list.size()), true);
         }

         return list.size();
      }
   }
}