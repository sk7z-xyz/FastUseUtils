package xyz.sk7z.fastuseutils.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.sk7z.fastuseutils.FastUseUtils;
import xyz.sk7z.fastuseutils.FullChargeSound;
import xyz.sk7z.fastuseutils.Lag;
import xyz.sk7z.fastuseutils.player_options.AbstractPlayerShotOptions;
import xyz.sk7z.fastuseutils.player_options.PlayerShotBowOptions;


@SuppressWarnings("Duplicates")
public class ShotBowListener extends ListenerFrame {

    private FastUseUtils plg;


    public ShotBowListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUseUtils) plg_;
    }
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerShotBowEvent(EntityShootBowEvent event) {

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        ItemStack usedItem = event.getBow();

        //弓以外(クロスボウ)の場合は何もしない
        if (usedItem != null && (usedItem).getType() != Material.BOW) {
            return;
        }

        PlayerShotBowOptions playerShotBowOptions = plg.getPlayerValues(player).getPlayerShotBowOptions();
        if (!playerShotBowOptions.isEnabled()) {
            return;
        }

        playerShotBowOptions.setEndTime();
    }

    /* 右クリを離したタイミングで発射するので チャージ開始時間を記録するだけ */
    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        AbstractPlayerShotOptions playerShotBowOptions = plg.getPlayerValues(player).getPlayerShotBowOptions();
        ItemStack usedItem = event.getItem();

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (playerShotBowOptions.isEnabled()) {
            if (usedItem != null && (usedItem).getType() == Material.BOW) {
                //見つからなければ
                if (!playerShotBowOptions.isAlreadyStarted()) {
                    playerShotBowOptions.setStartTime();
                    playerShotBowOptions.setStart_tick(Lag.getTickCount());

                    new FullChargeSound(player, plg, playerShotBowOptions).runTaskLater(plg, 5);
                } else {
                    //120秒以上経過してたらやり直し
                    if (playerShotBowOptions.getElapsedTimeMillis() >= 120 * 1000) {
                        playerShotBowOptions.setStartTime();

                        new FullChargeSound(player, plg, playerShotBowOptions).runTaskLater(plg, 5);
                    }
                }
            }
        }
    }


}

