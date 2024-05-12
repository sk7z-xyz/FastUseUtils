
package xyz.sk7z.fastuseutils.config;

import jp.minecraftuser.ecoframework.ConfigFrame;
import jp.minecraftuser.ecoframework.PluginFrame;

/**
 * FastUseUtils設定ファイル
 * @author sk7z
 */
public class FastUseUtilsConfig extends ConfigFrame {

    public FastUseUtilsConfig(PluginFrame plg_) {
        super(plg_);
        plg.registerNotifiable(this);
    }

    /**
     * リロード検知
     * @param base 基底クラス動作指定
     */
    @Override
    public void reloadNotify(boolean base) {
        if (base) {
            super.reload();
        } else {
            
        }
    }
}
