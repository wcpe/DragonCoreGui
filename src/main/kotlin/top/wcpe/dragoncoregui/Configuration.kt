package top.wcpe.dragoncoregui

import org.bukkit.configuration.file.FileConfiguration

/**
 * 由 WCPE 在 2024/1/13 14:22 创建
 * <p>
 * Created by WCPE on 2024/1/13 14:22
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.1-SNAPSHOT
 */
class Configuration(config: FileConfiguration) {
    val debugEnable: Boolean = config.getBoolean("debug.enable")
    val debugDir: String = config.getString("debug.dir")
    val debugProduceYaml: String = config.getString("debug.produce-yaml")
}