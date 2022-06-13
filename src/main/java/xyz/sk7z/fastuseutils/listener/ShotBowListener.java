package xyz.sk7z.fastuseutils.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.level.Level;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
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
        if (usedItem != null && !isBow(usedItem)) {
            return;
        }

        PlayerShotBowOptions playerShotBowOptions = plg.getPlayerValues(player).getPlayerShotBowOptions();
        if (!playerShotBowOptions.isEnabled()) {
            return;
        }

        //fastEatのときはイベント呼ばれなかったけど今回はイベントが呼ばれるので
        //1発目のノーマルで発射した矢は削除してその後プラグインで発射
        //2発目(プラグインでの発射)時には何もしない

        playerShotBowOptions.addShotCount();

        if (playerShotBowOptions.isPluginShot()) {
            //今回はプラグインからの発射なのでキャンセルしない
            //プラグインの発射もしない
            return;
        } else {
            //マイクラ本来の発射はキャンセルする
            //その後プラグインで発射する
            event.getProjectile().remove();
            //player.sendMessage("バニラの発射をキャンセルしました");
        }

        //spigotのItemStackをNMS(net.minecraft.server)ItemStackに変換する
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(usedItem);

        if (usedItem != null && isBow(usedItem)) {
            if (nmsItemStack.getItem() instanceof BowItem) {

                BowItem nmsItemBow = (BowItem) nmsItemStack.getItem();

                //player.sendMessage("チャージ時間" + shotValues.getElapsedTimeMillis());

                nmsItemBow.releaseUsing(nmsItemStack, ((CraftPlayer)player).getHandle().getLevel() , ((CraftPlayer)player).getHandle(), (int) (72000 - playerShotBowOptions.getElapsedTimeMillis() / 50));

                playerShotBowOptions.setEndTime();
            }
        }
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
            if (usedItem != null && isBow(usedItem)) {
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

    private boolean isBow(ItemStack item) {
        return CraftItemStack.asNMSCopy(item).getItem() instanceof BowItem;
    }
}

