package top.wcpe.dragoncoregui.placeholder

import org.bukkit.configuration.ConfigurationSection

/**
 * 由 WCPE 在 2024/1/21 14:29 创建
 * <p>
 * Created by WCPE on 2024/1/21 14:29
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.4.0-SNAPSHOT
 */
data class SubPlaceholder(
    val key: String,
    val oldCompatibleFormat: String,
    val oldCompatibleFormats: List<String>,
    val format: String,
    val formats: List<String>,
    val description: String,
    val exampleResultValue: String,
) {

    companion object {
        @JvmStatic
        fun load(key: String, configurationSection: ConfigurationSection): SubPlaceholder {
            val oldCompatibleFormat = configurationSection.getString("old-compatible-format", "")
            val oldCompatibleFormats = configurationSection.getStringList("old-compatible-formats")
            val format = configurationSection.getString("format", "")
            val formats = configurationSection.getStringList("formats")
            val description = configurationSection.getString("description", "")
            val exampleResultValue = configurationSection.getString("example-result-value", "")

            return SubPlaceholder(
                key = key,
                oldCompatibleFormat = oldCompatibleFormat,
                oldCompatibleFormats = oldCompatibleFormats,
                format = format,
                formats = formats,
                description = description,
                exampleResultValue = exampleResultValue
            )
        }
    }
}