package top.wcpe.coregui.ui

import com.yuankong.easycore.api.EasyCoreAPI
import com.yuankong.easycore.api.ui.SlotAPI
import com.yuankong.easycore.api.ui.UiConfig
import com.yuankong.easycore.config.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import top.wcpe.coregui.gui.AbstractGui
import top.wcpe.dragoncoregui.DragonCoreGui
import java.io.File

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
 * @since  : v
 */
class CoreManagerEasyCoreImpl(private val easyCorePlugin: Plugin) : CoreManager {

    override fun moveConfig(abstractGui: AbstractGui) {
        val guiDirFile = File(easyCorePlugin.dataFolder, "gui")
        val f = File(guiDirFile, "${abstractGui.fullPath}.yml")
        abstractGui.yamlConfiguration.save(f)
    }

    override fun reloadCoreConfig() {
        Config.loadConfig()
    }

    /**
     * 移除变量
     */
    override fun removePlaceholder(player: Player, key: String, startWith: Boolean) {
        if (startWith) {
            EasyCoreAPI.removePlaceholderStartswith(player, key)
        } else {
            EasyCoreAPI.removePlaceholder(player, key)
        }
    }

    /**
     * 移除变量
     */
    override fun removePlaceholder(vararg players: Player, key: String) {
        for (player in players) {
            removePlaceholder(player, key, false)
        }
    }

    /**
     * 发送自定义函数
     */
    override fun sendRunFunction(player: Player, fullPath: String, function: String, async: Boolean) {
    }

    /**
     * 发送变量
     */
    override fun sendPlaceholder(player: Player, data: Map<String, String>) {
        EasyCoreAPI.sendPlaceholder(player, data)
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
        EasyCoreAPI.sendPlaceholder(player, data.toMap())
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
        EasyCoreAPI.sendPlaceholder(player, data.toMap())
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
            EasyCoreAPI.sendPlaceholder(player, data.toMap())
        }
    }

    /**
     * 发送异步变量
     */
    override fun sendAsyncPlaceholder(player: Player, asyncGet: (Player) -> Map<String, String>) {
        Bukkit.getScheduler().runTaskAsynchronously(DragonCoreGui.instance) {
            EasyCoreAPI.sendPlaceholder(player, asyncGet(player))
        }
    }

    /**
     * 发送客户端格子物品
     */
    override fun putClientSlotItem(player: Player, slotIdentity: String, itemStack: ItemStack) {
        SlotAPI.sendCacheItemStack(player, slotIdentity, itemStack)
    }


    override fun sendYaml(player: Player, abstractGui: AbstractGui) {
        val uiConfigFromYaml = UiConfig.getUiConfigFromYaml(abstractGui.fileName, abstractGui.yamlConfiguration)
        EasyCoreAPI.sendGui(player, uiConfigFromYaml)
    }

    override fun openHud(player: Player, fullPath: String) {
        EasyCoreAPI.openGui(player, fullPath)
    }

    override fun openGui(player: Player, fullPath: String) {
        EasyCoreAPI.openGui(player, fullPath)
    }

    override fun closeGui(player: Player, fullPath: String) {
        EasyCoreAPI.closeGui(player)
    }

}