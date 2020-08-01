package chromakey.devsdream.util;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

public class BlockCommandSourceHelper {
    
    public static CommandSource blockCommandSource(ServerWorld world, BlockPos pos) {
        return new CommandSource(ICommandSource.DUMMY, getVectorFromBlockPos(pos), Vector2f.ZERO, world, 4, "Server", new StringTextComponent("Server"), world.getServer(), (Entity)null);
    }

    public static Vector3d getVectorFromBlockPos(BlockPos pos) {
        return new Vector3d(pos.getX(), pos.getY(), pos.getZ());
    }

}