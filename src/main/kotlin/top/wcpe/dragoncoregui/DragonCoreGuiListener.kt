package top.wcpe.dragoncoregui

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import top.wcpe.dragoncoregui.compose.AbstractDragonCoreCompose
import top.wcpe.dragoncoregui.compose.DragonCoreComposeAction
import top.wcpe.dragoncoregui.gui.DragonCoreGuiFunctions
import top.wcpe.dragoncoregui.gui.DragonCoreGuiManager
import java.util.function.BiConsumer
import java.util.function.Consumer

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
    private val packetsMap = mutableMapOf<String, BiConsumer<Player, List<String>>>()

    fun registerPacketHandler(packetIdentifier: String, consumer: BiConsumer<Player, List<String>>) {
        packetsMap[packetIdentifier] = consumer
    }

    @EventHandler
    fun listenerCustomPacketEvent(e: CustomPacketEvent) {
        when (val identifier = e.identifier) {
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
                val player = e.player
                DragonCoreGuiManager.consumerDragonCoreGui(dragonCoreGuiFullPath) { dragonCoreGui ->
                    val consumer = Consumer<AbstractDragonCoreCompose> { compose ->
                        compose.consumerActionCallBack(action) { action ->
                            action.accept(
                                data.subList(3, data.size), player
                            )
                        }
                    }
                    dragonCoreGui.consumerCompose(composeKey, consumer)
                    dragonCoreGui.consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
                        dragonCoreGuiExpandConfig.consumerCompose(composeKey, consumer)
                    }
                }
            }

            "DragonCoreGui_Functions" -> {
                if (e.data.size != 2) {
                    return
                }
                val dragonCoreGuiFullPath = e.data[0]
                val functionKey = e.data[1]
                if (!functionsMap.containsKey(functionKey)) {
                    return
                }
                val player = e.player
                DragonCoreGuiManager.consumerDragonCoreGui(dragonCoreGuiFullPath) { dragonCoreGui ->
                    val consumer = Consumer<BiConsumer<String, Player>> { function ->
                        function.accept(dragonCoreGuiFullPath, player)
                    }

                    dragonCoreGui.consumerFunctionsCallBack(functionKey, consumer)
                    dragonCoreGui.consumerPlayerDragonCoreGuiExpandConfig(player) { dragonCoreGuiExpandConfig ->
                        dragonCoreGuiExpandConfig.consumerFunctionsCallBack(functionKey, consumer)
                    }
                }
            }

            else -> {
                packetsMap[identifier]?.accept(e.player, e.data)
            }
        }
    }
}