package top.wcpe.coregui.ui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.wcpe.coregui.gui.AbstractGui
import java.io.File
import java.util.function.Consumer

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

    fun getGuiDirFile(): File

    fun getGuiList(): List<String> {

        val guiDirFile = getGuiDirFile()
        return guiDirFile.walk().filter { it.isFile }.mapNotNull { file ->

            val filePath = file.path
            val fullPath = filePath.replace("${guiDirFile}${File.separatorChar}", "")

            // 查找最后一个点的位置，即扩展名的起始位置
            val lastDotIndex = fullPath.lastIndexOf('.')

            // 如果找到了点，并且点不在文件名的开头位置
            if (lastDotIndex == -1 || lastDotIndex <= fullPath.lastIndexOf(File.separatorChar)) {
                return@mapNotNull null
            }
            val fullPathWithoutExtension = fullPath.substring(0, lastDotIndex)
            return@mapNotNull fullPathWithoutExtension.replace("${File.separatorChar}", "/")
        }.toList()
    }

    fun getCoreName(): String

    fun moveConfig(fullPath: String, file: File) {
        val f = File(getGuiDirFile(), fullPath + file.name)
        file.copyTo(f, true)
    }

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
    fun sendPlaceholderMap(player: Player, data: Map<String, String>)


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

    fun registerKey(key: String)

    fun getCacheSlotItem(player: Player, identifier: String): ItemStack?

    fun consumerSlotItem(
        player: Player,
        identifier: String,
        success: Consumer<ItemStack?>,
        fail: Runnable,
    )

    fun setSlotItem(player: Player, identifier: String, itemStack: ItemStack?, syncToClient: Boolean)

}