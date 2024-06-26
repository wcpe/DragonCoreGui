package top.wcpe.coregui.ui

import eos.moe.dragoncore.DragonCore
import eos.moe.dragoncore.api.CoreAPI
import eos.moe.dragoncore.api.SlotAPI
import eos.moe.dragoncore.config.Config
import eos.moe.dragoncore.database.IDataBase.Callback
import eos.moe.dragoncore.network.PacketSender
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import top.wcpe.coregui.gui.AbstractGui
import top.wcpe.dragoncoregui.DragonCoreGui
import java.io.File
import java.util.function.Consumer

/**
 * 由 WCPE 在 2024/4/5 16:19 创建
 * <p>
 * Created by WCPE on 2024/4/5 16:19
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v2.0.0-SNAPSHOT
 */
class CoreManagerDragonCoreImpl(private val dragonCorePlugin: Plugin) : CoreManager {

    private val guiDirFile = File(dragonCorePlugin.dataFolder, "Gui")

    override fun getGuiDirFile(): File {
        return guiDirFile
    }

    override fun getCoreName(): String {
        return "DragonCore"
    }

    override fun moveConfig(abstractGui: AbstractGui) {
        val f = File(guiDirFile, "${abstractGui.fullPath}.yml")
        abstractGui.yamlConfiguration.save(f)
    }

    override fun reloadCoreConfig() {
        Config.init(DragonCore.getInstance())
        Config.sendYamlToClient()
    }


    /**
     * 移除变量
     */
    override fun removePlaceholder(player: Player, key: String, startWith: Boolean) {
        PacketSender.sendDeletePlaceholderCache(player, key, startWith)
    }

    /**
     * 移除变量
     */
    override fun removePlaceholder(vararg players: Player, key: String) {
        for (player in players) {
            PacketSender.sendDeletePlaceholderCache(player, key, false)
        }
    }

    /**
     * 发送自定义函数
     */
    override fun sendRunFunction(player: Player, fullPath: String, function: String, async: Boolean) {
        PacketSender.sendRunFunction(player, fullPath, function, async)
    }

    /**
     * 发送变量
     */
    override fun sendPlaceholder(player: Player, data: Map<String, String>) {
        PacketSender.sendSyncPlaceholder(player, data)
    }


    /**
     * 发送变量
     */
    override fun sendPlaceholder(
        player: Player,
        syncGet: (player: Player, data: MutableMap<String, String>) -> Unit,
    ) {
        val data = mutableMapOf<String, String>()
        syncGet.invoke(player, data)
        PacketSender.sendSyncPlaceholder(player, data)
    }

    override fun sendPlaceholderMap(player: Player, data: Map<String, String>) {
        PacketSender.sendSyncPlaceholder(player, data)
    }

    /**
     * 同步发送变量
     */
    override fun sendSyncPlaceholder(
        player: Player,
        syncGet: (player: Player, data: MutableMap<String, String>) -> Unit,
    ) {
        val data = mutableMapOf<String, String>()
        syncGet.invoke(player, data)
        PacketSender.sendSyncPlaceholder(player, data)
    }


    /**
     * 发送异步变量
     */
    override fun sendAsyncPlaceholder(
        player: Player,
        asyncGet: (player: Player, data: MutableMap<String, String>) -> Unit,
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
    override fun sendAsyncPlaceholder(player: Player, asyncGet: (Player) -> Map<String, String>) {
        Bukkit.getScheduler().runTaskAsynchronously(DragonCoreGui.instance) {
            PacketSender.sendSyncPlaceholder(player, asyncGet(player))
        }
    }

    /**
     * 发送客户端格子物品
     */
    override fun putClientSlotItem(player: Player, slotIdentity: String, itemStack: ItemStack) {
        PacketSender.putClientSlotItem(player, slotIdentity, itemStack)
    }

    override fun sendYaml(player: Player, abstractGui: AbstractGui) {
        PacketSender.sendYaml(player, "Gui/${abstractGui.fullPath}", abstractGui.yamlConfiguration)
    }

    override fun openHud(player: Player, fullPath: String) {
        PacketSender.sendOpenHud(player, fullPath)
    }

    override fun openGui(player: Player, fullPath: String) {
        PacketSender.sendOpenGui(player, fullPath)
    }

    override fun closeGui(player: Player, fullPath: String) {
        PacketSender.sendRunFunction(player, fullPath, "方法.关闭界面", false)
    }

    override fun registerKey(key: String) {
        CoreAPI.registerKey("")
    }

    override fun getCacheSlotItem(player: Player, identifier: String): ItemStack? {
        return SlotAPI.getCacheSlotItem(player, identifier)
    }

    companion object {
        @JvmStatic
        private val emptyItemStack = ItemStack(Material.AIR)
    }

    override fun consumerSlotItem(
        player: Player,
        identifier: String,
        success: Consumer<ItemStack?>,
        fail: Runnable,
    ) {
        SlotAPI.getSlotItem(player, identifier, object : Callback<ItemStack> {
            override fun onResult(itemStack: ItemStack?) {
                success.accept(itemStack)
            }

            override fun onFail() {
                fail.run()
            }

        })
    }

    override fun setSlotItem(player: Player, identifier: String, itemStack: ItemStack?, syncToClient: Boolean) {
        SlotAPI.setSlotItem(player, identifier, itemStack ?: emptyItemStack, syncToClient)
    }


}