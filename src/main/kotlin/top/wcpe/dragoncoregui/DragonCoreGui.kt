package top.wcpe.dragoncoregui

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.coregui.listener.DragonCoreGuiListener
import top.wcpe.coregui.listener.ListenerDragonCoreImpl
import top.wcpe.coregui.listener.ListenerEasyCoreImpl
import top.wcpe.coregui.ui.CoreManager
import top.wcpe.coregui.ui.CoreManagerDragonCoreImpl
import top.wcpe.coregui.ui.CoreManagerEasyCoreImpl
import top.wcpe.dragoncoregui.packet.PacketManager
import top.wcpe.dragoncoregui.placeholder.PlaceholderManager
import java.io.File
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.logging.Logger

/**
 * 由 WCPE 在 2022/7/19 1:00 创建
 *
 * Created by WCPE on 2022/7/19 1:00
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class DragonCoreGui : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var instance: DragonCoreGui
            private set

        @JvmStatic
        lateinit var coreManager: CoreManager
            private set

        @JvmStatic
        fun debug(consumer: Consumer<Logger>) {
            val configuration = instance.configuration ?: return
            if (configuration.debugEnable) {
                consumer.accept(instance.logger)
            }
        }
    }

    var configuration: Configuration? = null
        private set

    private val messageFile = File(dataFolder, "message.yml")
    var messageConfig: FileConfiguration? = null
        private set

    override fun saveDefaultConfig() {
        super.saveDefaultConfig()
        if (!messageFile.exists()) {
            saveResource("message.yml", false)
        }
    }

    fun reloadOtherConfig() {
        reloadConfig()
        configuration = Configuration(config)
        messageConfig = YamlConfiguration.loadConfiguration(messageFile)
    }

    override fun onLoad() {
        instance = this
        saveDefaultConfig()
        reloadOtherConfig()
        val dragonCorePlugin = server.pluginManager.getPlugin("DragonCore")
        val easyCorePlugin = server.pluginManager.getPlugin("EasyCore")
        coreManager = if (dragonCorePlugin != null) {
            CoreManagerDragonCoreImpl(dragonCorePlugin)
        } else if (easyCorePlugin != null) {
            CoreManagerEasyCoreImpl(easyCorePlugin)
        } else {
            logger.info("DragonCore 或 EasyCore 未加载 关闭服务器!")
            server.shutdown()
            return
        }
        logger.info("coreManager load $coreManager ....")

    }

    private lateinit var dragonCoreGuiListener: DragonCoreGuiListener

    @Deprecated("This function is deprecated. Use the PacketManager instead.")
    fun registerPacketHandler(packetIdentifier: String, consumer: BiConsumer<Player, List<String>>) {
        dragonCoreGuiListener.registerPacketHandler(packetIdentifier, consumer)
    }

    override fun onEnable() {

        val command = getCommand("dragoncoregui")
        command.aliases = listOf("dcg", "coregui")
        command.executor = this
        command.tabCompleter = this

        dragonCoreGuiListener = if (server.pluginManager.getPlugin("DragonCore") != null) {
            ListenerDragonCoreImpl()
        } else if (server.pluginManager.getPlugin("EasyCore") != null) {
            ListenerEasyCoreImpl()
        } else {
            logger.info("DragonCore 或 EasyCore 未加载 关闭服务器!")
            server.shutdown()
            return
        }
        server.pluginManager.registerEvents(dragonCoreGuiListener, this)

        logger.info("debug -> ${configuration?.debugEnable}")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>,
    ): Boolean {
        execute(sender, args)
        return false
    }

    private fun sendHelper(commandSender: CommandSender) {
        commandSender.sendMessage("/coregui reload 重载插件")
        commandSender.sendMessage("/coregui packets <发包 identifier> <参数...> 测试发包指令")
        commandSender.sendMessage("/coregui packetToPlayer <玩家名称> <发包 identifier> <参数...> 测试发包指令")
        commandSender.sendMessage("/coregui document [packet/placeholder] <插件名称> 生成文档")
    }


    private fun execute(
        sender: CommandSender,
        args: Array<String>,
    ) {
        if (args.isEmpty()) {
            sendHelper(sender)
            return
        }
        val size = args.size

        val arg1 = args[0]
        if (arg1 == "reload") {
            reloadOtherConfig()
            sender.sendMessage("重载配置文件完成")
            return
        }
        if (size > 1) {
            val arg2 = args[1]
            if (arg1 == "packets") {
                if (sender !is Player) {
                    sender.sendMessage("该指令只能玩家使用!")
                    return
                }
                handlerPacket(arg2, sender, args, 2)
                return
            }
            if (size > 2) {
                val arg3 = args[2]
                if (arg1 == "packetToPlayer") {
                    val playerExact = Bukkit.getPlayerExact(arg2)
                    if (playerExact == null || !playerExact.isOnline) {
                        sender.sendMessage("玩家 $arg2 不存在或不在线!")
                        return
                    }
                    handlerPacket(arg3, playerExact, args, 3)
                    return
                }
                if (arg1 == "document") {
                    if (arg2 == "packet") {
                        val pluginPacketMap = PacketManager.getPluginPacketMap(arg3)
                        if (pluginPacketMap.isEmpty()) {
                            sender.sendMessage("插件: $arg3 未注册发包!")
                            return
                        }

                        val resolve = dataFolder.resolve("${arg3}_Packets.md")
                        if (!resolve.exists()) {
                            resolve.createNewFile()
                        }
                        val stringBuilder = StringBuilder()
                        for ((_, value) in pluginPacketMap) {
                            val toMarkDownDoc = value.first.toMarkDownDoc()
                            stringBuilder.append(toMarkDownDoc)
                            stringBuilder.append("\n")
                            stringBuilder.append("\n")
                        }

                        resolve.writeText(stringBuilder.toString())
                        sender.sendMessage("生成 $arg3 PacketDocument 成功! 路径: $resolve")
                        return
                    }
                    if (arg2 == "placeholder") {
                        val placeholderManagerMap = PlaceholderManager.getPlaceholderManagerMap(arg3)
                        if (placeholderManagerMap.isEmpty()) {
                            sender.sendMessage("插件: $arg3 未注册变量!")
                            return
                        }


                        val resolve = dataFolder.resolve("${arg3}_Placeholders.md")
                        if (!resolve.exists()) {
                            resolve.createNewFile()
                        }
                        val stringBuilder = StringBuilder()
                        for ((_, value) in placeholderManagerMap) {
                            val toMarkDownDoc = value.toMarkDownDoc()
                            stringBuilder.append(toMarkDownDoc)
                            stringBuilder.append("\n")
                            stringBuilder.append("\n")
                        }

                        resolve.writeText(stringBuilder.toString())
                        sender.sendMessage("生成 $arg3 PlaceholderDocument 成功! 路径: $resolve")
                        return
                    }
                }
            }
        }
        sendHelper(sender)
        return
    }

    private fun handlerPacket(packetIdentifier: String, player: Player, args: Array<String>, startIndex: Int) {
        val newSize = args.size - startIndex
        val argsStrings = Array(newSize) { index ->
            if (index < args.size) {
                args[startIndex + index]
            } else {
                null
            }
        }
        PacketManager.handleExecutePacket(packetIdentifier, player, argsStrings)
    }

    private val baseTabList = listOf("reload", "packets", "packetToPlayer", "document")

    private val documentTabList = listOf("packet", "placeholder")

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>,
    ): List<String> {
        if (args.isEmpty()) {
            return emptyList()
        }
        val size = args.size

        if (size == 1) {
            return baseTabList.filter {
                it.lowercase().startsWith(
                    args[0].lowercase()
                )
            }
        }
        if (size > 1) {
            val arg1 = args[0]
            val arg2 = args[1]
            if (arg1 == "packets") {
                return if (size == 2) {
                    PacketManager.getPacketMap().keys.filter {
                        it.lowercase().startsWith(arg2.lowercase())
                    }
                } else {
                    handlerTabComplete(args[1], sender, args, 2)
                }
            }
            if (arg1 == "document") {
                if (size == 2) {
                    return documentTabList.filter { it.startsWith(arg2) }
                } else if (size == 3) {
                    val arg3 = args[2]
                    if (arg2 == "packet") {
                        return PacketManager.getPluginPacketMap()
                            .filter { it.key.lowercase().startsWith(arg3.lowercase()) }.map { it.key }
                    } else if (arg2 == "placeholder") {
                        return PlaceholderManager.getPlaceholderManagerMap()
                            .filter { it.key.lowercase().startsWith(arg3.lowercase()) }.map { it.key }
                    }
                }
            }

            if (arg1 == "packetToPlayer") {
                return if (size == 2) {
                    if (arg2.isNotEmpty()) {
                        Bukkit.getOnlinePlayers().filter { it.name.startsWith(arg2) }.map { it.name }
                    } else {
                        Bukkit.getOnlinePlayers().map { it.name }
                    }
                } else if (size == 3) {
                    PacketManager.getPacketMap().keys.filter {
                        it.startsWith(args[2])
                    }
                } else {
                    handlerTabComplete(args[2], sender, args, 3)
                }
            }

        }

        return emptyList()
    }

    private fun handlerTabComplete(
        packetIdentifier: String,
        commandSender: CommandSender,
        args: Array<String>,
        startIndex: Int,
    ): List<String> {
        val newSize = args.size - startIndex
        val argsStrings = Array(newSize) { index ->
            if (index < args.size) {
                args[startIndex + index]
            } else {
                ""
            }
        }
        return PacketManager.handleTabComplete(packetIdentifier, commandSender, argsStrings)
    }
}