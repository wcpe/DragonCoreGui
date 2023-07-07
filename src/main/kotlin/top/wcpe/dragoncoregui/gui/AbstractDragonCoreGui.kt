package top.wcpe.dragoncoregui.gui

import eos.moe.dragoncore.config.Config
import eos.moe.dragoncore.network.PacketSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import top.wcpe.dragoncoregui.DragonCoreGui
import top.wcpe.dragoncoregui.DragonCoreGui.Companion.debug
import top.wcpe.dragoncoregui.compose.AbstractDragonCoreCompose
import top.wcpe.dragoncoregui.yaml.DragonCoreGuiYaml
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * 由 WCPE 在 2022/7/18 3:37 创建
 *
 * Created by WCPE on 2022/7/18 3:37
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
abstract class AbstractDragonCoreGui(
    path: String, private val fileName: String,
    open var match: String = "",
    open var through: String = "",
    open var priority: String = "",
    open var interactHud: String = "",
    open var currentItemSize: String = "",
    open var allowEscClose: String = "",
    open var hideHud: String = "",
    open var import: String = "",
) {

    private val dragonCoreGuiExpandConfig = DragonCoreGuiExpandConfig()

    /**
     * Player's Name to DragonCoreGuiExpandConfig
     */
    private val playerDragonCoreGuiExpandConfigMap = mutableMapOf<String, DragonCoreGuiExpandConfig>()

    fun consumerCompose(composeKey: String, consumer: Consumer<AbstractDragonCoreCompose>) {
        dragonCoreGuiExpandConfig.consumerCompose(composeKey, consumer)
    }

    fun consumerFunctionsCallBack(functionKey: String, consumer: Consumer<BiConsumer<String, Player>>) {
        dragonCoreGuiExpandConfig.consumerFunctionsCallBack(functionKey, consumer)
    }


    val fullPath = "$path$fileName"

    private val logger = DragonCoreGui.instance.logger

    private val file = DragonCoreGui.instance.dataFolder.toPath().parent.resolve(path).also {
        Files.createDirectories(it)
    }.toFile()

    init {
        this.reload()
        this.register()
    }

    private fun register() {
        DragonCoreGuiManager.registerDragonCoreGui(this)
    }


    private lateinit var baseYamlConfiguration: YamlConfiguration

    open fun reload() {
        baseYamlConfiguration = YamlConfiguration.loadConfiguration(File(file, "$fileName.yml"))
        logger.info("load base gui yaml $fullPath success!")
    }

    private fun getDebugFile(file: String): Path {
        return DragonCoreGui.instance.dataFolder.toPath().parent.resolve(
            DragonCoreGui.instance.config.getString("debug.dir")
        ).resolve(file)
    }

    fun consumerPlayerDragonCoreGuiExpandConfig(player: Player, consumer: Consumer<DragonCoreGuiExpandConfig>) {
        consumer.accept(playerDragonCoreGuiExpandConfigMap.computeIfAbsent(player.name) {
            DragonCoreGuiExpandConfig()
        })
    }

    /**
     * 清理玩家所有 Compose
     */
    open fun clearCompose(player: Player) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.clearCompose()
        }
    }

    /**
     * 给单独玩家添加 Compose
     */
    open fun addCompose(player: Player, compose: AbstractDragonCoreCompose) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.addCompose(compose)
        }
    }

    /**
     * 清理玩家所有 Function
     */
    open fun clearFunction(player: Player) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.clearFunction()
        }
    }

    /**
     * 给单独玩家添加龙之核心定义的 Function
     */
    open fun addFunction(player: Player, dragonCoreGuiFunction: DragonCoreGuiFunctions, function: String) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.addFunction(dragonCoreGuiFunction, function)
        }
    }

    /**
     * 给单独玩家添加龙之核心定义的 Function
     */
    open fun addFunction(player: Player, dragonCoreGuiFunction: DragonCoreGuiFunctions, function: List<String>) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.addFunction(dragonCoreGuiFunction, function)
        }
    }

    /**
     * 清理玩家所有 CustomFunction
     */
    open fun clearCustomFunction(player: Player) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.clearCustomFunction()
        }
    }

    /**
     * 给单独玩家添加自定义 Function
     */
    open fun addCustomFunction(player: Player, functionName: String, functionBody: String) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.addCustomFunction(functionName, functionBody)
        }
    }

    /**
     * 给单独玩家添加自定义 Function
     */
    open fun addCustomFunction(player: Player, functionName: String, functionBody: List<String>) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.addCustomFunction(functionName, functionBody)
        }
    }

    /**
     * 给单独玩家添加自定义 Function 回调函数
     */
    open fun addFunctionCallBack(
        player: Player, dragonCoreGuiFunction: DragonCoreGuiFunctions, callBack: BiConsumer<String, Player>
    ) {
        consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
            dragonCoreGuiExpandConfig.addFunctionCallBack(dragonCoreGuiFunction, callBack)
        }
    }


    open fun openGui(player: Player) {
        PacketSender.sendYaml(player, "Gui/$fullPath", DragonCoreGuiYaml().also { newYaml ->
            //从模板配置中读取 Compose 并放入界面
            for (key in baseYamlConfiguration.getKeys(false)) {
                newYaml[key] = baseYamlConfiguration[key]
            }
            newYaml["match"] = match
            newYaml["through"] = through
            newYaml["priority"] = priority
            newYaml["interactHud"] = interactHud
            newYaml["currentItemSize"] = currentItemSize
            newYaml["allowEscClose"] = allowEscClose
            newYaml["hideHud"] = hideHud
            newYaml["import"] = import

            dragonCoreGuiExpandConfig.handleDragonCoreGuiYaml(fullPath, newYaml)

            consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
                dragonCoreGuiExpandConfig.handleDragonCoreGuiYaml(fullPath, newYaml)
            }

            debug {
                newYaml.save(
                    getDebugFile(
                        DragonCoreGui.instance.config.getString("debug.produce-yaml").replace("%gui_key%", fileName)
                    ).toFile()
                )
            }
        })
        if ("hud".equals(match, true)) {
            PacketSender.sendOpenHud(player, fullPath)
        } else {
            PacketSender.sendOpenGui(player, fullPath)
        }
    }


    /**
     * 清空玩家缓存 Yaml 并将龙之核心配置文件夹下的配置全发送过去
     */
    open fun clearCacheYaml(player: Player) {
        Config.sendYamlToClient(player)
    }

    /**
     * 关闭 HUD
     */
    open fun closeHUD(player: Player) {
        PacketSender.sendRunFunction(player, fullPath, "方法.关闭界面", false)
    }

    open fun removePlaceholder(vararg players: Player, key: String) {
        for (player in players) {
            PacketSender.sendDeletePlaceholderCache(player, key, false)
        }
    }

    open fun sendPlaceholder(vararg players: Player, data: Map<String, String>) {
        for (player in players) {
            PacketSender.sendSyncPlaceholder(player, data)
        }
    }

    /**
     * 获取基础配置中的 Value
     */
    open fun getValue(path: String): String {
        return baseYamlConfiguration.getString(path)
    }

    /**
     * 设置基础配置中的 Value
     */
    open fun setValue(path: String, any: Any) {
        return baseYamlConfiguration.set(path, any)
    }

    /**
     * 清理所有 Compose
     */
    open fun clearCompose() {
        dragonCoreGuiExpandConfig.clearCompose()
    }

    /**
     * 添加 Compose
     */
    open fun addCompose(compose: AbstractDragonCoreCompose) {
        dragonCoreGuiExpandConfig.addCompose(compose)
    }

    /**
     * 清理所有 Function
     */
    open fun clearFunction() {
        dragonCoreGuiExpandConfig.clearFunction()
    }

    /**
     * 添加龙之核心定义的 Function
     */
    open fun addFunction(dragonCoreGuiFunction: DragonCoreGuiFunctions, function: String) {
        dragonCoreGuiExpandConfig.addFunction(dragonCoreGuiFunction, function)
    }

    /**
     * 添加龙之核心定义的 Function
     */
    open fun addFunction(dragonCoreGuiFunction: DragonCoreGuiFunctions, function: List<String>) {
        dragonCoreGuiExpandConfig.addFunction(dragonCoreGuiFunction, function)
    }

    /**
     * 清理所有 CustomFunction
     */
    open fun clearCustomFunction() {
        dragonCoreGuiExpandConfig.clearCustomFunction()
    }

    /**
     * 添加自定义 Function
     */
    open fun addCustomFunction(functionName: String, functionBody: String) {
        dragonCoreGuiExpandConfig.addCustomFunction(functionName, functionBody)
    }

    /**
     * 添加自定义 Function
     */
    open fun addCustomFunction(functionName: String, functionBody: List<String>) {
        dragonCoreGuiExpandConfig.addCustomFunction(functionName, functionBody)
    }

    /**
     * 添加自定义 Function 回调函数
     */
    open fun addFunctionCallBack(dragonCoreGuiFunction: DragonCoreGuiFunctions, callBack: BiConsumer<String, Player>) {
        dragonCoreGuiExpandConfig.addFunctionCallBack(dragonCoreGuiFunction, callBack)
    }
}