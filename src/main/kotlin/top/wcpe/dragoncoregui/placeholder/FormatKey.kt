package top.wcpe.dragoncoregui.placeholder

import org.bukkit.configuration.ConfigurationSection

/**
 * 由 WCPE 在 2024/1/21 14:21 创建
 * <p>
 * Created by WCPE on 2024/1/21 14:21
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.4.0-SNAPSHOT
 */
data class FormatKey(val key: String, val name: String, val description: String) {
    companion object {
        @JvmStatic
        fun load(key: String, configurationSection: ConfigurationSection): FormatKey {
            val name = configurationSection.getString("name", key)
            val description = configurationSection.getString("description", "")
            return FormatKey(
                key = key, name = name, description = description
            )
        }
    }
}