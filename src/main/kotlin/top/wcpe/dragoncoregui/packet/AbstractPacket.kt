package top.wcpe.dragoncoregui.packet

import top.wcpe.dragoncoregui.Message
import kotlin.math.max

/**
 * 由 WCPE 在 2024/1/14 17:13 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:13
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
abstract class AbstractPacket(
    val name: String,
    val description: String,
    val arguments: List<Argument>,
    val usageMessage: String,
) {

    var packetExecutor: PacketExecutor? = null
    var tabCompleter: TabCompleter? = null

    infix fun String.to(required: Boolean): Argument = Argument(this, required)

    fun register(instance: Any): Boolean {
        if (this is ChildPacket) {
            return false
        }
        return PacketManager.registerPacket(this, instance)
    }

    private val startFormat = """
        ## %name% 发包
        
        | 发包 | 描述 | 
        | :--- | :--- |
    """.trimIndent()

    private val markDownDocStringBuilder = StringBuilder(startFormat.replace("%name%", name))

    private val format = """
        | 方法.发包('%packet_name%'%parameters%) | %description% |
    """.trimIndent()

    private val parameterFormat = ", '%parameter%'".trimIndent()

    fun getArgumentsString(): String {
        return arguments.joinToString("") {
            parameterFormat.replace(
                "%parameter%",
                it.description
            )
        }
    }

    fun putPacketToDoc(
        mainPacketName: String,
        subPacketName: String,
        argumentsString: String,
        description: String,
    ) {
        val f = format.replace("%packet_name%", mainPacketName)
            .replace(
                "%parameters%", "${
                    if (subPacketName.isEmpty()) {
                        ""
                    } else {
                        parameterFormat.replace("%parameter%", subPacketName)
                    }
                }$argumentsString"
            )
            .replace("%description%", description)
        markDownDocStringBuilder.append("\n")
        markDownDocStringBuilder.append(f)
    }

    fun toMarkDownDoc(): String {
        return markDownDocStringBuilder.toString()
    }

    private fun requiredArgs(
        argsStrings: Array<String?>, arguments: List<Argument>,
    ): List<Argument> {
        val result = mutableListOf<Argument>()
        for ((i, value) in arguments.withIndex()) {
            if (value.required && argsStrings[i] == null) {
                result.add(value)
            }
        }
        return result
    }

    private fun notRequiredArgsReplace(argsStrings: Array<String?>, arguments: List<Argument>) {
        for ((i, argument) in arguments.withIndex()) {
            if (argument.required) {
                continue
            }
            val arg = argsStrings[i]
            if (arg == null || arg == " " || arg == ".") {
                argsStrings[i] = null
            }
        }
    }

    fun handleExecute(packetSender: PacketSender<*>, argsList: List<String>) {
        val argsStrings = Array(max(argsList.size, arguments.size)) { index ->
            if (index < argsList.size) argsList[index]
            else null
        }
        handleExecute0(packetSender, argsStrings)
    }

    fun handleExecute(packetSender: PacketSender<*>, args: Array<String?>) {
        val argsStrings = args.copyOf(max(args.size, arguments.size))
        handleExecute0(packetSender, argsStrings)
    }

    private fun handleExecute0(packetSender: PacketSender<*>, argsStrings: Array<String?>) {
        if (beforeExecute(packetSender, argsStrings)) {
            notRequiredArgsReplace(argsStrings, arguments)
            if (this is PacketExecutor) {
                execute(packetSender, argsStrings)
            } else {
                packetExecutor?.execute(packetSender, argsStrings)
            }
        }
    }

    fun handleTabComplete(packetSender: PacketSender<*>, args: Array<String>): List<String> {
        return if (this is TabCompleter) {
            tabComplete(packetSender, args)
        } else {
            tabCompleter?.tabComplete(packetSender, args) ?: emptyList()
        }
    }

    /**
     *
     * 执行 execute 之前执行
     *
     * @param packetSender 执行的对象
     * @param args 参数
     * @return true 检查通过
     * @return false 检查不通过
     */
    open fun beforeExecute(packetSender: PacketSender<*>, args: Array<String?>): Boolean {
        if (arguments.isEmpty()) {
            return true
        }
        val requiredArgResult = requiredArgs(args, arguments)
        if (requiredArgResult.isNotEmpty()) {
            packetSender.sendMessage(usageMessage)
            packetSender.sendMessage(
                Message.PacketArgsError.toLocalization("%arguments%" to requiredArgResult.joinToString(
                    " "
                ) {
                    Message.RequiredFormat.toLocalization("%packet_name%" to it.name)
                })
            )
            return false
        }
        return true
    }


}
