package top.wcpe.coregui.listener

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import top.wcpe.dragoncoregui.compose.DragonCoreComposeAction
import top.wcpe.dragoncoregui.gui.DragonCoreGuiFunctions
import java.util.function.BiConsumer

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
abstract class DragonCoreGuiListener : Listener {

    protected val actionsMap = DragonCoreComposeAction.values().associateBy { it.actionName }
    protected val functionsMap = DragonCoreGuiFunctions.values().associateBy { it.functionName }
    protected val packetsMap = mutableMapOf<String, BiConsumer<Player, List<String>>>()

    @Deprecated("This function is deprecated. Use the PacketManager instead.")
    fun registerPacketHandler(packetIdentifier: String, consumer: BiConsumer<Player, List<String>>) {
        packetsMap[packetIdentifier] = consumer
    }
}