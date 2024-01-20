package top.wcpe.dragoncoregui.packet

/**
 * 由 WCPE 在 2024/1/14 17:08 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:08
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
fun interface PacketExecutor {
    fun execute(packetSender: PacketSender<*>, args: Array<String?>)
}