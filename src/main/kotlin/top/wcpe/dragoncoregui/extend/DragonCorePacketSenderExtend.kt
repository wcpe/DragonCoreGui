package top.wcpe.dragoncoregui.extend

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
    DragonCoreGui.coreManager.removePlaceholder(player, key, startWith)
}

/**
 * 移除变量
 */
fun removePlaceholder(vararg players: Player, key: String) {
    for (player in players) {
        DragonCoreGui.coreManager.removePlaceholder(player, key, false)
    }
}

/**
 * 发送自定义函数
 */
fun sendRunFunction(player: Player, fullPath: String, function: String, async: Boolean = false) {
    DragonCoreGui.coreManager.sendRunFunction(player, fullPath, function, async)
}

/**
 * 发送变量
 */
fun sendPlaceholder(player: Player, data: Map<String, String>) {
    DragonCoreGui.coreManager.sendPlaceholder(player, data)
}


/**
 * 发送变量
 */
fun sendPlaceholder(player: Player, syncGet: (player: Player, data: MutableMap<String, String>) -> Unit) {
    DragonCoreGui.coreManager.sendPlaceholder(player, syncGet)
}

/**
 * 同步发送变量
 */
fun sendSyncPlaceholder(player: Player, syncGet: (player: Player, data: MutableMap<String, String>) -> Unit) {
    DragonCoreGui.coreManager.sendSyncPlaceholder(player, syncGet)
}


/**
 * 发送变量
 */
fun sendPlaceholder(vararg players: Player, data: Map<String, String>) {
    for (player in players) {
        DragonCoreGui.coreManager.sendPlaceholder(player, data)
    }
}


/**
 * 发送异步变量
 */
fun sendAsyncPlaceholder(
    player: Player,
    asyncGet: (player: Player, data: MutableMap<String, String>) -> Unit,
) {
    DragonCoreGui.coreManager.sendAsyncPlaceholder(player, asyncGet)
}

/**
 * 发送异步变量
 */
@Deprecated(
    "Use sendAsyncPlaceholder(player, asyncGet)", ReplaceWith(
        "DragonCoreGui.coreManager.sendAsyncPlaceholder(player, asyncGet)",
        "top.wcpe.dragoncoregui.DragonCoreGui"
    )
)
fun sendAsyncPlaceholder(player: Player, asyncGet: (Player) -> Map<String, String>) {
    DragonCoreGui.coreManager.sendAsyncPlaceholder(player, asyncGet)
}

/**
 * 发送异步变量
 */
fun sendAsyncPlaceholder(vararg players: Player, asyncGet: (Player) -> Map<String, String>) {
    for (player in players) {
        DragonCoreGui.coreManager.sendAsyncPlaceholder(player, asyncGet)
    }
}

/**
 * 发送客户端格子物品
 */
fun putClientSlotItem(player: Player, slotIdentity: String, itemStack: ItemStack) {
    DragonCoreGui.coreManager.putClientSlotItem(player, slotIdentity, itemStack)
}


/**
 * 发送客户端格子物品
 */
fun putClientSlotItem(vararg players: Player, slotIdentity: String, itemStack: ItemStack) {
    for (player in players) {
        DragonCoreGui.coreManager.putClientSlotItem(player, slotIdentity, itemStack)
    }
}
