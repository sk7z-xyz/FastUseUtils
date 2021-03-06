package xyz.sk7z.fastuseutils.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuseutils.FastUseUtils;

import xyz.sk7z.fastuseutils.player_options.PlayerGlideOptions;


public class GlideListener extends ListenerFrame {
    private FastUseUtils plg;

    public GlideListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUseUtils) plg_;
    }


    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerGlideOptions playerGlideOptions = plg.getPlayerValues(player).getPlayerGlideOptions();
        ItemStack chestPlate_Item = player.getInventory().getChestplate();
        ItemStack usedItem = event.getItem();

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (playerGlideOptions.isEnabled()) {
            //エリトラを開いていなくてかつ 空中にいる場合のみ実行する
            if (!player.isGliding() && !player.isOnGround()) {
                if (chestPlate_Item != null && usedItem != null) {
                    if (chestPlate_Item.getType() == Material.ELYTRA && usedItem.getType() == Material.FIREWORK_ROCKET) {
                        Location l = player.getLocation();
                        player.teleport(l);
                        player.setGliding(true);
                    }
                }
            }
        }
    }
}
