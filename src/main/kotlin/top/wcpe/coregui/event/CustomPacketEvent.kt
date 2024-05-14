package top.wcpe.coregui.event

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/**
 * 由 WCPE 在 2024/5/14 11:07 创建
 * <p>
 * Created by WCPE on 2024/5/14 11:07
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v2.0.0-SNAPSHOT
 */
data class CustomPacketEvent(
    private val player: Player,
    val identifier: String,
    val data: List<String>,
) : PlayerEvent(player), Cancellable {
    companion object {
        @JvmStatic
        val handlerList: HandlerList = HandlerList()
    }

    private var cancelled = false


    override fun getHandlers(): HandlerList {
        return handlerList
    }


    override fun isCancelled(): Boolean {
        return this.cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }


}
