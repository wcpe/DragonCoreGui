package top.wcpe.dragoncoregui.placeholder

import com.google.common.base.Charsets
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.dragoncoregui.DragonCoreGui
import java.io.InputStream
import java.io.InputStreamReader

/**
 * 由 WCPE 在 2024/1/20 15:58 创建
 * <p>
 * Created by WCPE on 2024/1/20 15:58
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.4.0-SNAPSHOT
 */
class PlaceholderManager(val plugin: JavaPlugin, private val fileName: String = "Placeholders.yml") {

    companion object {
        @JvmStatic
        private val placeholderMap = mutableMapOf<String, MutableMap<String, PlaceholderManager>>()

        @JvmStatic
        fun getPlaceholderManagerMap(): Map<String, Map<String, PlaceholderManager>> {
            return placeholderMap.toMap()
        }

        @JvmStatic
        fun getPlaceholderManagerMap(pluginName: String): Map<String, PlaceholderManager> {
            return placeholderMap.computeIfAbsent(pluginName) {
                mutableMapOf()
            }.toMap()
        }

        @JvmStatic
        fun register(placeholderManager: PlaceholderManager) {
            val pluginName = placeholderManager.plugin.name
            val placeholderManagerMap = placeholderMap.computeIfAbsent(pluginName) {
                mutableMapOf()
            }
            placeholderManagerMap[placeholderManager.fileName] = placeholderManager

            placeholderMap[pluginName] = placeholderManagerMap
        }
    }

    init {
        register(this)
    }

    fun init() = reload()


    fun toMarkDownDoc(): String {
        return placeholderMap.map { it.value.toMarkDownDoc() }.joinToString("\n")
    }

    private val logger = DragonCoreGui.instance.logger
    private val placeholderMap = mutableMapOf<String, Placeholder>()

    fun reload() {
        placeholderMap.clear()
        val resource = plugin.getResource(fileName) ?: return

        logger.info("start load Placeholders...")
        var i = 0
        val loadConfiguration = YamlConfiguration.loadConfiguration(InputStreamReader(resource, Charsets.UTF_8))

        for (key in loadConfiguration.getKeys(false)) {
            val keySection = loadConfiguration.getConfigurationSection(key) ?: continue
            val load = Placeholder.load(key, keySection)
            placeholderMap[key] = load
            i++
            logger.info("$key load success!")
        }
        logger.info("$i Placeholder are loaded successfully!")
    }

    fun getPlaceholder(key: String): Placeholder? {
        return placeholderMap[key]
    }
}