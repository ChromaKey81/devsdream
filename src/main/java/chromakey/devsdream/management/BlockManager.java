package chromakey.devsdream.management;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chromakey.devsdream.deserialization.BlockDeserializer;
import chromakey.devsdream.list.BlockList;
import net.minecraft.block.Block;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class BlockManager extends JsonReloadListener {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).create();
    private BlockList blockList = new BlockList();

    public BlockManager() {
        super(GSON, "block");
    }

    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ResourceLocation, Block> map = Maps.newHashMap();
        objectIn.forEach((p_240923_2_, p_240923_3_) -> {
           try {
              JsonObject jsonobject = JSONUtils.getJsonObject(p_240923_3_, "advancement");
              Block block = BlockDeserializer.deserializeBlock(jsonobject);
              if (block == null) {
                  LOGGER.debug("Skipping loading block {}", p_240923_2_);
                  return;
              }
              map.put(p_240923_2_, block);
           } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
              LOGGER.error("Parsing error loading custom block {}: {}", p_240923_2_, jsonparseexception.getMessage());
           }
  
        });
        BlockList blocklist = new BlockList();
        blocklist.loadBlocks(map);
  
        this.blockList = blocklist;
     }

    @Nullable
    public Block getBlock(ResourceLocation id) {
       return this.blockList.getBlock(id);
    }
 
    public Collection<Block> getAllBlocks() {
       return this.blockList.getAll();
    }
}