package chromakey.devsdream.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.command.Commands;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.TranslationTextComponent;

public class DamageCommand {
        private static final SimpleCommandExceptionType DAMAGE_FAILED_EXCEPTION = new SimpleCommandExceptionType(
                        new TranslationTextComponent("commands.devsdream.damage.failed"));

        public static void register(CommandDispatcher<CommandSource> dispatcher) {
                dispatcher.register(Commands.literal("damage").requires((user) -> {
                        return user.hasPermissionLevel(2);
                }).then(Commands.argument("targets", EntityArgument.entities()).then(Commands
                                .argument("amount", FloatArgumentType.floatArg(0))
                                .then(Commands.argument("sourceString", StringArgumentType.string())
                                                .then(Commands.argument("sourceEntity", EntityArgument.entity())
                                                                .executes((damage) -> {
                                                                        return damageEntity(damage.getSource(),
                                                                                        EntityArgument.getEntities(
                                                                                                        damage,
                                                                                                        "targets"),
                                                                                        setDamageProperties(
                                                                                                        StringArgumentType
                                                                                                                        .getString(damage,
                                                                                                                                        "sourceString"),
                                                                                                        false, false,
                                                                                                        false, false,
                                                                                                        false, false,
                                                                                                        false,
                                                                                                        EntityArgument.getEntity(
                                                                                                                        damage,
                                                                                                                        "sourceEntity"),
                                                                                                        false, false),
                                                                                        FloatArgumentType.getFloat(
                                                                                                        damage,
                                                                                                        "amount"));
                                                                })
                                                                .then(Commands.argument("isFire",
                                                                                BoolArgumentType.bool())
                                                                                .executes((damage) -> {
                                                                                        return damageEntity(damage
                                                                                                        .getSource(),
                                                                                                        EntityArgument.getEntities(
                                                                                                                        damage,
                                                                                                                        "targets"),
                                                                                                        setDamageProperties(
                                                                                                                        StringArgumentType
                                                                                                                                        .getString(damage,
                                                                                                                                                        "sourceString"),
                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                        damage,
                                                                                                                                        "isFire"),
                                                                                                                        false,
                                                                                                                        false,
                                                                                                                        false,
                                                                                                                        false,
                                                                                                                        false,
                                                                                                                        false,
                                                                                                                        EntityArgument.getEntity(
                                                                                                                                        damage,
                                                                                                                                        "sourceEntity"),
                                                                                                                        false,
                                                                                                                        false),
                                                                                                        FloatArgumentType
                                                                                                                        .getFloat(damage,
                                                                                                                                        "amount"));
                                                                                })
                                                                                .then(Commands.argument("pierceArmor",
                                                                                                BoolArgumentType.bool())
                                                                                                .executes((damage) -> {
                                                                                                        return damageEntity(
                                                                                                                        damage.getSource(),
                                                                                                                        EntityArgument.getEntities(
                                                                                                                                        damage,
                                                                                                                                        "targets"),
                                                                                                                        setDamageProperties(
                                                                                                                                        StringArgumentType
                                                                                                                                                        .getString(damage,
                                                                                                                                                                        "sourceString"),
                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                        damage,
                                                                                                                                                        "isFire"),
                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                        damage,
                                                                                                                                                        "pierceArmor"),
                                                                                                                                        false,
                                                                                                                                        false,
                                                                                                                                        false,
                                                                                                                                        false,
                                                                                                                                        false,
                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                        damage,
                                                                                                                                                        "sourceEntity"),
                                                                                                                                        false,
                                                                                                                                        false),
                                                                                                                        FloatArgumentType
                                                                                                                                        .getFloat(damage,
                                                                                                                                                        "amount"));
                                                                                                })
                                                                                                .then(Commands.argument(
                                                                                                                "difficultyScaled",
                                                                                                                BoolArgumentType.bool())
                                                                                                                .executes((damage) -> {
                                                                                                                        return damageEntity(
                                                                                                                                        damage.getSource(),
                                                                                                                                        EntityArgument.getEntities(
                                                                                                                                                        damage,
                                                                                                                                                        "targets"),
                                                                                                                                        setDamageProperties(
                                                                                                                                                        StringArgumentType
                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                        "sourceString"),
                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                        damage,
                                                                                                                                                                        "isFire"),
                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                        damage,
                                                                                                                                                                        "pierceArmor"),
                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                        damage,
                                                                                                                                                                        "difficultyScaled"),
                                                                                                                                                        false,
                                                                                                                                                        false,
                                                                                                                                                        false,
                                                                                                                                                        false,
                                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                                        damage,
                                                                                                                                                                        "sourceEntity"),
                                                                                                                                                        false,
                                                                                                                                                        false),
                                                                                                                                        FloatArgumentType
                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                        "amount"));
                                                                                                                })
                                                                                                                .then(Commands.argument(
                                                                                                                                "isMagic",
                                                                                                                                BoolArgumentType.bool())
                                                                                                                                .executes((damage) -> {
                                                                                                                                        return damageEntity(
                                                                                                                                                        damage.getSource(),
                                                                                                                                                        EntityArgument.getEntities(
                                                                                                                                                                        damage,
                                                                                                                                                                        "targets"),
                                                                                                                                                        setDamageProperties(
                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                        damage,
                                                                                                                                                                                        "isFire"),
                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                        damage,
                                                                                                                                                                                        "pierceArmor"),
                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                        damage,
                                                                                                                                                                                        "difficultyScaled"),
                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                        damage,
                                                                                                                                                                                        "isMagic"),
                                                                                                                                                                        false,
                                                                                                                                                                        false,
                                                                                                                                                                        false,
                                                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                                                        damage,
                                                                                                                                                                                        "sourceEntity"),
                                                                                                                                                                        false,
                                                                                                                                                                        false),
                                                                                                                                                        FloatArgumentType
                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                        "amount"));
                                                                                                                                })
                                                                                                                                .then(Commands.argument(
                                                                                                                                                "damageCreative",
                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                .executes((damage) -> {
                                                                                                                                                        return damageEntity(
                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                        EntityArgument.getEntities(
                                                                                                                                                                                        damage,
                                                                                                                                                                                        "targets"),
                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "pierceArmor"),
                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "difficultyScaled"),
                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "isMagic"),
                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "damageCreative"),
                                                                                                                                                                                        false,
                                                                                                                                                                                        false,
                                                                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "sourceEntity"),
                                                                                                                                                                                        false,
                                                                                                                                                                                        false),
                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                        "amount"));
                                                                                                                                                })
                                                                                                                                                .then(Commands.argument(
                                                                                                                                                                "isExplosion",
                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                        EntityArgument.getEntities(
                                                                                                                                                                                                        damage,
                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "pierceArmor"),
                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "difficultyScaled"),
                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "isMagic"),
                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "damageCreative"),
                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "isExplosion"),
                                                                                                                                                                                                        false,
                                                                                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "sourceEntity"),
                                                                                                                                                                                                        false,
                                                                                                                                                                                                        false),
                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                })
                                                                                                                                                                .then(Commands.argument(
                                                                                                                                                                                "isProjectile",
                                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                        EntityArgument.getEntities(
                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "pierceArmor"),
                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "difficultyScaled"),
                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "isMagic"),
                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "damageCreative"),
                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "isExplosion"),
                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "isProjectile"),
                                                                                                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "sourceEntity"),
                                                                                                                                                                                                                        false,
                                                                                                                                                                                                                        false),
                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                })
                                                                                                                                                                                .then(Commands.argument(
                                                                                                                                                                                                "absolute",
                                                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                                        EntityArgument.getEntities(
                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "pierceArmor"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "difficultyScaled"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "isMagic"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "damageCreative"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "isExplosion"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "isProjectile"),
                                                                                                                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "sourceEntity"),
                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "absolute"),
                                                                                                                                                                                                                                        false),
                                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                                })
                                                                                                                                                                                                .then(Commands.argument(
                                                                                                                                                                                                                "thorns",
                                                                                                                                                                                                                BoolArgumentType.bool())
                                                                                                                                                                                                                .executes((damage) -> {
                                                                                                                                                                                                                        return damageEntity(
                                                                                                                                                                                                                                        damage.getSource(),
                                                                                                                                                                                                                                        EntityArgument.getEntities(
                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                        "targets"),
                                                                                                                                                                                                                                        setDamageProperties(
                                                                                                                                                                                                                                                        StringArgumentType
                                                                                                                                                                                                                                                                        .getString(damage,
                                                                                                                                                                                                                                                                                        "sourceString"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "isFire"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "pierceArmor"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "difficultyScaled"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "isMagic"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "damageCreative"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "isExplosion"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "isProjectile"),
                                                                                                                                                                                                                                                        EntityArgument.getEntity(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "sourceEntity"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "absolute"),
                                                                                                                                                                                                                                                        BoolArgumentType.getBool(
                                                                                                                                                                                                                                                                        damage,
                                                                                                                                                                                                                                                                        "thorns")),
                                                                                                                                                                                                                                        FloatArgumentType
                                                                                                                                                                                                                                                        .getFloat(damage,
                                                                                                                                                                                                                                                                        "amount"));
                                                                                                                                                                                                                })))))))))))))));
        }

        private static EntityDamageSource setDamageProperties(String sourceString, boolean isFire, boolean pierceArmor,
                        boolean difficultyScaled, boolean isMagic, boolean damageCreative, boolean isExplosion,
                        boolean isProjectile, @Nullable Entity sourceEntity, boolean absolute, boolean thorns) {

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

                if (absolute == true) {
                        damage.setDamageIsAbsolute();
                }

                if (thorns == true) {
                        damage.setIsThornsDamage();
                }

                return damage;
        }

        private static int damageEntity(CommandSource source, Collection<? extends Entity> targets,
                        EntityDamageSource damageSource, float amount) throws CommandSyntaxException {
                List<Entity> list = Lists.newArrayListWithCapacity(targets.size());

                for (Entity entity : targets) {
                        if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).attackEntityFrom(damageSource, amount);
                                list.add(entity);
                        }
                }

                if (list.isEmpty()) {
                        throw DAMAGE_FAILED_EXCEPTION.create();
                } else {
                        if (list.size() == 1) {
                                source.sendFeedback(
                                                new TranslationTextComponent("commands.devsdream.damage.success.single",
                                                                list.iterator().next().getDisplayName(), amount),
                                                true);
                        } else {
                                source.sendFeedback(new TranslationTextComponent(
                                                "commands.devsdream.damage.success.multiple", list.size(), amount),
                                                true);
                        }

                        return (int) amount;
                }
        }
}