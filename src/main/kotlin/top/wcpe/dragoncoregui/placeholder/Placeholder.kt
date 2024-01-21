package top.wcpe.dragoncoregui.placeholder

import org.bukkit.configuration.ConfigurationSection

/**
 * 由 WCPE 在 2024/1/21 14:28 创建
 * <p>
 * Created by WCPE on 2024/1/21 14:28
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.4.0-SNAPSHOT
 */
data class Placeholder(
    val key: String,
    val formatKeyMap: Map<String, FormatKey>,
    val placeholderMap: Map<String, SubPlaceholder>,
) {
    companion object {
        @JvmStatic
        fun load(key: String, configurationSection: ConfigurationSection): Placeholder {
            val formatKeyMap = mutableMapOf<String, FormatKey>()
            val formatKey = configurationSection.getConfigurationSection("format-key")
            if (formatKey != null) {
                for (k in formatKey.getKeys(false)) {
                    val keySection = formatKey.getConfigurationSection(k) ?: continue
                    formatKeyMap[k] = FormatKey.load(k, keySection)
                }
            }
            val placeholderMap = mutableMapOf<String, SubPlaceholder>()
            val placeholders = configurationSection.getConfigurationSection("placeholders")
            if (placeholders != null) {
                for (k in placeholders.getKeys(false)) {
                    val keySection = placeholders.getConfigurationSection(k) ?: continue
                    placeholderMap[k] = SubPlaceholder.load(k, keySection)
                }
            }

            return Placeholder(
                key = key,
                formatKeyMap = formatKeyMap,
                placeholderMap = placeholderMap
            )
        }
    }
}