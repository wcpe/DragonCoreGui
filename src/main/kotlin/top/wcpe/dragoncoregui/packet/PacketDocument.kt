package top.wcpe.dragoncoregui.packet

import top.wcpe.dragoncoregui.packet.annotation.Argument
import top.wcpe.dragoncoregui.packet.annotation.ChildPacket
import top.wcpe.dragoncoregui.packet.annotation.ParentPacket
import top.wcpe.dragoncoregui.packet.annotation.SinglePacket
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * 由 WCPE 在 2024/1/20 18:10 创建
 * <p>
 * Created by WCPE on 2024/1/20 18:10
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.4.0-SNAPSHOT
 */
object PacketDocument {

    private val startFormat = """
        | 发包 | 描述 | 
        | :--- | :--- |
    """.trimIndent()
    private val format = """
        | 方法.发包('%packet_name%'%parameters%) | %description% |
    """.trimIndent()
    private val parameterFormat = ", '%parameter%'".trimIndent()


    private fun getArgString(arguments: Array<Argument>): String {
        println(arguments)
        println(arguments.size)
        return arguments.joinToString("") {
            parameterFormat.replace(
                "%parameter%",
                it.description
            )
        }
    }

    private fun putPacket(
        stringBuilder: StringBuilder,
        arguments: Array<Argument>,
        mainPacketName: String,
        subPacketName: String,
        description: String,
    ) {
        val argString = getArgString(arguments)

        val packetName = parameterFormat.replace("%parameter%", subPacketName)
        val f = format.replace("%packet_name%", mainPacketName)
            .replace("%parameters%", "$packetName$argString")
            .replace("%description%", description)
        stringBuilder.append("\n")
        stringBuilder.append(f)
    }

    private fun putSinglePacket(commandClass: KClass<*>, stringBuilder: StringBuilder): Boolean {
        val singlePacketAnnotation = commandClass.findAnnotation<SinglePacket>() ?: return false
        putPacket(
            stringBuilder,
            singlePacketAnnotation.arguments,
            singlePacketAnnotation.name,
            singlePacketAnnotation.name,
            singlePacketAnnotation.description
        )
        return true
    }


    private fun getParentPacket(commandClass: KClass<*>, stringBuilder: StringBuilder): Boolean {
        val parentPacketAnnotation = commandClass.findAnnotation<ParentPacket>() ?: return false

        for (nestedClass in commandClass.nestedClasses) {
            val childPacketAnnotation = nestedClass.findAnnotation<ChildPacket>() ?: continue
            println(childPacketAnnotation)
            putPacket(
                stringBuilder,
                childPacketAnnotation.arguments,
                parentPacketAnnotation.name,
                childPacketAnnotation.name,
                childPacketAnnotation.description
            )
        }
        return true
    }

    @JvmStatic
    fun getDocumentString(commandClass: KClass<*>): String {
        val stringBuilder = StringBuilder(startFormat)
        if (!putSinglePacket(commandClass, stringBuilder)) {
            if (!getParentPacket(commandClass, stringBuilder)) {
                return ""
            }
        }
        return stringBuilder.toString()
    }
}