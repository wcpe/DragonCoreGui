package top.wcpe.dragoncoregui

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import top.wcpe.dragoncoregui.compose.DragonCoreComposeAction
import top.wcpe.dragoncoregui.gui.AbstractDragonCoreGui
import top.wcpe.dragoncoregui.gui.DragonCoreGuiFunctions

/**
 * 由 WCPE 在 2022/7/19 1:14 创建
 *
 * Created by WCPE on 2022/7/19 1:14
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class DragonCoreGuiListener : Listener {

    private val actionsMap = DragonCoreComposeAction.values().associateBy { it.actionName }
    private val functionsMap = DragonCoreGuiFunctions.values().associateBy { it.functionName }

    @EventHandler
    fun listenerCustomPacketEvent(e: CustomPacketEvent) {
        when (e.identifier) {
            "DragonCoreGui_actions" -> {
                if (e.data.size < 3) {
                    return
                }
                val data = e.data
                val dragonCoreGuiFullPath = data[0]
                val action = data[1]
                if (!actionsMap.containsKey(action)) {
                    return
                }
                val composeKey = data[2]

                (((AbstractDragonCoreGui.dragonCoreGuiComposeMap[dragonCoreGuiFullPath] ?: return)[composeKey]
                    ?: return).actionCallBackMap[action] ?: return).accept(
                    data.subList(3, data.size),
                    e.player
                )
            }

            "DragonCoreGui_Functions" -> {
                if (e.data.size != 2) {
                    return
                }
                val dragonCoreGuiFullPath = e.data[0]
                val function = e.data[1]
                if (!functionsMap.containsKey(function)) {
                    return
                }
                ((AbstractDragonCoreGui.dragonCoreGuiFunctionsCallBack[dragonCoreGuiFullPath]
                    ?: return)[function]
                    ?: return).accept(
                    dragonCoreGuiFullPath,
                    e.player
                )
            }
        }
    }
}