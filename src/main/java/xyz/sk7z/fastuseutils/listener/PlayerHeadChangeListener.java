package xyz.sk7z.fastuseutils.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHeadChangeListener extends ListenerFrame {

    public PlayerHeadChangeListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerHeadChange(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity target_entity = event.getRightClicked();
        Player target_player;
        if (player.isSneaking()) {
            return;
        }
        if (target_entity instanceof Player) {
            target_player = (Player) target_entity;
        } else {
            return;
        }
        ItemStack itemSkull = player.getInventory().getItemInMainHand();
        if (itemSkull.getType() == Material.PLAYER_HEAD) {
            playerHeadChange(itemSkull, target_player);
        }
    }

    public void playerHeadChange(ItemStack item_skull, Player player) {
        SkullMeta skull_meta = (SkullMeta) item_skull.getItemMeta();
        skull_meta.setOwningPlayer(player);
        item_skull.setItemMeta(skull_meta);
    }

}