package top.wcpe.dragoncoregui.packet

import top.wcpe.dragoncoregui.Message
import top.wcpe.dragoncoregui.packet.extend.parentPacket

/**
 * 由 WCPE 在 2024/1/14 17:52 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:52
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
class ParentPacketBuilder @JvmOverloads constructor(
    val name: String,
    description: String = "",
    usageMessage: String = "",
) {

    val description: String = description
        get() {
            return field.ifEmpty {
                "$name Commands"
            }
        }
    val usageMessage: String = usageMessage
        get() {
            return field.ifEmpty {
                Message.UsageMessageFormat.toLocalization(
                    "%packet_name%" to name, "%arguments%" to "[help]", "%argument_tip%" to ""
                )
            }
        }

    fun build(): ParentPacket {
        return parentPacket(this)
    }
}