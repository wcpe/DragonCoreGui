package top.wcpe.dragoncoregui.yaml

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration

/**
 * 由 WCPE 在 2022/7/19 1:06 创建
 *
 * Created by WCPE on 2022/7/19 1:06
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class DragonCoreGuiYaml : YamlConfiguration() {

    override fun set(path: String?, value: Any?) {
        value ?: return
        if ((path ?: return).isEmpty() || value is String && value.isEmpty()) {
            return
        }

        return super.set(
            path, if (value is ConfigurationSection) {
                val cConfig = createSection(path)
                for (key in value.getKeys(false)) {
                    cConfig.set(key, value.get(key))
                }
                cConfig
            } else {
                value
            }
        )
    }
}