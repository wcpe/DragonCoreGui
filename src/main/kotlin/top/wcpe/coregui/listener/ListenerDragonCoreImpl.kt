package top.wcpe.coregui.listener

import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import top.wcpe.dragoncoregui.DragonCoreGui
import top.wcpe.dragoncoregui.compose.AbstractDragonCoreCompose
import top.wcpe.dragoncoregui.gui.DragonCoreGuiManager
import top.wcpe.dragoncoregui.packet.PacketManager
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * 由 WCPE 在 2024/4/9 10:56 创建
 * <p>
 * Created by WCPE on 2024/4/9 10:56
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v2.0.0-SNAPSHOT
 */
class ListenerDragonCoreImpl : DragonCoreGuiListener() {

    @EventHandler
    fun listenerCustomPacketEvent(e: CustomPacketEvent) {
        DragonCoreGui.debug { logger ->
            logger.info("[${e.player.name}] Received packet: ${e.identifier} -> ${e.data}")
        }
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
                PacketManager.handleExecutePacket(identifier, e.player, e.data)
            }
        }
    }
}