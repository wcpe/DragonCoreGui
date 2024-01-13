package top.wcpe.dragoncoregui

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.logging.Logger
import kotlin.properties.Delegates

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
        fun debug(consumer: Consumer<Logger>) {
            if (instance.configuration.debugEnable) {
                consumer.accept(instance.logger)
            }
        }
    }

    var configuration: Configuration = Configuration(config)
        private set

    fun reloadOtherConfig() {
        reloadConfig()
        configuration = Configuration(config)
    }

    override fun onLoad() {
        instance = this
        saveDefaultConfig()
        reloadOtherConfig()
    }

    private val dragonCoreGuiListener = DragonCoreGuiListener()
    fun registerPacketHandler(packetIdentifier: String, consumer: BiConsumer<Player, List<String>>) {
        dragonCoreGuiListener.registerPacketHandler(packetIdentifier, consumer)
    }

    override fun onEnable() {
        val command = getCommand("dragoncoregui")
        command.aliases = listOf("dcg")
        command.executor = this
        server.pluginManager.registerEvents(dragonCoreGuiListener, this)
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        when (args.size) {
            1 -> {
                when (args[0]) {
                    "reload" -> {
                        reloadOtherConfig()
                        sender.sendMessage("重载配置文件完成")
                        return true
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args)
    }
}