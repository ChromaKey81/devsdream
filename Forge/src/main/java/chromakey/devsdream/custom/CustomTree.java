package chromakey.devsdream.custom;

import java.util.Random;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class CustomTree extends Tree {

    private ConfiguredFeature<BaseTreeFeatureConfig, ?> configuredTreeFeature;

    public CustomTree(ConfiguredFeature<BaseTreeFeatureConfig, ?> configuredTreeFeature) {
        this.configuredTreeFeature = configuredTreeFeature;
    }

    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return this.configuredTreeFeature;
    }
    
}