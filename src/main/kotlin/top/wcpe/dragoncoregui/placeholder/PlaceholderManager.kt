package top.wcpe.dragoncoregui.placeholder

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.dragoncoregui.DragonCoreGui

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
class PlaceholderManager(private val plugin: JavaPlugin, private val fileName: String = "Placeholders.yml") {

    private val logger = DragonCoreGui.instance.logger
    private val placeholderMap = mutableMapOf<String, Placeholder>()

    fun reload() {
        placeholderMap.clear()
        val file = plugin.dataFolder.resolve(fileName)
        if (!file.exists()) {
            plugin.saveResource(fileName, false)
        }

        logger.info("start load Placeholders...")
        var i = 0
        val loadConfiguration = YamlConfiguration.loadConfiguration(file)
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