package com.chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Collection;

import javax.annotation.Nullable;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.command.Commands;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.TranslationTextComponent;

public class StatCommand {
    private static final SimpleCommandExceptionType HEAL_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.stat.heal.failed"));
    private static final SimpleCommandExceptionType DAMAGE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.stat.damage.failed"));
    private static final SimpleCommandExceptionType FEED_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.stat.feed.failed"));
    private static final SimpleCommandExceptionType EXHAUST_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.stat.exhaust.failed"));
    private static final SimpleCommandExceptionType AIR_SET_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.stat.air.set.failed"));
    private static final SimpleCommandExceptionType AIR_ADD_FAILED_EXCEPTION = new SimpleCommandExceptionType(
            new TranslationTextComponent("commands.devsdream.stat.air.add.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("stat").requires((user) -> {
            return user.hasPermissionLevel(2);
        }).then(Commands.literal("heal").then(Commands.argument("targets", EntityArgument.entities()).then(Commands.argument("amount", FloatArgumentType.floatArg(0)).executes((heal) -> {
            return healEntity(heal.getSource(), EntityArgument.getEntities(heal, "targets"), FloatArgumentType.getFloat(heal, "amount"));
        })))).then(Commands.literal("damage").then(Commands.argument("targets", EntityArgument.entities()).then(Commands.argument("amount", FloatArgumentType.floatArg(0)).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties("generic", false, false, false, false, false, false, false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("sourceString", StringArgumentType.string()).executes((damage) -> {
			return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), false, false, false, false, false, false, false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("isFire", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), false, false, false, false, false, false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("pierceArmor", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), false, false, false, false, false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("difficultyScaled", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "difficultyScaled"), false, false, false, false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("isMagic", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "difficultyScaled"), BoolArgumentType.getBool(damage, "isMagic"), false, false, false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("damageCreative", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "difficultyScaled"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "damageCreative"), false, false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("isExplosion", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "difficultyScaled"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "damageCreative"), BoolArgumentType.getBool(damage, "isExplosion"), false, null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("isProjectile", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "difficultyScaled"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "damageCreative"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), null, false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("sourceEntity", EntityArgument.entity()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "difficultyScaled"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "damageCreative"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), EntityArgument.getEntity(damage, "sourceEntity"), false), FloatArgumentType.getFloat(damage, "amount"));
        }).then(Commands.argument("thorns", BoolArgumentType.bool()).executes((damage) -> {
            return damageEntity(damage.getSource(), EntityArgument.getEntities(damage, "targets"), setDamageProperties(StringArgumentType.getString(damage, "sourceString"), BoolArgumentType.getBool(damage, "isFire"), BoolArgumentType.getBool(damage, "pierceArmor"), BoolArgumentType.getBool(damage, "difficultyScaled"), BoolArgumentType.getBool(damage, "isMagic"), BoolArgumentType.getBool(damage, "damageCreative"), BoolArgumentType.getBool(damage, "isExplosion"), BoolArgumentType.getBool(damage, "isProjectile"), EntityArgument.getEntity(damage, "sourceEntity"), BoolArgumentType.getBool(damage, "thorns")), FloatArgumentType.getFloat(damage, "amount"));
        })))))))))))))).then(Commands.literal("feed").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("foodLevel", IntegerArgumentType.integer()).executes((feed) -> {
            return feedPlayer(feed.getSource(), EntityArgument.getPlayers(feed, "targets"), IntegerArgumentType.getInteger(feed, "foodLevel"), 0);
        }).then(Commands.argument("saturation", FloatArgumentType.floatArg()).executes((feedWithSaturation) -> {
            return feedPlayer(feedWithSaturation.getSource(), EntityArgument.getPlayers(feedWithSaturation, "targets"), IntegerArgumentType.getInteger(feedWithSaturation, "foodLevel"), FloatArgumentType.getFloat(feedWithSaturation, "saturation"));
        }))))).then(Commands.literal("exhaust").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", FloatArgumentType.floatArg(0)).executes((exhaust) -> {
            return exhaustPlayer(exhaust.getSource(), EntityArgument.getPlayers(exhaust, "targets"), FloatArgumentType.getFloat(exhaust, "amount"));
        })))).then(Commands.literal("air").then(Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("set").then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((run) -> {
            return setAir(run.getSource(), EntityArgument.getEntities(run, "targets"), IntegerArgumentType.getInteger(run, "amount"));
        }))).then(Commands.literal("add").then(Commands.argument("amount", IntegerArgumentType.integer()).executes((run) -> {
            return increaseAir(run.getSource(), EntityArgument.getEntities(run, "targets"), IntegerArgumentType.getInteger(run, "amount"));
        }))))));
    }

    private static int healEntity(CommandSource source, Collection<? extends Entity> targets, float amount) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).heal(amount);
                i++;
            }
        }

        if (i == 0) {
            throw HEAL_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.heal.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.heal.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
    }


    private static EntityDamageSource setDamageProperties(String sourceString, boolean isFire, boolean pierceArmor, boolean difficultyScaled, boolean isMagic, boolean damageCreative, boolean isExplosion, boolean isProjectile, @Nullable Entity sourceEntity, boolean thorns) {
        
        EntityDamageSource damage = new EntityDamageSource(sourceString, sourceEntity);

        if (isFire == true) {
            damage.setFireDamage();
        }

        if (pierceArmor == true) {
            damage.setDamageBypassesArmor();
        }

        if (difficultyScaled == true) {
            damage.setDifficultyScaled();
        }

        if (isMagic == true) {
            damage.setMagicDamage();
        }

        if (damageCreative == true) {
            damage.setDamageAllowedInCreativeMode();
        }

        if (isExplosion == true) {
            damage.setExplosion();
        }

        if (isProjectile == true) {
            damage.setProjectile();
        }

        if (thorns == true) {
            damage.setIsThornsDamage();
        }

        return damage;
    }

    private static int damageEntity(CommandSource source, Collection<? extends Entity> targets, EntityDamageSource damageSource, float amount) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).attackEntityFrom(damageSource, amount);
                i++;
            }
        }

        if (i == 0) {
            throw DAMAGE_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.damage.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.damage.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
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
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.feed.success.single", targets.iterator().next().getDisplayName(), foodLevel, saturation), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.feed.success.multiple", targets.size(), foodLevel, saturation), true);
            }
   
            return i;
         }
    }

    private static int exhaustPlayer(CommandSource source, Collection<? extends PlayerEntity> targets, float amount) throws CommandSyntaxException {
        
        int i = 0;

        for (PlayerEntity entity : targets) {
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity)entity).getFoodStats().addExhaustion(amount);
                i++;
            }
        }

        if (i == 0) {
            throw EXHAUST_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.exhaust.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.exhaust.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
    }

    private static int setAir(CommandSource source, Collection<? extends Entity> targets, int amount) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setAir(amount);
                i++;
            }
        }

        if (i == 0) {
            throw AIR_SET_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.air.set.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.air.set.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
    }

    private static int increaseAir(CommandSource source, Collection<? extends Entity> targets, int amount) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity)entity).setAir(((LivingEntity)entity).getAir() + amount);
                i++;
            }
        }

        if (i == 0) {
            throw AIR_ADD_FAILED_EXCEPTION.create();
         } else {
            if (targets.size() == 1) {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.air.increase.success.single", targets.iterator().next().getDisplayName(), amount), true);
            } else {
               source.sendFeedback(new TranslationTextComponent("commands.devsdream.stat.air.increase.success.multiple", targets.size(), amount), true);
            }
   
            return i;
         }
    }
}