package top.wcpe.dragoncoregui.gui

import eos.moe.dragoncore.config.Config
import eos.moe.dragoncore.network.PacketSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import top.wcpe.dragoncoregui.DragonCoreGui
import top.wcpe.dragoncoregui.compose.AbstractDragonCoreCompose
import top.wcpe.dragoncoregui.yaml.DragonCoreGuiYaml
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.BiConsumer

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
    private val functions: MutableMap<DragonCoreGuiFunctions, MutableList<String>> = mutableMapOf()
    private val customFunctions: MutableMap<String, MutableList<String>> = mutableMapOf()

    companion object {
        @JvmStatic
        val dragonCoreGuiComposeMap = mutableMapOf<String, MutableMap<String, AbstractDragonCoreCompose>>()

        @JvmStatic
        val dragonCoreGuiFunctionsCallBack = mutableMapOf<String, MutableMap<String, BiConsumer<String, Player>>>()
    }

    private val fullPath = "$path$fileName"
    private val logger = DragonCoreGui.instance.logger

    private val file = DragonCoreGui.instance.dataFolder.toPath().parent.resolve(path).also {
        Files.createDirectories(it)
    }.toFile()

    init {
        reload()
    }

    private lateinit var baseYamlConfiguration: YamlConfiguration

    fun reload() {
        baseYamlConfiguration = YamlConfiguration.loadConfiguration(File(file, "$fileName.yml"))
        logger.info("load base gui yaml $fullPath success!")
    }

    private fun getDebugFile(file: String): Path {
        return DragonCoreGui.instance.dataFolder.toPath().parent.resolve(
            DragonCoreGui.instance.config.getString("debug.dir")
        ).resolve(file)
    }

    fun openGui(player: Player) {
        val debug = DragonCoreGui.instance.config.getBoolean("debug.enable")
        PacketSender.sendYaml(player, "Gui/$fullPath", DragonCoreGuiYaml().also { newYaml ->
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
            for ((key, value) in customFunctions) {
                newYaml["Functions.${key}"] = "${
                    StringBuilder(newYaml.getString("Functions.${key}", "")).also { sb ->
                        for (s in value) {
                            if (s.isEmpty()) continue
                            sb.append(s).append(";\n")
                        }
                    }
                }"
            }

            for ((key, value) in functions) {
                newYaml["Functions.${key.functionName}"] = "${key.callBackMethod};${
                    StringBuilder(newYaml.getString("Functions.${key.functionName}", "")).also { sb ->
                        for (s in value) {
                            if (s.isEmpty()) continue
                            sb.append(s).append(";\n")
                        }
                    }
                }".replace(
                    "%gui_full_path%", fullPath
                )
            }

            dragonCoreGuiComposeMap[fullPath]?.let { composeMap ->
                for ((key, value) in composeMap) {
                    val convertToConfiguration = value.convertToConfiguration()
                    for (convertKey in value.convertToConfiguration().getKeys(false)) {
                        when (convertKey) {
                            "actions" -> {
                                val actionsConfig =
                                    convertToConfiguration.getConfigurationSection("actions") ?: continue
                                for (actionKey in actionsConfig.getKeys(false)) {
                                    actionsConfig.set(
                                        actionKey, actionsConfig.getString(actionKey).replace(
                                            "%gui_full_path%", fullPath
                                        )
                                    )
                                }
                                newYaml["$key.actions"] = actionsConfig
                            }

                            else -> {
                                newYaml["$key.$convertKey"] = convertToConfiguration.get(convertKey)
                            }
                        }

                    }
                }
            }
            if (debug) {
                newYaml.save(
                    getDebugFile(
                        DragonCoreGui.instance.config.getString("debug.produce-yaml").replace("%gui_key%", fileName)
                    )
                        .toFile()
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
    fun clearCacheYaml(player: Player) {
        Config.sendYamlToClient(player)
    }

    /**
     * 关闭 HUD
     */
    fun closeHUD(player: Player) {
        PacketSender.sendRunFunction(player, fullPath, "方法.关闭界面", false)
    }

    fun getValue(path: String): String {
        return baseYamlConfiguration.getString(path)
    }

    fun removePlaceholder(vararg players: Player, key: String) {
        for (player in players) {
            PacketSender.sendDeletePlaceholderCache(player, key, false)
        }
    }

    fun sendPlaceholder(vararg players: Player, data: Map<String, String>) {
        for (player in players) {
            PacketSender.sendSyncPlaceholder(player, data)
        }
    }

    /**
     * 清理缓存的 Compose
     */
    fun clearCompose() {
        dragonCoreGuiComposeMap[fullPath]?.clear()
    }

    fun addCompose(compose: AbstractDragonCoreCompose) {
        dragonCoreGuiComposeMap.computeIfAbsent(fullPath) {
            mutableMapOf()
        }[compose.key] = compose

    }

    /**
     * 清理缓存的 Function
     */
    fun clearFunction() {
        functions.clear()
    }

    fun addFunction(dragonCoreGuiFunction: DragonCoreGuiFunctions, function: String) {
        functions.computeIfAbsent(dragonCoreGuiFunction) {
            mutableListOf()
        }.add(function)
    }

    fun addFunction(dragonCoreGuiFunction: DragonCoreGuiFunctions, function: List<String>) {
        functions.computeIfAbsent(dragonCoreGuiFunction) {
            mutableListOf()
        }.addAll(function)
    }

    /**
     * 清理缓存的 CustomFunction
     */
    fun clearCustomFunction() {
        customFunctions.clear()
    }

    fun addCustomFunction(functionName: String, functionBody: String) {
        customFunctions.computeIfAbsent(functionName) {
            mutableListOf()
        }.add(functionBody)
    }

    fun addCustomFunction(functionName: String, functionBody: List<String>) {
        customFunctions.computeIfAbsent(functionName) {
            mutableListOf()
        }.addAll(functionBody)
    }

    fun addFunctionCallBack(dragonCoreGuiFunction: DragonCoreGuiFunctions, callBack: BiConsumer<String, Player>) {
        dragonCoreGuiFunctionsCallBack.computeIfAbsent(fullPath) {
            mutableMapOf()
        }[dragonCoreGuiFunction.functionName] = callBack
        functions.computeIfAbsent(dragonCoreGuiFunction) {
            mutableListOf("")
        }
    }
}