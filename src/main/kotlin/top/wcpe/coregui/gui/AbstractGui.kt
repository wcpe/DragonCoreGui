package top.wcpe.coregui.gui

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import top.wcpe.dragoncoregui.DragonCoreGui
import java.io.File
import java.nio.file.Files
import java.util.logging.Logger

/**
 * 由 WCPE 在 2024/4/13 上午10:29 创建
 * <p>
 * Created by WCPE on 2024/4/13 上午10:29
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */
abstract class AbstractGui(
    path: String, val fileName: String, val hud: Boolean,
) {

    private val logger: Logger = DragonCoreGui.instance.logger

    val fullPath = "$path$fileName"
    private val dirFile = DragonCoreGui.instance.dataFolder.toPath().parent.resolve(path).also {
        Files.createDirectories(it)
    }.toFile()

    private val guiFile = File(dirFile, "${fileName}.yml")
    var yamlConfiguration: YamlConfiguration = YamlConfiguration.loadConfiguration(guiFile)


    fun reload() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(guiFile)
        logger.info("load base gui yaml ${guiFile.nameWithoutExtension} success!")
    }

    fun sendYaml(player: Player) {
        DragonCoreGui.coreManager.sendYaml(player, this)
    }

    open fun openGui(player: Player) {
        if (hud) {
            DragonCoreGui.coreManager.openHud(player, fullPath)
        } else {
            DragonCoreGui.coreManager.openGui(player, fullPath)
        }
    }

    fun closeGui(player: Player) {
        DragonCoreGui.coreManager.closeGui(player, fullPath)
    }

}