package xyz.sk7z.fastuseutils;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Stairs;


public class Utils {
    public static FastUseUtils plugin;

    public static boolean isChairBlock(Block block) {
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Stairs) {
            Stairs stairsData = (Stairs) blockData;
            if (stairsData.getHalf() == Stairs.Half.BOTTOM) {
                return true;
            }
        }
        return false;
    }
}