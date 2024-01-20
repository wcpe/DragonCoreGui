package top.wcpe.dragoncoregui.packet

/**
 * 由 WCPE 在 2024/1/14 17:11 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:11
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
interface PacketSender<T> {
    fun getPacketSender(): T

    /**
     * 获取名称
     */
    fun getName(): String

    /**
     * 发送消息
     */
    fun sendMessage(message: String)


    /**
     * 是否为管理员
     */
    fun isOp(): Boolean
}