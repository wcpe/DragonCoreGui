package top.wcpe.dragoncoregui

/**
 * 由 WCPE 在 2024/1/14 17:20 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:20
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
enum class Message(private val path: String) {
    OptionalFormat("optional-format"),
    RequiredFormat("required-format"),
    UsageMessageFormat("usage-message-format"),
    PacketHelpFormat("packet-help-format"),
    PacketArgsError("packet-args-error"),
    PacketHelpTop("packet-help-top"),
    PacketHelpBottom("packet-help-bottom"),
    ArgumentTip("argument-tip"),
    PacketNotExist("packet-not-exist");

    fun toLocalization(vararg vars: Pair<String, Any>): String {
        val messageConfig = DragonCoreGui.instance.messageConfig ?: return ""
        var message = messageConfig.getStringList(path).joinToString("\n").ifEmpty {
            messageConfig.getString(path)
        }
        for (pair in vars) {
            message = message.replace(pair.first, "${pair.second}")
        }
        return message
    }
}