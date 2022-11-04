package top.wcpe.dragoncoregui

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

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
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        getCommand("dragoncoregui").executor = this
        server.pluginManager.registerEvents(DragonCoreGuiListener(), this)
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
                        reloadConfig()
                        sender.sendMessage("重载配置文件完成")
                        return true
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args)
    }
}