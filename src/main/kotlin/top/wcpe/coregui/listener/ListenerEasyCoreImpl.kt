package top.wcpe.coregui.listener

import com.yuankong.easycore.api.event.CustomPacketEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import top.wcpe.dragoncoregui.DragonCoreGui
import top.wcpe.dragoncoregui.packet.PacketManager

/**
 * 由 WCPE 在 2024/4/9 10:58 创建
 * <p>
 * Created by WCPE on 2024/4/9 10:58
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v2.0.0-SNAPSHOT
 */
class ListenerEasyCoreImpl : DragonCoreGuiListener() {

    @EventHandler
    fun listenerCustomPacketEvent(e: CustomPacketEvent) {
        DragonCoreGui.debug { logger ->
            logger.info("[${e.player.name}] Received packet: ${e.identifier} -> ${e.data}")
        }
        val identifier = e.identifier
        packetsMap[identifier]?.accept(e.player, e.data)
        PacketManager.handleExecutePacket(identifier, e.player, e.data)
        Bukkit.getPluginManager().callEvent(top.wcpe.coregui.event.CustomPacketEvent(e.player, identifier, e.data))
    }

}