package chromakey.devsdream.deserialization;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;

import chromakey.devsdream.custom.CustomTree;
import chromakey.devsdream.util.JSONHelper;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.PressurePlateBlock.Sensitivity;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockDeserializer {

  public static Block deserializeBlock(JsonObject object) throws JsonSyntaxException {
    String type = JSONUtils.getString(object, "type");
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
        Block block = JSONHelper.getBlock(JSONUtils.getString(object, "grown_fruit"));
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
        String blockString = JSONUtils.getString(object, "chorus_plant");
        Block block = JSONHelper.getBlock(JSONUtils.getString(object, "chorus_plant"));
        if (block instanceof ChorusPlantBlock) {
          return new ChorusFlowerBlock((ChorusPlantBlock) block, deserializeProperties(object));
        } else {
          throw new JsonSyntaxException(blockString + " is not a chorus plant block");
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
        return new ConcretePowderBlock(JSONHelper.getBlock(JSONUtils.getString(object, "solidified_block")),
            deserializeProperties(object));
      }
      case "conduit": {
        return new ConduitBlock(deserializeProperties(object));
      }
      case "coral": {
        return new CoralBlock(JSONHelper.getBlock(JSONUtils.getString(object, "dead_block")),
            deserializeProperties(object));
      }
      case "coral_fan": {
        return new CoralFanBlock(deserializeProperties(object));
      }
      case "coral_fin": {
        return new CoralFinBlock(JSONHelper.getBlock(JSONUtils.getString(object, "dead_block")),
            deserializeProperties(object));
      }
      case "coral_plant": {
        return new CoralPlantBlock(JSONHelper.getBlock(JSONUtils.getString(object, "dead_block")),
            deserializeProperties(object));
      }
      case "coral_wall_fan": {
        return new CoralWallFanBlock(JSONHelper.getBlock(JSONUtils.getString(object, "dead_block")),
            deserializeProperties(object));
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
        return new FlowerBlock(JSONHelper.setRequiredEffectElement(object, "effect"),
            JSONUtils.getInt(object, "duration"), deserializeProperties(object));
      }
      case "flower_pot": {
        return new FlowerPotBlock(null, () -> {
          return JSONHelper.getBlock(JSONUtils.getString(object, "potted_block"));
        }, deserializeProperties(object));
      }
      case "flowing_fluid": {
        throw new JsonSyntaxException("Flowing fluid blocks are not yet supported (sorry!)");
      }
      case "four_way": {
        JsonObject node = JSONUtils.getJsonObject(object, "node");
        JsonObject extension = JSONUtils.getJsonObject(object, "extension");
        return new FourWayBlock(JSONUtils.getFloat(node, "width"), JSONUtils.getFloat(extension, "width"),
            JSONUtils.getFloat(node, "height"), JSONUtils.getFloat(extension, "height"),
            JSONUtils.getFloat(object, "vertical_collision"), deserializeProperties(object));
      }
      case "frosted_ice": {
        return new FrostedIceBlock(deserializeProperties(object));
      }
      case "fungus": {
        throw new JsonSyntaxException("Fungi are not yet supported (sorry!)");
      }
      case "furnace": {
        return new FurnaceBlock(deserializeProperties(object));
      }
      case "glass": {
        return new GlassBlock(deserializeProperties(object));
      }
      case "glazed_terracotta": {
        return new GlazedTerracottaBlock(deserializeProperties(object));
      }
      case "grass": {
        return new GrassBlock(deserializeProperties(object));
      }
      case "grass_path": {
        return new GrassPathBlock(deserializeProperties(object));
      }
      case "gravel": {
        return new GravelBlock(deserializeProperties(object));
      }
      case "grindstone": {
        return new GrindstoneBlock(deserializeProperties(object));
      }
      case "hay": {
        return new HayBlock(deserializeProperties(object));
      }
      case "honey": {
        return new HoneyBlock(deserializeProperties(object));
      }
      case "hopper": {
        return new HopperBlock(deserializeProperties(object));
      }
      case "horizontal_face": {
        return new HorizontalFaceBlock(deserializeProperties(object));
      }
      case "huge_mushroom": {
        return new HugeMushroomBlock(deserializeProperties(object));
      }
      case "ice": {
        return new IceBlock(deserializeProperties(object));
      }
      case "jigsaw": {
        return new JigsawBlock(deserializeProperties(object));
      }
      case "jukebox": {
        return new JukeboxBlock(deserializeProperties(object));
      }
      case "kelp": {
        return new KelpBlock(deserializeProperties(object));
      }
      case "kelp_top": {
        return new KelpTopBlock(deserializeProperties(object));
      }
      case "ladder": {
        return new LadderBlock(deserializeProperties(object));
      }
      case "lantern": {
        return new LanternBlock(deserializeProperties(object));
      }
      case "leaves": {
        return new LeavesBlock(deserializeProperties(object));
      }
      case "lectern": {
        return new LecternBlock(deserializeProperties(object));
      }
      case "lever": {
        return new LeverBlock(deserializeProperties(object));
      }
      case "lily_pad": {
        return new LilyPadBlock(deserializeProperties(object));
      }
      case "loom": {
        return new LoomBlock(deserializeProperties(object));
      }
      case "magma": {
        return new MagmaBlock(deserializeProperties(object));
      }
      case "melon": {
        return new MelonBlock(deserializeProperties(object));
      }
      case "moving_piston": {
        return new MovingPistonBlock(deserializeProperties(object));
      }
      case "mushroom": {
        return new MushroomBlock(deserializeProperties(object));
      }
      case "mycelium": {
        return new MyceliumBlock(deserializeProperties(object));
      }
      case "nether_portal": {
        return new NetherPortalBlock(deserializeProperties(object));
      }
      case "nether_roots": {
        return new NetherRootsBlock(deserializeProperties(object));
      }
      case "nether_sprouts": {
        return new NetherSproutsBlock(deserializeProperties(object));
      }
      case "nether_wart": {
        return new NetherWartBlock(deserializeProperties(object));
      }
      case "netherrack": {
        return new NetherrackBlock(deserializeProperties(object));
      }
      case "note": {
        return new NoteBlock(deserializeProperties(object));
      }
      case "nylium": {
        return new NyliumBlock(deserializeProperties(object));
      }
      case "observer": {
        return new ObserverBlock(deserializeProperties(object));
      }
      case "ore": {
        return new OreBlock(deserializeProperties(object));
      }
      case "pane": {
        return new PaneBlock(deserializeProperties(object));
      }
      case "piston": {
        return new PistonBlock(JSONUtils.getBoolean(object, "sticky"), deserializeProperties(object));
      }
      case "piston_head": {
        return new PistonHeadBlock(deserializeProperties(object));
      }
      case "potato": {
        return new PotatoBlock(deserializeProperties(object));
      }
      case "powered_rail": {
        return new PoweredRailBlock(deserializeProperties(object));
      }
      case "pressure_plate": {
        String sensitivityString = JSONUtils.getString(object, "sensitivity");
        switch (sensitivityString) {
          case "everything": {
            return new PressurePlateBlock(Sensitivity.EVERYTHING, deserializeProperties(object));
          }
          case "mobs": {
            return new PressurePlateBlock(Sensitivity.MOBS, deserializeProperties(object));
          }
          default: {
            throw new JsonSyntaxException(
                "Unknown sensitivity '" + sensitivityString + "'; only 'everything' or 'mobs' are accepted");
          }
        }
      }
      case "pumpkin": {
        return new PumpkinBlock(deserializeProperties(object));
      }
      case "rail": {
        return new RailBlock(deserializeProperties(object));
      }
      case "redstone": {
        return new RedstoneBlock(deserializeProperties(object));
      }
      case "redstone_lamp": {
        return new RedstoneLampBlock(deserializeProperties(object));
      }
      case "redstone_ore": {
        return new RedstoneOreBlock(deserializeProperties(object));
      }
      case "redstone_torch": {
        return new RedstoneTorchBlock(deserializeProperties(object));
      }
      case "redstone_wall_torch": {
        return new RedstoneWallTorchBlock(deserializeProperties(object));
      }
      case "redstone_wire": {
        return new RedstoneWireBlock(deserializeProperties(object));
      }
      case "repeater": {
        return new RepeaterBlock(deserializeProperties(object));
      }
      case "respawn_anchor": {
        return new RespawnAnchorBlock(deserializeProperties(object));
      }
      case "rotated_pillar": {
        return new RotatedPillarBlock(deserializeProperties(object));
      }
      case "sand": {
        return new SandBlock(JSONUtils.getInt(object, "dust_color"), deserializeProperties(object));
      }
      case "sapling": {
        return new SaplingBlock(new CustomTree(Feature.TREE.withConfiguration(BaseTreeFeatureConfig.CODEC
            .parse(JsonOps.INSTANCE, JSONUtils.getJsonObject(object, "tree")).getOrThrow(false, (error) -> {
              error = new String("Could not parse tree");
            }))), deserializeProperties(object));
      }
      case "scaffolding": {
        return new ScaffoldingBlock(deserializeProperties(object));
      }
      case "sea_grass": {
        return new SeaGrassBlock(deserializeProperties(object));
      }
      case "sea_pickle": {
        return new SeaPickleBlock(deserializeProperties(object));
      }
      case "shulker_box": {
        DyeColor color = null;
        if (object.has("color")) {
          color = DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE);
        }
        return new ShulkerBoxBlock(color, deserializeProperties(object));
      }
      case "silverfish": {
        return new SilverfishBlock(JSONHelper.getBlock(JSONUtils.getString(object, "mimics")),
            deserializeProperties(object));
      }
      case "six_way": {
        return new SixWayBlock(JSONUtils.getFloat(object, "apothem"), deserializeProperties(object));
      }
      case "skull": {
        String skullTypeString = JSONUtils.getString(object, "skull_type");
        switch (skullTypeString) {
          case "skeleton": {
            return new SkullBlock(SkullBlock.Types.SKELETON, deserializeProperties(object));
          }
          case "wither_skeleton": {
            return new SkullBlock(SkullBlock.Types.WITHER_SKELETON, deserializeProperties(object));
          }
          case "player": {
            return new SkullBlock(SkullBlock.Types.PLAYER, deserializeProperties(object));
          }
          case "zombie": {
            return new SkullBlock(SkullBlock.Types.ZOMBIE, deserializeProperties(object));
          }
          case "creeper": {
            return new SkullBlock(SkullBlock.Types.CREEPER, deserializeProperties(object));
          }
          case "dragon": {
            return new SkullBlock(SkullBlock.Types.DRAGON, deserializeProperties(object));
          }
          default: {
            throw new JsonSyntaxException("Unknown skull type: '" + skullTypeString + "'");
          }
        }
      }
      case "skull_player": {
        return new SkullPlayerBlock(deserializeProperties(object));
      }
      case "skull_wall_player": {
        return new SkullWallPlayerBlock(deserializeProperties(object));
      }
      case "slab": {
        return new SlabBlock(deserializeProperties(object));
      }
      case "slime": {
        return new SlimeBlock(deserializeProperties(object));
      }
      case "smithing_table": {
        return new SmithingTableBlock(deserializeProperties(object));
      }
      case "smoker": {
        return new SmokerBlock(deserializeProperties(object));
      }
      case "snow": {
        return new SnowBlock(deserializeProperties(object));
      }
      case "snowy_dirt": {
        return new SnowyDirtBlock(deserializeProperties(object));
      }
      case "soul_fire": {
        return new SoulFireBlock(deserializeProperties(object));
      }
      case "soul_sand": {
        return new SoulSandBlock(deserializeProperties(object));
      }
      case "spawner": {
        return new SpawnerBlock(deserializeProperties(object));
      }
      case "sponge": {
        return new SpongeBlock(deserializeProperties(object));
      }
      case "stained_glass": {
        DyeColor color = null;
        if (object.has("color")) {
          color = DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE);
        }
        return new StainedGlassBlock(color, deserializeProperties(object));
      }
      case "stained_glass_pane": {
        DyeColor color = null;
        if (object.has("color")) {
          color = DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE);
        }
        return new StainedGlassPaneBlock(color, deserializeProperties(object));
      }
      case "stairs": {
        return new StairsBlock(() -> {
          return JSONHelper.getBlock(JSONUtils.getString(object, "source_block")).getDefaultState();
        }, deserializeProperties(object));
      }
      case "standing_sign": {
        throw new JsonSyntaxException("Custom signs are not supported yet (sorry!)");
      }
      case "stem": {
        String blockString = JSONUtils.getString(object, "grown_fruit");
        Block block = JSONHelper.getBlock(JSONUtils.getString(object, "grown_fruit"));
        if (block instanceof StemGrownBlock) {
          return new StemBlock((StemGrownBlock) block, deserializeProperties(object));
        } else {
          throw new JsonSyntaxException(blockString + " is not a stem grown block");
        }
      }
      case "stone_button": {
        return new StoneButtonBlock(deserializeProperties(object));
      }
      case "stonecutter": {
        return new StonecutterBlock(deserializeProperties(object));
      }
      case "structure": {
        return new StructureBlock(deserializeProperties(object));
      }
      case "structure_void": {
        return new StructureVoidBlock(deserializeProperties(object));
      }
      case "sugar_cane": {
        return new SugarCaneBlock(deserializeProperties(object));
      }
      case "sweet_berry_bush": {
        return new SweetBerryBushBlock(deserializeProperties(object));
      }
      case "tnt": {
        return new TNTBlock(deserializeProperties(object));
      }
      case "tall_flower": {
        return new TallFlowerBlock(deserializeProperties(object));
      }
      case "tall_grass": {
        return new TallGrassBlock(deserializeProperties(object));
      }
      case "tall_sea_grass": {
        return new TallSeaGrassBlock(deserializeProperties(object));
      }
      case "target": {
        return new TargetBlock(deserializeProperties(object));
      }
      case "torch": {
        String particleString = JSONUtils.getString(object, "effect");
        ResourceLocation resourcelocation = new ResourceLocation(particleString);
        ParticleType<?> particle = RegistryObject.of(resourcelocation, ForgeRegistries.PARTICLE_TYPES)
            .orElseThrow(() -> {
              return new JsonSyntaxException("Unknown particle type '" + particleString + "'");
            });
        return new TorchBlock(deserializeProperties(object), (IParticleData) particle);
      }
      case "trap_door": {
        return new TrapDoorBlock(deserializeProperties(object));
      }
      case "trapped_chest": {
        return new TrappedChestBlock(deserializeProperties(object));
      }
      case "trip_wire": {
        String blockString = JSONUtils.getString(object, "hook");
        Block block = JSONHelper.getBlock(JSONUtils.getString(object, "hook"));
        if (block instanceof TripWireHookBlock) {
          return new TripWireBlock((TripWireHookBlock) block, deserializeProperties(object));
        } else {
          throw new JsonSyntaxException(blockString + " is not a trip wire hook");
        }
      }
      case "trip_wire_hook": {
        return new TripWireHookBlock(deserializeProperties(object));
      }
      case "turtle_egg": {
        return new TurtleEggBlock(deserializeProperties(object));
      }
      case "twisting_vines": {
        return new TwistingVinesBlock(deserializeProperties(object));
      }
      case "twisting_vines_top": {
        return new TwistingVinesTopBlock(deserializeProperties(object));
      }
      case "vine": {
        return new VineBlock(deserializeProperties(object));
      }
      case "wall_banner": {
        return new BannerBlock(DyeColor.byTranslationKey(JSONUtils.getString(object, "color"), DyeColor.WHITE),
            deserializeProperties(object));
      }
      case "wall": {
        return new WallBlock(deserializeProperties(object));
      }
      case "wall_sign": {
        throw new JsonSyntaxException("Custom signs are not supported yet (sorry!)");
      }
      case "wall_skull": {
        String skullTypeString = JSONUtils.getString(object, "skull_type");
        switch (skullTypeString) {
          case "skeleton": {
            return new WallSkullBlock(SkullBlock.Types.SKELETON, deserializeProperties(object));
          }
          case "wither_skeleton": {
            return new WallSkullBlock(SkullBlock.Types.WITHER_SKELETON, deserializeProperties(object));
          }
          case "player": {
            return new WallSkullBlock(SkullBlock.Types.PLAYER, deserializeProperties(object));
          }
          case "zombie": {
            return new WallSkullBlock(SkullBlock.Types.ZOMBIE, deserializeProperties(object));
          }
          case "creeper": {
            return new WallSkullBlock(SkullBlock.Types.CREEPER, deserializeProperties(object));
          }
          case "dragon": {
            return new WallSkullBlock(SkullBlock.Types.DRAGON, deserializeProperties(object));
          }
          default: {
            throw new JsonSyntaxException("Unknown skull type '" + skullTypeString + "'");
          }
        }
      }
      case "wall_torch": {
        String particleString = JSONUtils.getString(object, "effect");
        ResourceLocation resourcelocation = new ResourceLocation(particleString);
        ParticleType<?> particle = RegistryObject.of(resourcelocation, ForgeRegistries.PARTICLE_TYPES)
            .orElseThrow(() -> {
              return new JsonSyntaxException("Unknown particle type '" + particleString + "'");
            });
        return new TorchBlock(deserializeProperties(object), (IParticleData) particle);
      }
      case "web": {
        return new WebBlock(deserializeProperties(object));
      }
      case "weeping_vines": {
        return new WeepingVinesBlock(deserializeProperties(object));
      }
      case "weeping_vines_top": {
        return new WeepingVinesTopBlock(deserializeProperties(object));
      }
      case "weighted_pressure_plate": {
        return new WeightedPressurePlateBlock(JSONUtils.getInt(object, "max_weight"), deserializeProperties(object));
      }
      case "wet_sponge": {
        return new WetSpongeBlock(deserializeProperties(object));
      }
      case "wither_rose": {
        return new WitherRoseBlock(JSONHelper.setRequiredEffectElement(object, "effect"),
            deserializeProperties(object));
      }
      case "wither_skeleton_skull": {
        return new WitherSkeletonSkullBlock(deserializeProperties(object));
      }
      case "wither_skeleton_wall_skull": {
        return new WitherSkeletonWallSkullBlock(deserializeProperties(object));
      }
      case "wood_button": {
        return new WoodButtonBlock(deserializeProperties(object));
      }
      default: {
        throw new JsonSyntaxException("Unknown block type: " + type);
      }
    }
  }

  private static List<EntityType<?>> allowedSpawnableEntitiesList = Lists.newArrayList();

  private static Properties deserializeProperties(JsonObject object) throws JsonSyntaxException {
    JsonObject propertiesObj = JSONUtils.getJsonObject(object, "properties");
    Properties properties;
    if (propertiesObj.has("map_color")) {
      properties = Properties.create(deserializeMaterial(propertiesObj), deserializeColor(propertiesObj));
    } else {
      properties = Properties.create(deserializeMaterial(propertiesObj));
    }
    if (propertiesObj.has("blocks_movement")) {
      if (JSONUtils.getBoolean(propertiesObj, "blocks_movement") == false) {
        properties.doesNotBlockMovement();
      }
    }
    if (propertiesObj.has("sounds")) {
      if (propertiesObj.get("sounds").isJsonObject()) {
        JsonObject soundsObj = propertiesObj.get("sounds").getAsJsonObject();
        properties.sound(new SoundType(JSONUtils.getFloat(soundsObj, "volume"), JSONUtils.getFloat(soundsObj, "pitch"),
            JSONHelper.setRequiredSoundElement(soundsObj, "break"),
            JSONHelper.setRequiredSoundElement(soundsObj, "step"),
            JSONHelper.setRequiredSoundElement(soundsObj, "place"),
            JSONHelper.setRequiredSoundElement(soundsObj, "hit"),
            JSONHelper.setRequiredSoundElement(soundsObj, "fall")));
      } else {
        properties.sound(deserializeSounds(propertiesObj));
      }
    }
    if (propertiesObj.has("light")) {
      int lightLevel = JSONUtils.getInt(propertiesObj, "light");
      properties.setLightLevel((light) -> {
        return lightLevel;
      });
    }
    if (propertiesObj.has("hardness_and_resistance")) {
      if (propertiesObj.get("hardness_and_resistance").isJsonObject()) {
        JsonObject hardnessResistanceObj = propertiesObj.get("hardness_and_resistance").getAsJsonObject();
        properties.hardnessAndResistance(JSONUtils.getFloat(hardnessResistanceObj, "hardness"),
            JSONUtils.getFloat(hardnessResistanceObj, "resistance"));
      } else {
        properties.hardnessAndResistance(JSONUtils.getFloat(propertiesObj, "hardness_and_resistance"));
      }
    }
    if (propertiesObj.has("requires_tool")) {
      if (JSONUtils.getBoolean(propertiesObj, "requires_tool") == true) {
        properties.setRequiresTool();
      }
    }
    if (propertiesObj.has("ticks_randomly")) {
      if (JSONUtils.getBoolean(propertiesObj, "ticks_randomly") == true) {
        properties.tickRandomly();
      }
    }
    if (propertiesObj.has("slipperiness")) {
      properties.slipperiness(JSONUtils.getFloat(propertiesObj, "slipperiness"));
    }
    if (propertiesObj.has("speed_factor")) {
      properties.speedFactor(JSONUtils.getFloat(propertiesObj, "speed_factor"));
    }
    if (propertiesObj.has("jump_factor")) {
      properties.jumpFactor(JSONUtils.getFloat(propertiesObj, "jump_factor"));
    }
    if (propertiesObj.has("loot_from")) {
      properties.lootFrom(JSONHelper.getBlock(JSONUtils.getString(propertiesObj, "loot_from")));
    }
    if (propertiesObj.has("solid")) {
      if (JSONUtils.getBoolean(propertiesObj, "solid") == false) {
        properties.notSolid();
      }
    }
    if (propertiesObj.has("air")) {
      if (JSONUtils.getBoolean(propertiesObj, "air") == true) {
        properties.setAir();
      }
    }
    if (propertiesObj.has("harvest_level")) {
      properties.harvestLevel(JSONUtils.getInt(propertiesObj, "harvest_level"));
    }
    if (propertiesObj.has("harvest_tool")) {
      properties.harvestTool(ToolType.get(JSONUtils.getString(propertiesObj, "harvest_tool")));
    }
    if (propertiesObj.has("variable_opacity")) {
      if (JSONUtils.getBoolean(propertiesObj, "transparent") == true) {
        properties.variableOpacity();
      }
    }
    if (propertiesObj.has("drops")) {
      if (JSONUtils.getBoolean(propertiesObj, "drops") == false) {
        properties.noDrops();
      }
    }
    if (propertiesObj.has("allows_spawn")) {
      JsonElement allowsSpawn = propertiesObj.get("allows_spawn");
      if (allowsSpawn.isJsonArray()) {
        JsonArray spawnable = JSONUtils.getJsonArray(propertiesObj, "allows_spawn");
        spawnable.iterator().forEachRemaining((entityType) -> {
          allowedSpawnableEntitiesList.add(JSONHelper.getEntity(JSONUtils.getString(entityType, "entity type")));
        });
        properties.setAllowsSpawn(BlockDeserializer::allowsSpawn);
      } else {
        boolean allowsSpawnBool = JSONUtils.getBoolean(allowsSpawn, "boolean");
        if (allowsSpawnBool == false) {
          properties.setAllowsSpawn(BlockDeserializer::allowNoSpawns);
        } else {
          properties.setAllowsSpawn(BlockDeserializer::allowAllSpawns);
        }
      }
    }
    if (propertiesObj.has("opaque")) {
      boolean isOpaque = JSONUtils.getBoolean(propertiesObj, "opaque");
      if (isOpaque == true) {
        properties.setOpaque(BlockDeserializer::truePositionPredicate);
      } else {
        properties.setOpaque(BlockDeserializer::falsePositionPredicate);
      }
    }
    if (propertiesObj.has("suffocates")) {
      boolean suffocates = JSONUtils.getBoolean(propertiesObj, "suffocates");
      if (suffocates == true) {
        properties.setSuffocates(BlockDeserializer::truePositionPredicate);
      } else {
        properties.setSuffocates(BlockDeserializer::falsePositionPredicate);
      }
    }
    if (propertiesObj.has("blocks_vision")) {
      boolean blocksVision = JSONUtils.getBoolean(propertiesObj, "blocks_vision");
      if (blocksVision == true) {
        properties.setBlocksVision(BlockDeserializer::truePositionPredicate);
      } else {
        properties.setBlocksVision(BlockDeserializer::falsePositionPredicate);
      }
    }
    if (propertiesObj.has("needs_post_processing")) {
      boolean needsPostProcessing = JSONUtils.getBoolean(propertiesObj, "needs_post_processing");
      if (needsPostProcessing == true) {
        properties.setNeedsPostProcessing(BlockDeserializer::truePositionPredicate);
      } else {
        properties.setNeedsPostProcessing(BlockDeserializer::falsePositionPredicate);
      }
    }
    if (propertiesObj.has("emmisive_rendering")) {
      boolean emmisiveRendering = JSONUtils.getBoolean(propertiesObj, "emmisive_rendering");
      if (emmisiveRendering == true) {
        properties.setEmmisiveRendering(BlockDeserializer::truePositionPredicate);
      } else {
        properties.setEmmisiveRendering(BlockDeserializer::falsePositionPredicate);
      }
    }
    return properties;
  }

  private static Boolean allowsSpawn(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
    return allowedSpawnableEntitiesList.contains(entity);
  }

  private static Boolean allowNoSpawns(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
    return false;
  }

  private static Boolean allowAllSpawns(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
    return true;
  }

  private static boolean truePositionPredicate(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  private static boolean falsePositionPredicate(BlockState state, IBlockReader reader, BlockPos pos) {
    return false;
  }

  private static Material deserializeMaterial(JsonObject object) throws JsonSyntaxException {
    String materialString = JSONUtils.getString(object, "material");
    switch (materialString) {
      case "air": {
        return Material.AIR;
      }
      case "structure_void": {
        return Material.STRUCTURE_VOID;
      }
      case "portal": {
        return Material.PORTAL;
      }
      case "carpet": {
        return Material.CARPET;
      }
      case "plants": {
        return Material.PLANTS;
      }
      case "ocean_plant": {
        return Material.OCEAN_PLANT;
      }
      case "tall_plants": {
        return Material.TALL_PLANTS;
      }
      case "sea_grass": {
        return Material.SEA_GRASS;
      }
      case "water": {
        return Material.WATER;
      }
      case "bubble_column": {
        return Material.BUBBLE_COLUMN;
      }
      case "lava": {
        return Material.LAVA;
      }
      case "snow": {
        return Material.SNOW;
      }
      case "fire": {
        return Material.FIRE;
      }
      case "miscellaneous": {
        return Material.MISCELLANEOUS;
      }
      case "web": {
        return Material.WEB;
      }
      case "redstone_light": {
        return Material.REDSTONE_LIGHT;
      }
      case "clay": {
        return Material.CLAY;
      }
      case "earth": {
        return Material.EARTH;
      }
      case "organic": {
        return Material.ORGANIC;
      }
      case "packed_ice": {
        return Material.PACKED_ICE;
      }
      case "sand": {
        return Material.SAND;
      }
      case "sponge": {
        return Material.SPONGE;
      }
      case "shulker": {
        return Material.SHULKER;
      }
      case "wood": {
        return Material.WOOD;
      }
      case "hyphae": {
        return Material.NETHER_WOOD;
      }
      case "bamboo_sapling": {
        return Material.BAMBOO_SAPLING;
      }
      case "bamboo": {
        return Material.WOOL;
      }
      case "tnt": {
        return Material.TNT;
      }
      case "leaves": {
        return Material.LEAVES;
      }
      case "glass": {
        return Material.GLASS;
      }
      case "ice": {
        return Material.ICE;
      }
      case "cactus": {
        return Material.CACTUS;
      }
      case "rock": {
        return Material.ROCK;
      }
      case "iron": {
        return Material.IRON;
      }
      case "snow_block": {
        return Material.SNOW_BLOCK;
      }
      case "anvil": {
        return Material.ANVIL;
      }
      case "barrier": {
        return Material.BARRIER;
      }
      case "piston": {
        return Material.PISTON;
      }
      case "coral": {
        return Material.CORAL;
      }
      case "gourd": {
        return Material.GOURD;
      }
      case "dragon_egg": {
        return Material.DRAGON_EGG;
      }
      case "cake": {
        return Material.CAKE;
      }
      default: {
        throw new JsonSyntaxException("Unknown material '" + materialString + "'");
      }
    }
  }

  private static MaterialColor deserializeColor(JsonObject object) throws JsonSyntaxException {
    String color = JSONUtils.getString(object, "map_color");
    switch (color) {
      case "air": {
        return MaterialColor.AIR;
      }
      case "grass": {
        return MaterialColor.GRASS;
      }
      case "sand": {
        return MaterialColor.SAND;
      }
      case "wool": {
        return MaterialColor.WOOL;
      }
      case "tnt": {
        return MaterialColor.TNT;
      }
      case "ice": {
        return MaterialColor.ICE;
      }
      case "iron": {
        return MaterialColor.IRON;
      }
      case "foliage": {
        return MaterialColor.FOLIAGE;
      }
      case "snow": {
        return MaterialColor.SNOW;
      }
      case "clay": {
        return MaterialColor.CLAY;
      }
      case "dirt": {
        return MaterialColor.DIRT;
      }
      case "stone": {
        return MaterialColor.STONE;
      }
      case "water": {
        return MaterialColor.WATER;
      }
      case "wood": {
        return MaterialColor.WOOD;
      }
      case "quartz": {
        return MaterialColor.QUARTZ;
      }
      case "adobe": {
        return MaterialColor.ADOBE;
      }
      case "magenta": {
        return MaterialColor.MAGENTA;
      }
      case "light_blue": {
        return MaterialColor.LIGHT_BLUE;
      }
      case "yellow": {
        return MaterialColor.YELLOW;
      }
      case "lime": {
        return MaterialColor.LIME;
      }
      case "pink": {
        return MaterialColor.PINK;
      }
      case "gray": {
        return MaterialColor.GRAY;
      }
      case "light_gray": {
        return MaterialColor.LIGHT_GRAY;
      }
      case "cyan": {
        return MaterialColor.CYAN;
      }
      case "purple": {
        return MaterialColor.PURPLE;
      }
      case "blue": {
        return MaterialColor.BLUE;
      }
      case "brown": {
        return MaterialColor.BROWN;
      }
      case "green": {
        return MaterialColor.GREEN;
      }
      case "red": {
        return MaterialColor.RED;
      }
      case "black": {
        return MaterialColor.BLACK;
      }
      case "gold": {
        return MaterialColor.GOLD;
      }
      case "diamond": {
        return MaterialColor.DIAMOND;
      }
      case "lapis": {
        return MaterialColor.LAPIS;
      }
      case "emerald": {
        return MaterialColor.EMERALD;
      }
      case "obsidian": {
        return MaterialColor.OBSIDIAN;
      }
      case "netherrack": {
        return MaterialColor.NETHERRACK;
      }
      case "light_blue_terracotta": {
        return MaterialColor.LIGHT_BLUE_TERRACOTTA;
      }
      case "yellow_terracotta": {
        return MaterialColor.YELLOW_TERRACOTTA;
      }
      case "lime_terracotta": {
        return MaterialColor.LIME_TERRACOTTA;
      }
      case "pink_terracotta": {
        return MaterialColor.PINK_TERRACOTTA;
      }
      case "gray_terracotta": {
        return MaterialColor.GRAY_TERRACOTTA;
      }
      case "light_gray_terracotta": {
        return MaterialColor.LIGHT_GRAY_TERRACOTTA;
      }
      case "cyan_terracotta": {
        return MaterialColor.CYAN_TERRACOTTA;
      }
      case "purple_terracotta": {
        return MaterialColor.PURPLE_TERRACOTTA;
      }
      case "blue_terracotta": {
        return MaterialColor.BLUE_TERRACOTTA;
      }
      case "brown_terracotta": {
        return MaterialColor.BROWN_TERRACOTTA;
      }
      case "green_terracotta": {
        return MaterialColor.GREEN_TERRACOTTA;
      }
      case "red_terracotta": {
        return MaterialColor.RED_TERRACOTTA;
      }
      case "black_terracotta": {
        return MaterialColor.BLACK_TERRACOTTA;
      }
      case "crimson_nylium": {
        return MaterialColor.CRIMSON_NYLIUM;
      }
      case "crimson_stem": {
        return MaterialColor.CRIMSON_STEM;
      }
      case "crimson_hyphae": {
        return MaterialColor.CRIMSON_HYPHAE;
      }
      case "warped_nylium": {
        return MaterialColor.WARPED_NYLIUM;
      }
      case "warped_stem": {
        return MaterialColor.WARPED_STEM;
      }
      case "warped_hyphae": {
        return MaterialColor.WARPED_HYPHAE;
      }
      case "warped_wart": {
        return MaterialColor.WARPED_WART;
      }
      default: {
        throw new JsonSyntaxException("Unknown color '" + color + "'");
      }
    }
  }

  private static SoundType deserializeSounds(JsonObject object) {
    String sounds = JSONUtils.getString(object, "sounds");
    switch (sounds) {
      case "wood": {
        return SoundType.WOOD;
      }
      case "ground": {
        return SoundType.GROUND;
      }
      case "plant": {
        return SoundType.PLANT;
      }
      case "lily_pad": {
        return SoundType.LILY_PADS;
      }
      case "stone": {
        return SoundType.STONE;
      }
      case "metal": {
        return SoundType.METAL;
      }
      case "glass": {
        return SoundType.GLASS;
      }
      case "cloth": {
        return SoundType.CLOTH;
      }
      case "sand": {
        return SoundType.SAND;
      }
      case "snow": {
        return SoundType.SNOW;
      }
      case "ladder": {
        return SoundType.LADDER;
      }
      case "anvil": {
        return SoundType.ANVIL;
      }
      case "slime": {
        return SoundType.SLIME;
      }
      case "honey": {
        return SoundType.HONEY;
      }
      case "wet_grass": {
        return SoundType.WET_GRASS;
      }
      case "coral": {
        return SoundType.CORAL;
      }
      case "bamboo": {
        return SoundType.BAMBOO;
      }
      case "bamboo_sapling": {
        return SoundType.BAMBOO_SAPLING;
      }
      case "scaffolding": {
        return SoundType.SCAFFOLDING;
      }
      case "sweet_berry_bush": {
        return SoundType.SWEET_BERRY_BUSH;
      }
      case "crop": {
        return SoundType.CROP;
      }
      case "stem": {
        return SoundType.STEM;
      }
      case "vine": {
        return SoundType.VINE;
      }
      case "nether_wart": {
        return SoundType.NETHER_WART;
      }
      case "lantern": {
        return SoundType.LANTERN;
      }
      case "hyphae": {
        return SoundType.HYPHAE;
      }
      case "nylium": {
        return SoundType.NYLIUM;
      }
      case "fungus": {
        return SoundType.FUNGUS;
      }
      case "root": {
        return SoundType.ROOT;
      }
      case "shroomlight": {
        return SoundType.SHROOMLIGHT;
      }
      case "nether_vine": {
        return SoundType.NETHER_VINE;
      }
      case "low_pitch_nether_vine": {
        return SoundType.NETHER_VINE_LOWER_PITCH;
      }
      case "soul_sand": {
        return SoundType.SOUL_SAND;
      }
      case "soul_soil": {
        return SoundType.SOUL_SOIL;
      }
      case "basalt": {
        return SoundType.BASALT;
      }
      case "wart_block": {
        return SoundType.WART;
      }
      case "netherrack": {
        return SoundType.NETHERRACK;
      }
      case "nether_brick": {
        return SoundType.NETHER_BRICK;
      }
      case "nether_sprout": {
        return SoundType.NETHER_SPROUT;
      }
      case "nether_quartz_ore": {
        return SoundType.NETHER_ORE;
      }
      case "bone": {
        return SoundType.BONE;
      }
      case "netherite": {
        return SoundType.NETHERITE;
      }
      case "ancient_debris": {
        return SoundType.ANCIENT_DEBRIS;
      }
      case "lodestone": {
        return SoundType.LODESTONE;
      }
      case "chain": {
        return SoundType.CHAIN;
      }
      case "nether_gold_ore": {
        return SoundType.NETHER_GOLD;
      }
      case "gilded_blackstone": {
        return SoundType.GILDED_BLACKSTONE;
      }
      default: {
        throw new JsonSyntaxException("Unknown sound type '" + sounds + "'");
      }
    }
  }
}