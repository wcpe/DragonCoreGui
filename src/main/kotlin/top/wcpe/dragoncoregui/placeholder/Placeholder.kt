package top.wcpe.dragoncoregui.placeholder

import org.bukkit.configuration.ConfigurationSection
import top.wcpe.dragoncoregui.DragonCoreGui

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
            var i = 0
            if (placeholders != null) {
                for (k in placeholders.getKeys(false)) {
                    val keySection = placeholders.getConfigurationSection(k) ?: continue
                    placeholderMap[k] = SubPlaceholder.load(k, keySection)
                    i++
                    DragonCoreGui.instance.logger.info("$k load success!")
                }
            }

            return Placeholder(
                key = key, formatKeyMap = formatKeyMap, placeholderMap = placeholderMap
            )
        }
    }

    private val docFormat = """
        ## %key% 变量
        
        %format_key_format%
        
        %placeholder_format%
    """.trimIndent()

    private val formatKeyStartFormat = """
        ### %key% 变量占位符解释 
        
        | 占位符 | 描述 |
        | :--- | :---: |
    """.trimIndent()

    private val formatKeyFormat = """
        | %format_name% | %description% |
    """.trimIndent()

    private val formatKeyFormatStringBuilder = StringBuilder(formatKeyStartFormat.replace("%key%", key))

    private fun putFormatKeyToDoc(
        formatName: String,
        description: String,
    ) {
        val f = formatKeyFormat.replace("%format_name%", formatName).replace("%description%", description)
        formatKeyFormatStringBuilder.append("\n")
        formatKeyFormatStringBuilder.append(f)
    }

    private val placeholderStartFormat = """
        ### %key% 变量
        
        | 变量 | 描述 | 示例返回值 | 
        | :--- | :---: | :---: |
    """.trimIndent()
    private val placeholderFormat = """
        | %placeholder% | %description% | %example_result_value% |
    """.trimIndent()

    private val placeholderFormatStringBuilder = StringBuilder(placeholderStartFormat.replace("%key%", key))


    private fun putPlaceholderToDoc(
        placeholder: String,
        description: String,
        exampleResultValue: String,
    ) {
        val f = placeholderFormat.replace("%placeholder%", placeholder).replace("%description%", description)
            .replace("%example_result_value%", exampleResultValue)
        placeholderFormatStringBuilder.append("\n")
        placeholderFormatStringBuilder.append(f)
    }

    fun toMarkDownDoc(): String {
        return docFormat.replace("%key%", key).replace(
            "%format_key_format%", if (formatKeyMap.isEmpty()) {
                ""
            } else {
                formatKeyFormatStringBuilder.toString()
            }
        ).replace(
            "%placeholder_format%", if (placeholderMap.isEmpty()) {
                ""
            } else {
                placeholderFormatStringBuilder.toString()
            }
        )
    }

    private fun replaceKeyFormat(s: String): String {
        var v = s
        for ((key, value) in formatKeyMap) {
            v = v.replace(key, value.name)
        }
        return v
    }

    init {
        for ((_, value) in formatKeyMap) {
            putFormatKeyToDoc(value.name, value.description)
        }
        for ((_, value) in placeholderMap) {
            val replaceKeyFormat = "${key}_${replaceKeyFormat(value.format)}"
            putPlaceholderToDoc(replaceKeyFormat, value.description, value.exampleResultValue)
        }
    }
}