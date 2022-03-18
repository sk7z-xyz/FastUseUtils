package xyz.sk7z.fastuseutils.player_options;


import org.bukkit.entity.Player;
import xyz.sk7z.fastuseutils.FastUseUtils;

public class PlayerOptions {
    private PlayerAttackOptions playerAttackOptions;
    private PlayerGlideOptions playerGlideOptions;
    private PlayerShotBowOptions playerShotBowOptions;

    private PlayerChairOptions playerChairOptions;

    public PlayerOptions(FastUseUtils plg, Player player) {
        this.playerAttackOptions = new PlayerAttackOptions();
        this.playerGlideOptions = new PlayerGlideOptions();
        this.playerShotBowOptions = new PlayerShotBowOptions();
        this.playerChairOptions = new PlayerChairOptions();
    }

    public PlayerAttackOptions getPlayerAttackOptions() {
        return playerAttackOptions;
    }


    public PlayerGlideOptions getPlayerGlideOptions() {
        return playerGlideOptions;
    }

    public PlayerShotBowOptions getPlayerShotBowOptions() {
        return playerShotBowOptions;
    }

    public PlayerChairOptions getPlayerChairOptions() {
        return playerChairOptions;
    }
}
