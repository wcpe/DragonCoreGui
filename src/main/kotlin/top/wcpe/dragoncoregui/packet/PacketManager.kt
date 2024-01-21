package top.wcpe.dragoncoregui.packet

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.dragoncoregui.DragonCoreGui
import top.wcpe.dragoncoregui.packet.annotation.ChildPacket
import top.wcpe.dragoncoregui.packet.annotation.ParentPacket
import top.wcpe.dragoncoregui.packet.annotation.SinglePacket
import top.wcpe.dragoncoregui.packet.annotation.Argument
import top.wcpe.dragoncoregui.packet.extend.parentPacket
import top.wcpe.dragoncoregui.packet.extend.singlePacket
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

/**
 * 由 WCPE 在 2024/1/14 17:09 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:09
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
object PacketManager {

    private val logger = DragonCoreGui.instance.logger
    private val packetMap = mutableMapOf<String, Pair<AbstractPacket, JavaPlugin>>()
    private val pluginPacketMap = mutableMapOf<String, MutableMap<String, Pair<AbstractPacket, JavaPlugin>>>()

    @JvmStatic
    fun getPacketMap() = packetMap.toMap()

    @JvmStatic
    fun getPluginPacketMap(pluginName: String) = pluginPacketMap[pluginName]?.toMap() ?: mapOf()

    /**
     * 注册发包
     * 通过 实现 AbstractPacket 的实例 和 插件的主类实例来注册
     * @param abstractPacket 命令类
     * @param pluginInstance 插件主类实例
     * @return 返回注册是否成功
     */
    @JvmStatic
    fun registerPacket(abstractPacket: AbstractPacket, pluginInstance: Any): Boolean {
        if (pluginInstance !is JavaPlugin) {
            return false
        }
        val packetCommand = abstractPacket.name
        val packetPair = packetMap[packetCommand]

        if (packetPair != null) {
            val packet = packetPair.first
            val instance = packetPair.second
            logger.info("检测到插件: [${instance.name}] 已注册包: ${packet.name}")
            if (packetMap.remove(packetCommand) != null) {
                logger.info("注销包: ${packet.name} 成功!")
            } else {
                logger.info("注销包: ${packet.name} 失败! 您的包可能并不会生效")
            }
        }
        pluginPacketMap.computeIfAbsent(pluginInstance.name) {
            mutableMapOf()
        }[packetCommand] = abstractPacket to pluginInstance

        packetMap[packetCommand] = abstractPacket to pluginInstance
        return true
    }

    fun handleExecutePacket(packetIdentifier: String, player: Player, data: List<String>) {
        packetMap[packetIdentifier]?.first?.handleExecute(PlayerPacketSender(player), data)
    }

    fun handleExecutePacket(packetIdentifier: String, player: Player, data: Array<String?>) {
        packetMap[packetIdentifier]?.first?.handleExecute(PlayerPacketSender(player), data)
    }


    fun handleTabComplete(packetIdentifier: String, commandSender: CommandSender, args: Array<String>): List<String> {
        return packetMap[packetIdentifier]?.first?.handleTabComplete(PlayerPacketSender(commandSender), args)
            ?: emptyList()
    }


    private fun parseAnnotation(commandClass: KClass<*>): AbstractPacket? {
        return parseSinglePacket(commandClass) ?: parseParentPacket(commandClass)
    }

    private fun parseSinglePacket(commandClass: KClass<*>): AbstractPacket? {
        val singlePacketAnnotation = commandClass.findAnnotation<SinglePacket>() ?: return null

        val newInstance = commandClass.createInstance()

        return singlePacket(
            singlePacketAnnotation.name,
            singlePacketAnnotation.description,
            singlePacketAnnotation.arguments.map { top.wcpe.dragoncoregui.packet.Argument(it.name, it.required, it.description) }.toList(),
            singlePacketAnnotation.usageMessage,
            newInstance as? PacketExecutor,
            newInstance as? TabCompleter
        )
    }

    private fun parseParentPacket(commandClass: KClass<*>): AbstractPacket? {
        val parentPacketAnnotation = commandClass.findAnnotation<ParentPacket>() ?: return null

        val parentPacket = parentPacket(
            parentPacketAnnotation.name,
            parentPacketAnnotation.description,
            parentPacketAnnotation.usageMessage,
        )

        for (nestedClass in commandClass.nestedClasses) {
            parentPacket.addChildPacket(parseChildPacket(parentPacket, nestedClass) ?: continue)
        }

        return parentPacket
    }

    private fun parseChildPacket(
        parentInstance: top.wcpe.dragoncoregui.packet.ParentPacket, commandClass: KClass<*>,
    ): top.wcpe.dragoncoregui.packet.ChildPacket? {
        val childPacketAnnotation = commandClass.findAnnotation<ChildPacket>() ?: return null

        val newInstance = commandClass.createInstance()

        return parentInstance.childPacket(
            childPacketAnnotation.name,
            childPacketAnnotation.description,
            childPacketAnnotation.arguments.map { top.wcpe.dragoncoregui.packet.Argument(it.name, it.required, it.description) }.toList(),
            childPacketAnnotation.usageMessage,
            childPacketAnnotation.shouldDisplay,
            newInstance as? PacketExecutor,
            newInstance as? TabCompleter
        )
    }


    /**
     * 注册包
     * 通过含有包注解的类和插件主类实例来注册
     * @param commandClass 含 ParentPacket 注解的 javaClass 类
     * @param pluginInstance 插件主类实例
     * @return 返回注册是否成功
     */
    @JvmStatic
    fun registerPacket(commandClass: Class<*>, pluginInstance: Any): Boolean {
        return registerPacket(commandClass.kotlin, pluginInstance)
    }

    /**
     * 注册包
     * 通过含有包注解的类和插件主类实例来注册
     * @param commandClass 含 ParentPacket 注解的 KClass 类
     * @param pluginInstance 插件主类实例
     * @return 返回注册是否成功
     */
    @JvmStatic
    fun registerPacket(commandClass: KClass<*>, pluginInstance: Any): Boolean {
        val parseAnnotation = parseAnnotation(commandClass) ?: return false
        return registerPacket(parseAnnotation, pluginInstance)
    }
}