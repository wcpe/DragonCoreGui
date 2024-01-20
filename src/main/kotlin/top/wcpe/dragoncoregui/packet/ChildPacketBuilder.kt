package top.wcpe.dragoncoregui.packet

import top.wcpe.dragoncoregui.Message
import top.wcpe.dragoncoregui.packet.extend.childPacket

/**
 * 由 WCPE 在 2024/1/14 17:44 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:44
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
class ChildPacketBuilder @JvmOverloads constructor(
    private val parentPacket: ParentPacket,
    val name: String,
    description: String = "",
    val arguments: List<Argument> = listOf(),
    usageMessage: String = "",
    val packetExecutor: PacketExecutor? = null,
    val tabCompleter: TabCompleter? = null,
) {

    val description: String = description
        get() {
            return field.ifEmpty {
                "$name Packets"
            }
        }


    val usageMessage: String = usageMessage
        get() {
            return field.ifEmpty {
                Message.UsageMessageFormat.toLocalization(
                    "%packet_name%" to parentPacket.name,
                    "%arguments%" to arguments.joinToString(" ", prefix = "$name ") {
                        if (it.required) {
                            Message.RequiredFormat.toLocalization("%packet_name%" to it.name)
                        } else {
                            Message.OptionalFormat.toLocalization("%packet_name%" to it.name)
                        }
                    },
                    "%argument_tip%" to Message.ArgumentTip.toLocalization()
                )
            }
        }


    fun build(): ChildPacket {
        return parentPacket.childPacket(this)
    }

}