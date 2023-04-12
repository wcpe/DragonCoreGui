package top.wcpe.dragoncoregui.compose

import org.bukkit.configuration.MemoryConfiguration
import org.bukkit.entity.Player
import top.wcpe.dragoncoregui.yaml.DragonCoreGuiYaml
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * 由 WCPE 在 2022/7/18 22:24 创建
 *
 * Created by WCPE on 2022/7/18 22:24
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
abstract class AbstractDragonCoreCompose(
    open val key: String,
    open val type: String,
    open var extends: String = "",
    open var x: String = "",
    open var y: String = "",
    open var width: String = "",
    open var height: String = "",
    open var limitX: String = "",
    open var limitY: String = "",
    open var limitWidth: String = "",
    open var limitHeight: String = "",
    open var maxDistanceX: String = "",
    open var maxDistanceY: String = "",
    open var minDistanceX: String = "",
    open var minDistanceY: String = "",
    open var distanceX: String = "",
    open var distanceY: String = "",
    open var visible: String = "",
    open var enable: String = "",
    open var alpha: String = "",
    open var scale: String = "",
    open var tip: String = "",
) {
    private val actionsMap: MutableMap<DragonCoreComposeAction, MutableList<String>> = mutableMapOf()

    private val actionCallBackMap: MutableMap<String, BiConsumer<List<String>, Player>> = mutableMapOf()
    fun consumerActionCallBack(actionKey: String, consumer: Consumer<BiConsumer<List<String>, Player>>) {
        consumer.accept(actionCallBackMap[actionKey] ?: return)
    }

    private val actionCallBackGetMethodMap: MutableMap<String, List<String>> = mutableMapOf()

    private val customKeyValueMap: MutableMap<String, String> = mutableMapOf()

    /**
     * 添加自定义 属性值
     *
     * @param key key
     * @param value value
     */
    open fun addCustomKeyValue(key: String, value: String) {
        customKeyValueMap[key] = value
    }

    /**
     * 添加一个 action
     *
     * @param dragonCoreAction 龙核组件 action 枚举
     * @param action 龙核中的方法
     */
    open fun addAction(dragonCoreAction: DragonCoreComposeAction, action: String) {
        actionsMap.computeIfAbsent(dragonCoreAction) {
            mutableListOf()
        }.add(action)
    }

    /**
     * 添加一个 action 回调函数 通过 CustomPacketEvent 封装而来
     *
     * @param dragonCoreAction 龙核组件 action 枚举
     * @param getValueMethod 龙核中获取值的方法 可在回调函数中返回获取到的值
     * @param callBack 回调函数 数据 to 玩家
     */
    open fun addActionCallBack(
        dragonCoreAction: DragonCoreComposeAction,
        getValueMethod: List<String> = listOf(),
        callBack: BiConsumer<List<String>, Player>
    ) {
        actionCallBackMap[dragonCoreAction.actionName] = callBack
        actionCallBackGetMethodMap[dragonCoreAction.actionName] = getValueMethod
        actionsMap.computeIfAbsent(dragonCoreAction) {
            mutableListOf("")
        }
    }


    open fun convertToConfiguration(): MemoryConfiguration {
        return DragonCoreGuiYaml().also { yaml ->
            for ((key, value) in customKeyValueMap) {
                yaml[key] = value
            }
            yaml["type"] = type
            yaml["extends"] = extends
            yaml["x"] = x
            yaml["y"] = y
            yaml["width"] = width
            yaml["height"] = height
            yaml["limitX"] = limitX
            yaml["limitY"] = limitY
            yaml["limitWidth"] = limitWidth
            yaml["limitHeight"] = limitHeight
            yaml["maxDistanceX"] = maxDistanceX
            yaml["maxDistanceY"] = maxDistanceY
            yaml["minDistanceX"] = minDistanceX
            yaml["minDistanceY"] = minDistanceY
            yaml["distanceX"] = distanceX
            yaml["distanceY"] = distanceY
            yaml["visible"] = visible
            yaml["enable"] = enable
            yaml["alpha"] = alpha
            yaml["scale"] = scale
            yaml["tip"] = tip


            for ((key, value) in actionsMap) {
                yaml["actions.${key.actionName}"] = "${
                    if (key.enableSendPacker) {
                        "${
                            key.callBackMethod.replace("%get_value_method%",
                                actionCallBackGetMethodMap[key.actionName].let { getValueMethodList ->
                                    getValueMethodList ?: return@let ""
                                    val f = getValueMethodList.filter { it.isNotEmpty() }
                                    if (f.isEmpty()) {
                                        return@let ""
                                    }
                                    f.joinToString(prefix = ", ", separator = ", ")
                                })
                        };"
                    } else {
                        ""
                    }
                }${
                    StringBuilder(yaml.getString("actions.${key.actionName}", "")).also { sb ->
                        for (s in value) {
                            if (s.isEmpty()) continue
                            sb.append(s).append(";\n")
                        }
                    }
                }".replace(
                    "%compose_key%", this.key
                )
            }
        }
    }
}