package xyz.sk7z.fastuseutils.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.world.level.block.CarpetBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.sk7z.fastuseutils.FastUseUtils;
import xyz.sk7z.fastuseutils.Utils;
import xyz.sk7z.fastuseutils.player_options.PlayerChairOptions;

public class ChairListener extends ListenerFrame {

    FastUseUtils plg;

    public ChairListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        this.plg = (FastUseUtils) plg_;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack usedItem = event.getItem();
        Block clickedBlock = event.getClickedBlock();
        double y = 0;

        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() != null) {
            return;
        }

        if (clickedBlock == null) {
            return;
        }

        if (Utils.isChairBlock(clickedBlock)) {
            y = 0;
            //レモン鯖向けカスタマイズ カーペットに座る
        } else if (((CraftBlock) clickedBlock).getNMS().getBlock() instanceof CarpetBlock){
            y = -0.5;
        }else{
            return;
        }

        Location loc = clickedBlock.getLocation().setDirection(player.getLocation().getDirection()).add(new Vector(0.5, 0, 0.5));

        loc.setY(loc.getY() + y);

        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 0.1, 0.1, 0.1)) {
            if (entity instanceof CraftArrow) {
                if ((entity).getPassengers().size() > 0) {
                    //椅子に座ってる人がいるならキャンセル
                    return;
                }
            }
        }
        player.teleport(loc.clone().add(0, 1, 0));//先にプレイヤーを飛ばして着地させておく FlyKick対策
        new PlayerChairScheduler(plg, player, loc).runTaskTimer(plg, 2, 1);

        event.setCancelled(true);
    }
}

class PlayerChairScheduler extends BukkitRunnable {
    private FastUseUtils plg;
    private Player player;
    private Arrow arrow;
    private Location spawn_loc;
    private Location arrow_loc;
    private PlayerChairOptions playerChairOptions;

    PlayerChairScheduler(FastUseUtils plg, Player player, Location spawn_loc) {
        this.plg = plg;
        this.player = player;
        this.playerChairOptions = plg.getPlayerValues(player).getPlayerChairOptions();
        this.spawn_loc = spawn_loc;
    }

    public void run() {
        //flying kick対策のため､2Tick後に座らせる
        if (this.arrow == null) {
            Arrow arrow = (Arrow) player.getWorld().spawnEntity(spawn_loc, EntityType.ARROW);
            arrow.addPassenger(player);

            this.arrow = arrow;
            this.arrow_loc = arrow.getLocation().clone();
            playerChairOptions.setArrow(arrow);
            return;
        }
        //矢から降りた場合は削除
        if (arrow.getPassengers().size() == 0) {
            arrow.remove();
            if (playerChairOptions.getArrow() == arrow) {
                //プレイヤーとの距離を測り､コマンドによる移動でなければ､プレイヤーを1マス上にテレポートさせる
                if (player.getLocation().distance(spawn_loc) < 2f) {
                    player.teleport(spawn_loc.setDirection(player.getLocation().getDirection()).add(0, 1, 0));
                }
            }
            cancel();
            return;
        }
        //矢の位置が変更された場合は削除
        if (!arrow.getLocation().equals(arrow_loc)) {
            arrow.remove();
            if (playerChairOptions.getArrow() == arrow) {
                //プレイヤーとの距離を測り､コマンドによる移動でなければ､プレイヤーを1マス上にテレポートさせる
                if (player.getLocation().distance(spawn_loc) < 2f) {
                    player.teleport(spawn_loc.setDirection(player.getLocation().getDirection()).add(0, 1, 0));
                }
            }
            cancel();
            return;
        }
        //変化のない場合は矢を延命させる
        arrow.setTicksLived(1);
    }
}
