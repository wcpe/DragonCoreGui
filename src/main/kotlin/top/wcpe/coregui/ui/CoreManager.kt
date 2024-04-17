package top.wcpe.coregui.ui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.wcpe.coregui.gui.AbstractGui

/**
 * 由 WCPE 在 2024/4/5 16:18 创建
 * <p>
 * Created by WCPE on 2024/4/5 16:18
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v2.0.0-SNAPSHOT
 */
interface CoreManager {
    fun moveConfig(abstractGui: AbstractGui)

    fun reloadCoreConfig()

    /**
     * 移除变量
     */
    fun removePlaceholder(player: Player, key: String, startWith: Boolean = false)

    /**
     * 移除变量
     */
    fun removePlaceholder(vararg players: Player, key: String)

    /**
     * 发送自定义函数
     */
    fun sendRunFunction(player: Player, fullPath: String, function: String, async: Boolean = false)

    /**
     * 发送变量
     */
    fun sendPlaceholder(player: Player, data: Map<String, String>)


    /**
     * 发送变量
     */
    fun sendPlaceholder(player: Player, syncGet: (player: Player, data: MutableMap<String, String>) -> Unit)

    /**
     * 同步发送变量
     */
    fun sendSyncPlaceholder(player: Player, syncGet: (player: Player, data: MutableMap<String, String>) -> Unit)


    /**
     * 发送异步变量
     */
    fun sendAsyncPlaceholder(
        player: Player,
        asyncGet: (player: Player, data: MutableMap<String, String>) -> Unit,
    )

    /**
     * 发送异步变量
     */
    @Deprecated("Use sendAsyncPlaceholder(player, asyncGet)")
    fun sendAsyncPlaceholder(player: Player, asyncGet: (Player) -> Map<String, String>)


    /**
     * 发送客户端格子物品
     */
    fun putClientSlotItem(player: Player, slotIdentity: String, itemStack: ItemStack)

    fun sendYaml(player: Player, abstractGui: AbstractGui)


    fun openHud(player: Player, fullPath: String)

    fun openGui(player: Player, fullPath: String)

    fun closeGui(player: Player, fullPath: String)
}