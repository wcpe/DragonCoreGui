package top.wcpe.dragoncoregui.packet

/**
 * 由 WCPE 在 2024/1/14 17:28 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:28
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
abstract class ChildPacket @JvmOverloads constructor(
    parentPacket: ParentPacket,
    name: String,
    description: String,
    arguments: List<Argument> = listOf(),
    usageMessage: String = "",
    childCommandBuilder: ChildPacketBuilder = ChildPacketBuilder(
        parentPacket,
        name,
        description,
        arguments,
        usageMessage,
    ),
    val shouldDisplay: Boolean = false,
) : AbstractPacket(
    name = childCommandBuilder.name,
    description = childCommandBuilder.description,
    arguments = childCommandBuilder.arguments,
    usageMessage = childCommandBuilder.usageMessage,
)