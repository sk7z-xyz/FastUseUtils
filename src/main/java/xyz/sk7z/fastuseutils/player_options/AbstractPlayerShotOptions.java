package xyz.sk7z.fastuseutils.player_options;

public abstract class AbstractPlayerShotOptions extends AbstractTimer implements Options {
    private int shot_count = 0;
    private long start_tick;
    private boolean enabled = true;

    //PlayerValues以外にインスタンスを生成させないようにするためパッケージプライベートにする
    protected AbstractPlayerShotOptions() {
    }

    public void addShotCount() {
        this.shot_count++;
    }

    public boolean isPluginShot() {
        return shot_count % 2 == 0;
    }

    public long getStart_tick() {
        return start_tick;
    }

    public void setStart_tick(long start_tick) {
        this.start_tick = start_tick;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
