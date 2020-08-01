package chromakey.devsdream.list;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockList {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<ResourceLocation, Block> blocks = Maps.newHashMap();
   private BlockList.IListener listener;


   @OnlyIn(Dist.CLIENT)
   private void remove(Block blockIn) {
      this.remove(blockIn);

      LOGGER.info("Forgot about block {}", (Object)blockIn.getRegistryName());
      this.blocks.remove(blockIn.getRegistryName());
      if (this.listener != null) {
        this.listener.blockRemoved(blockIn);
      }

   }

   @OnlyIn(Dist.CLIENT)
   public void removeAll(Set<ResourceLocation> ids) {
      for(ResourceLocation resourcelocation : ids) {
         Block block = this.blocks.get(resourcelocation);
         if (block == null) {
            LOGGER.warn("Told to remove block {} but I don't know what that is", (Object)resourcelocation);
         } else {
            this.remove(block);
         }
      }

   }

   public void loadBlocks(Map<ResourceLocation, Block> blocksIn) {

      while(!blocksIn.isEmpty()) {
         boolean flag = false;
         Iterator<Entry<ResourceLocation, Block>> iterator = blocksIn.entrySet().iterator();

         while(iterator.hasNext()) {
            Entry<ResourceLocation, Block> entry = iterator.next();
            ResourceLocation resourcelocation = entry.getKey();
            Block block = entry.getValue();
            this.blocks.put(resourcelocation, block);
            flag = true;
            iterator.remove();
         }

         if (!flag) {
            for(Entry<ResourceLocation, Block> entry1 : blocksIn.entrySet()) {
               LOGGER.error("Couldn't load block {}: {}", entry1.getKey(), entry1.getValue());
            }
            break;
         }
      }

      LOGGER.info("Loaded {} blocks", (int)this.blocks.size());
   }

   public void clear() {
      this.blocks.clear();
      if (this.listener != null) {
         this.listener.blocksCleared();
      }

   }

   public Collection<Block> getAll() {
      return this.blocks.values();
   }

   @Nullable
   public Block getBlock(ResourceLocation id) {
      return this.blocks.get(id);
   }

   public void setListener(@Nullable BlockList.IListener listenerIn) {
      this.listener = listenerIn;
      if (listenerIn != null) {
        for (Block block : this.getAll()) {
            listenerIn.blockAdded(block);
        }
      }

   }

   public interface IListener {
      void blockAdded(Block blockIn);

      void blockRemoved(Block blockIn);

      void blocksCleared();
   }
}