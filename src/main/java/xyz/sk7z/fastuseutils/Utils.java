package xyz.sk7z.fastuseutils;


import net.minecraft.world.level.block.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftBlock;

public class Utils {
    public static FastUseUtils plugin;


    public static boolean isChairBlock(Block block) {

        ((CraftBlock)block).getNMS();
        net.minecraft.world.level.block.Block nmsBlock = ((CraftBlock) block).getNMS().getBlock();
        if (nmsBlock instanceof StairBlock) {
            String dataStr = "[half=bottom]";
            BlockData data = Bukkit.createBlockData(block.getType(), dataStr);
            return block.getBlockData().matches(data);
        }
        return false;

    }


}
