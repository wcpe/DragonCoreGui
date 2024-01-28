package top.wcpe.dragoncoregui.extend

import eos.moe.dragoncore.network.PacketSender
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.wcpe.dragoncoregui.DragonCoreGui

/**
 * 由 WCPE 在 2024/1/21 15:33 创建
 * <p>
 * Created by WCPE on 2024/1/21 15:33
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.4.0-SNAPSHOT
 */

/**
 * 移除变量
 */
fun removePlaceholder(player: Player, key: String, startWith: Boolean = false) {
    PacketSender.sendDeletePlaceholderCache(player, key, startWith)
}

/**
 * 移除变量
 */
fun removePlaceholder(vararg players: Player, key: String) {
    for (player in players) {
        PacketSender.sendDeletePlaceholderCache(player, key, false)
    }
}

/**
 * 发送自定义函数
 */
fun sendRunFunction(player: Player, fullPath: String, function: String, async: Boolean = false) {
    PacketSender.sendRunFunction(player, fullPath, function, async)
}

/**
 * 发送变量
 */
fun sendPlaceholder(player: Player, data: Map<String, String>) {
    PacketSender.sendSyncPlaceholder(player, data)
}


/**
 * 发送变量
 */
inline fun sendPlaceholder(player: Player, asyncGet: (player: Player, data: MutableMap<String, String>) -> Unit) {
    val data = mutableMapOf<String, String>()
    asyncGet.invoke(player, data)
    PacketSender.sendSyncPlaceholder(player, data)
}


/**
 * 发送变量
 */
fun sendPlaceholder(vararg players: Player, data: Map<String, String>) {
    for (player in players) {
        PacketSender.sendSyncPlaceholder(player, data)
    }
}


/**
 * 发送异步变量
 */
inline fun sendAsyncPlaceholder(
    player: Player,
    crossinline asyncGet: (player: Player, data: MutableMap<String, String>) -> Unit,
) {
    val data = mutableMapOf<String, String>()
    Bukkit.getScheduler().runTaskAsynchronously(DragonCoreGui.instance) {
        asyncGet.invoke(player, data)
        PacketSender.sendSyncPlaceholder(player, data)
    }
}

/**
 * 发送异步变量
 */
inline fun sendAsyncPlaceholder(player: Player, crossinline asyncGet: (Player) -> Map<String, String>) {
    Bukkit.getScheduler().runTaskAsynchronously(DragonCoreGui.instance) {
        PacketSender.sendSyncPlaceholder(player, asyncGet(player))
    }
}

/**
 * 发送异步变量
 */
inline fun sendAsyncPlaceholder(vararg players: Player, crossinline asyncGet: (Player) -> Map<String, String>) {
    for (player in players) {
        Bukkit.getScheduler().runTaskAsynchronously(DragonCoreGui.instance) {
            PacketSender.sendSyncPlaceholder(player, asyncGet(player))
        }
    }
}

/**
 * 发送客户端格子物品
 */
fun putClientSlotItem(player: Player, slotIdentity: String, itemStack: ItemStack) {
    PacketSender.putClientSlotItem(player, slotIdentity, itemStack)
}


/**
 * 发送客户端格子物品
 */
fun putClientSlotItem(vararg players: Player, slotIdentity: String, itemStack: ItemStack) {
    for (player in players) {
        PacketSender.putClientSlotItem(player, slotIdentity, itemStack)
    }
}
