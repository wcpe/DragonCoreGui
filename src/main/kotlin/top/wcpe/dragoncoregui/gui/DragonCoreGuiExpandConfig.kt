package top.wcpe.dragoncoregui.gui

import org.bukkit.entity.Player
import top.wcpe.dragoncoregui.compose.AbstractDragonCoreCompose
import top.wcpe.dragoncoregui.yaml.DragonCoreGuiYaml
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * 由 WCPE 在 2023/4/13 2:18 创建
 * <p>
 * Created by WCPE on 2023/4/13 2:18
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-1
 */
class DragonCoreGuiExpandConfig {
    private val functions: MutableMap<DragonCoreGuiFunctions, MutableList<String>> = mutableMapOf()

    fun consumerFunctions(consumer: Consumer<Map<DragonCoreGuiFunctions, List<String>>>) {
        consumer.accept(functions)
    }

    private val customFunctions: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun consumerCustomFunctions(consumer: Consumer<Map<String, List<String>>>) {
        consumer.accept(customFunctions)
    }

    private val dragonCoreGuiComposeMap = mutableMapOf<String, AbstractDragonCoreCompose>()

    fun consumerDragonCoreGuiComposeMap(consumer: Consumer<Map<String, AbstractDragonCoreCompose>>) {
        consumer.accept(dragonCoreGuiComposeMap)
    }

    fun consumerCompose(composeKey: String, consumer: Consumer<AbstractDragonCoreCompose>) {
        consumer.accept(dragonCoreGuiComposeMap[composeKey] ?: return)
    }

    private val dragonCoreGuiFunctionsCallBack = mutableMapOf<String, BiConsumer<String, Player>>()

    fun consumerFunctionsCallBack(functionKey: String, consumer: Consumer<BiConsumer<String, Player>>) {
        consumer.accept(dragonCoreGuiFunctionsCallBack[functionKey] ?: return)
    }

    /**
     * 清理所有 Compose
     */
    fun clearCompose() {
        dragonCoreGuiComposeMap.clear()
    }

    /**
     * 添加 Compose
     */
    fun addCompose(compose: AbstractDragonCoreCompose) {
        dragonCoreGuiComposeMap.remove(compose.key)
        dragonCoreGuiComposeMap[compose.key] = compose
    }

    /**
     * 清理所有 Function
     */
    fun clearFunction() {
        functions.clear()
    }

    /**
     * 添加龙之核心定义的 Function
     */
    fun addFunction(dragonCoreGuiFunction: DragonCoreGuiFunctions, function: String) {
        functions.computeIfAbsent(dragonCoreGuiFunction) {
            mutableListOf()
        }.add(function)
    }

    /**
     * 添加龙之核心定义的 Function
     */
    fun addFunction(dragonCoreGuiFunction: DragonCoreGuiFunctions, function: List<String>) {
        functions.computeIfAbsent(dragonCoreGuiFunction) {
            mutableListOf()
        }.addAll(function)
    }

    /**
     * 清理所有 CustomFunction
     */
    fun clearCustomFunction() {
        customFunctions.clear()
    }

    /**
     * 添加自定义 Function
     */
    fun addCustomFunction(functionName: String, functionBody: String) {
        customFunctions.computeIfAbsent(functionName) {
            mutableListOf()
        }.add(functionBody)
    }

    /**
     * 添加自定义 Function
     */
    fun addCustomFunction(functionName: String, functionBody: List<String>) {
        customFunctions.computeIfAbsent(functionName) {
            mutableListOf()
        }.addAll(functionBody)
    }

    /**
     * 添加自定义 Function 回调函数
     */
    fun addFunctionCallBack(dragonCoreGuiFunction: DragonCoreGuiFunctions, callBack: BiConsumer<String, Player>) {
        dragonCoreGuiFunctionsCallBack[dragonCoreGuiFunction.functionName] = callBack
        functions.computeIfAbsent(dragonCoreGuiFunction) {
            mutableListOf("")
        }
    }

    fun handleDragonCoreGuiYaml(fullPath: String, dragonCoreGuiYaml: DragonCoreGuiYaml) {
        //自定义 function 放进界面
        for ((key, value) in customFunctions) {
            val fKey = "Functions.${key}"
            val stringBuilder = StringBuilder(dragonCoreGuiYaml.getString(fKey, ""))
            for (s in value) {
                if (s.isEmpty()) continue
                stringBuilder.append(s).append(";\n")
            }
            dragonCoreGuiYaml[fKey] = stringBuilder.toString()
        }
        //龙核自带 function 放进界面
        for ((key, value) in functions) {
            val fKey = "Functions.${key.functionName}"
            val stringBuilder = StringBuilder(dragonCoreGuiYaml.getString(fKey, ""))
            for (s in value) {
                if (s.isEmpty()) continue
                stringBuilder.append(s).append(";\n")
            }

            dragonCoreGuiYaml["Functions.${key.functionName}"] = "${stringBuilder}${key.callBackMethod};".replace(
                "%gui_full_path%", fullPath
            )
        }
        //将代码定义的 Compose 放进界面
        for ((key, value) in dragonCoreGuiComposeMap) {
            val convertToConfiguration = value.convertToConfiguration()
            for (convertKey in convertToConfiguration.getKeys(false)) {
                when (convertKey) {
                    "actions" -> {
                        val actionsConfig = convertToConfiguration.getConfigurationSection("actions") ?: continue
                        val defaultActions = dragonCoreGuiYaml.getConfigurationSection("$key.actions")

                        for (actionKey in actionsConfig.getKeys(false)) {
                            val default = if (defaultActions != null) {
                                defaultActions.getString(actionKey, "")
                            } else {
                                ""
                            }
                            val s = actionsConfig.getString(actionKey, "").replace(
                                "%gui_full_path%", fullPath
                            )
                            actionsConfig.set(actionKey, "$default$s")
                        }
                        dragonCoreGuiYaml["$key.actions"] = actionsConfig
                    }

                    else -> {
                        dragonCoreGuiYaml["$key.$convertKey"] = convertToConfiguration.get(convertKey)
                    }
                }
            }
        }
    }
}