package xyz.sk7z.fastuseutils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.sk7z.fastuseutils.player_options.AbstractPlayerShotOptions;
import xyz.sk7z.fastuseutils.player_options.PlayerShotBowOptions;

public class FullChargeSound extends BukkitRunnable {
    private FastUseUtils plugin;
    private Player player;
    private AbstractPlayerShotOptions playerShotValues;

    public FullChargeSound(Player player, FastUseUtils plugin, AbstractPlayerShotOptions playerShotValues) {
        this.player = player;
        this.plugin = plugin;
        this.playerShotValues = playerShotValues;
    }

    @Override
    public void run() {
        if (playerShotValues instanceof PlayerShotBowOptions) {
            if (Lag.getTickCount() - playerShotValues.getStart_tick() >= 20 || (Lag.getTickCount() - playerShotValues.getStart_tick() >= 5 && playerShotValues.getElapsedTimeMillis() >= 1000)) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 10);
            } else {
                new FullChargeSound(player, plugin, playerShotValues).runTaskLater(plugin, 1);
            }
        } else {
            if (Lag.getTickCount() - playerShotValues.getStart_tick() >= 10) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 10);
            } else {
                new FullChargeSound(player, plugin, playerShotValues).runTaskLater(plugin, 1);
            }
        }
    }
}
