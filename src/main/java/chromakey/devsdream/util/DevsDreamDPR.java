package chromakey.devsdream.util;

import chromakey.devsdream.management.BlockManager;
import net.minecraft.command.Commands;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;

public class DevsDreamDPR extends DataPackRegistries {

    private final IReloadableResourceManager resourceManager = new SimpleReloadableResourceManager(ResourcePackType.SERVER_DATA);
    private final BlockManager blockManager = new BlockManager();

    public DevsDreamDPR(Commands.EnvironmentType p_i232598_1_, int p_i232598_2_) {
        super(p_i232598_1_, p_i232598_2_);
        this.resourceManager.addReloadListener(this.blockManager);
    }
    
    public BlockManager getBlockManager() {
        return this.blockManager;
    }
}