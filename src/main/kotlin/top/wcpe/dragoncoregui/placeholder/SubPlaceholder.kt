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
    val format: String,
    val description: String,
    val exampleResultValue: String,
) {

    companion object {
        @JvmStatic
        fun load(key: String, configurationSection: ConfigurationSection): SubPlaceholder {
            val format = configurationSection.getString("format", "")
            val description = configurationSection.getString("description", "")
            val exampleResultValue = configurationSection.getString("example-result-value", "")

            return SubPlaceholder(
                key = key,
                format = format,
                description = description,
                exampleResultValue = exampleResultValue
            )
        }
    }
}