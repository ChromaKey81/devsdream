package chromakey.devsdream.block;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.AirBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BeetrootBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.block.BushBlock;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.CarrotBlock;
import net.minecraft.block.CartographyTableBlock;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.ConduitBlock;
import net.minecraft.block.CoralBlock;
import net.minecraft.block.CoralFanBlock;
import net.minecraft.block.CoralFinBlock;
import net.minecraft.block.CoralPlantBlock;
import net.minecraft.block.CoralWallFanBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.DeadCoralPlantBlock;
import net.minecraft.block.DeadCoralWallFanBlock;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.EndRodBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class BlockDeserializer {

  public static Block deserializeBlock(JsonObject object, String fileName) throws JsonSyntaxException {
    if (!object.has("type")) {
      throw new JsonSyntaxException("Missing block type, expected to find a string");
    } else {
      String type = object.get("type").getAsString();
      switch (type) {
        case "simple": {
          return new Block(deserializeProperties(object));
        }
        case "air": {
          return new AirBlock(deserializeProperties(object));
        }
        case "anvil": {
          return new AnvilBlock(deserializeProperties(object));
        }
        case "attached_stem": {
          String blockString = JSONUtils.getString(object, "grown_fruit");
          Block block = setRequiredBlockElement(object, "grown_fruit", blockString);
          if (block instanceof StemGrownBlock) {
            return new AttachedStemBlock((StemGrownBlock) block, deserializeProperties(object));
          } else {
            throw new JsonSyntaxException(blockString + " is not a stem grown block");
          }
        }
        case "bamboo": {
          return new BambooBlock(deserializeProperties(object));
        }
        case "bamboo_sapling": {
          return new BambooSaplingBlock(deserializeProperties(object));
        }
        case "banner": {
          return new BannerBlock(DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE),
              deserializeProperties(object));
        }
        case "barrel": {
          return new BarrelBlock(deserializeProperties(object));
        }
        case "barrier": {
          return new BarrierBlock(deserializeProperties(object));
        }
        case "beacon": {
          return new BeaconBlock(deserializeProperties(object));
        }
        case "bed": {
          return new BedBlock(DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE),
              deserializeProperties(object));
        }
        case "beehive": {
          return new BeehiveBlock(deserializeProperties(object));
        }
        case "beetroot": {
          return new BeetrootBlock(deserializeProperties(object));
        }
        case "bell": {
          return new BellBlock(deserializeProperties(object));
        }
        case "blast_furnace": {
          return new BlastFurnaceBlock(deserializeProperties(object));
        }
        case "breakable": {
          return new BreakableBlock(deserializeProperties(object));
        }
        case "brewing_stand": {
          return new BrewingStandBlock(deserializeProperties(object));
        }
        case "bubble_column": {
          return new BubbleColumnBlock(deserializeProperties(object));
        }
        case "bush": {
          return new BushBlock(deserializeProperties(object));
        }
        case "cactus": {
          return new CactusBlock(deserializeProperties(object));
        }
        case "cake": {
          return new CakeBlock(deserializeProperties(object));
        }
        case "campfire": {
          int damageScale = 1;
          boolean doParticles = true;
          if (object.has("damage_scale")) {
            damageScale = JSONUtils.getInt(object, "damage_scale");
          }
          if (object.has("do_particles")) {
            doParticles = JSONUtils.getBoolean(object, "do_particles");
          }
          return new CampfireBlock(doParticles, damageScale, deserializeProperties(object));
        }
        case "carpet": {
          return new CarpetBlock(DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE),
              deserializeProperties(object));
        }
        case "carrot": {
          return new CarrotBlock(deserializeProperties(object));
        }
        case "cartography_table": {
          return new CartographyTableBlock(deserializeProperties(object));
        }
        case "carved_pumpkin": {
          return new CarvedPumpkinBlock(deserializeProperties(object));
        }
        case "cauldron": {
          return new CauldronBlock(deserializeProperties(object));
        }
        case "chain": {
          return new ChainBlock(deserializeProperties(object));
        }
        case "chest": {
          return new ChestBlock(deserializeProperties(object), () -> {
            return TileEntityType.CHEST;
          });
        }
        case "chorus_flower": {
          String blockString = JSONUtils.getString(object, "grown_fruit");
          Block block = setRequiredBlockElement(object, "grown_fruit", blockString);
          if (block instanceof ChorusPlantBlock) {
            return new ChorusFlowerBlock((ChorusPlantBlock) block, deserializeProperties(object));
          } else {
            throw new JsonSyntaxException(blockString + " is not a stem grown block");
          }
        }
        case "chorus_plant": {
          return new ChorusPlantBlock(deserializeProperties(object));
        }
        case "cocoa": {
          return new CocoaBlock(deserializeProperties(object));
        }
        case "command_block": {
          return new CommandBlockBlock(deserializeProperties(object));
        }
        case "comparator": {
          return new ComparatorBlock(deserializeProperties(object));
        }
        case "composter": {
          return new ComposterBlock(deserializeProperties(object));
        }
        case "concrete_powder": {
          return new ConcretePowderBlock(setRequiredBlockElement(object, "solidified_block", JSONUtils.getString(object, "solidified_block")), deserializeProperties(object));
        }
        case "conduit": {
          return new ConduitBlock(deserializeProperties(object));
        }
        case "coral": {
          if(object.has("dead_block")) {
            String deadBlockString = JSONUtils.getString(object, "dead_block");
            ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(object, "dead_block"));
            Block block = Registry.BLOCK.getValue(resourcelocation).orElseThrow(() -> {
              return new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
            });
            return new CoralBlock(block, deserializeProperties(object));
          } else {
            throw new JsonSyntaxException("Missing dead_block, expected to find a string");
          }
        }
        case "coral_fan": {
          return new CoralFanBlock(deserializeProperties(object));
        }
        case "coral_fin": {
          return new CoralFinBlock(setRequiredBlockElement(object, "dead_block", JSONUtils.getString(object, "dead_block")), deserializeProperties(object));
        }
        case "coral_plant": {
          return new CoralPlantBlock(setRequiredBlockElement(object, "dead_block", JSONUtils.getString(object, "dead_block")), deserializeProperties(object));
        }
        case "coral_wall_fan": {
          return new CoralWallFanBlock(setRequiredBlockElement(object, "dead_block", JSONUtils.getString(object, "dead_block")), deserializeProperties(object));

        }
        case "crafting_table": {
          return new CraftingTableBlock(deserializeProperties(object));
        }
        case "crops": {
          return new CropsBlock(deserializeProperties(object));
        }
        case "crying_obsidian": {
          return new CryingObsidianBlock(deserializeProperties(object));
        }
        case "daylight_detector": {
          return new DaylightDetectorBlock(deserializeProperties(object));
        }
        case "dead_bush": {
          return new DeadBushBlock(deserializeProperties(object));
        }
        case "dead_coral_plant": {
          return new DeadCoralPlantBlock(deserializeProperties(object));
        }
        case "dead_coral_wall_fan": {
          return new DeadCoralWallFanBlock(deserializeProperties(object));
        }
        case "detector_rail": {
          return new DetectorRailBlock(deserializeProperties(object));
        }
        case "dispenser": {
          return new DispenserBlock(deserializeProperties(object));
        }
        case "door": {
          return new DoorBlock(deserializeProperties(object));
        }
        case "double_plant": {
          return new DoublePlantBlock(deserializeProperties(object));
        }
        case "dragon_egg": {
          return new DragonEggBlock(deserializeProperties(object));
        }
        case "dropper": {
          return new DropperBlock(deserializeProperties(object));
        }
        case "enchanting_table": {
          return new EnchantingTableBlock(deserializeProperties(object));
        }
        case "end_gateway": {
          return new EndGatewayBlock(deserializeProperties(object));
        }
        case "end_portal": {
          return new EndPortalBlock(deserializeProperties(object));
        }
        case "end_portal_frame": {
          return new EndPortalFrameBlock(deserializeProperties(object));
        }
        case "end_rod": {
          return new EndRodBlock(deserializeProperties(object));
        }
        case "ender_chest": {
          return new EnderChestBlock(deserializeProperties(object));
        }
        case "falling": {
          return new FallingBlock(deserializeProperties(object));
        }
        case "farmland": {
          return new FarmlandBlock(deserializeProperties(object));
        }
        case "fence": {
          return new FenceBlock(deserializeProperties(object));
        }
        case "fence_gate": {
          return new FenceGateBlock(deserializeProperties(object));
        }
        case "fire": {
          return new FireBlock(deserializeProperties(object));
        }
        case "fletching_table": {
          return new FletchingTableBlock(deserializeProperties(object));
        }
        case "flower": {
          String effectString = JSONUtils.getString(object, "effect");
          ResourceLocation resourcelocation = new ResourceLocation(effectString);
          Effect effect = Registry.EFFECTS.getValue(resourcelocation).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown effect '" + resourcelocation + "'");
          });
          if (object.has("duration")) {
            return new FlowerBlock(effect, JSONUtils.getInt(object, "duration"), deserializeProperties(object));
          } else {
            throw new JsonSyntaxException("Missing duration, expected an integer");
          }
        }
        case "flower_pot": {
          return new FlowerPotBlock(null, () -> {
            return setRequiredBlockElement(object, "potted_block", JSONUtils.getString(object, "potted"));
          }, deserializeProperties(object));
        }
        case "flowing_fluid": {
          switch (JSONUtils.getString(object, "fluid")) {
            case "water": {
              return new FlowingFluidBlock(Fluids.WATER, deserializeProperties(object));
            }
            case "lava": {
              return new FlowingFluidBlock(Fluids.LAVA, deserializeProperties(object));
            }
            default: {
              throw new JsonSyntaxException("Custom fluids are not currently supported");
            }
          }
        }
        case "four_way": {
          if (!object.has("node_width")) {
            throw new JsonSyntaxException("Missing node_width, expected to find a float");
          }
          if (!object.has("extension_width")) {
            throw new JsonSyntaxException("Missing extension_width, expected to find a float");
          }
          if (!object.has("unmapped_argument_one")) {
            throw new JsonSyntaxException("Missing unmapped_argument_one, expected to find a float");
          }
          if (!object.has("unmapped_argument_two")) {
            throw new JsonSyntaxException("Missing unmapped_argument_two, expected to find a float");
          }
          if (!object.has("collision_y")) {
            throw new JsonSyntaxException("Missing collision_y, expected to find a float");
          }
          return new FourWayBlock(JSONUtils.getFloat(object, "node_width"), JSONUtils.getFloat(object, "extension_width"), JSONUtils.getFloat(object, "unmapped_argument_one"), JSONUtils.getFloat(object, "unmapped_argument_two"), JSONUtils.getFloat(object, "collision_y"), deserializeProperties(object));
        }
        case "frosted_ice": {
          return new FrostedIceBlock(deserializeProperties(object));
        }
        default: {
          throw new JsonSyntaxException("Unknown block type: " + type);
        }
      }
    }
  }

  public static Properties deserializeProperties(JsonObject object) {
    Properties properties = Properties.create(Material.ROCK);
    if (!object.has("light_level")) {
      properties.setLightLevel((light) -> {
        return 0;
      });
    } else {
      properties.setLightLevel((light) -> {
        return JSONUtils.getInt(object, "light_level");
      });
    }
    return properties;
  }

  private static Block setRequiredBlockElement(JsonObject object, String element, String argument) {
    if(object.has(element)) {
      ResourceLocation resourcelocation = new ResourceLocation(argument);
      Block block = Registry.BLOCK.getValue(resourcelocation).orElseThrow(() -> {
        return new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
      });
      return block;
    } else {
      throw new JsonSyntaxException("Missing " + element + ", expected to find a string");
    }
  }
}