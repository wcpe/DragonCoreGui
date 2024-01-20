package top.wcpe.dragoncoregui.packet

import top.wcpe.dragoncoregui.Message
import top.wcpe.dragoncoregui.packet.extend.singlePacket

/**
 * 由 WCPE 在 2024/1/14 17:31 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:31
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
class SinglePacketBuilder @JvmOverloads constructor(
    val name: String,
    val description: String = "",
    val arguments: List<Argument>,
    usageMessage: String = "",
    val packetExecutor: PacketExecutor? = null,
    val tabCompleter: TabCompleter? = null,
) {

    val usageMessage: String = usageMessage
        get() {
            return field.ifEmpty {
                Message.UsageMessageFormat.toLocalization(
                    "%packet_name%" to name, "%arguments%" to arguments.joinToString(" ") {
                        if (it.required) {
                            Message.RequiredFormat.toLocalization("%packet_name%" to it.name)
                        } else {
                            Message.OptionalFormat.toLocalization("%packet_name%" to it.name)
                        }
                    }, "%argument_tip%" to Message.ArgumentTip.toLocalization()
                )
            }
        }


    fun build(): SinglePacket {
        return singlePacket(this)
    }
}