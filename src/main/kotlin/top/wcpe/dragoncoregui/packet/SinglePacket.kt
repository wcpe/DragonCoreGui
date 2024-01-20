package top.wcpe.dragoncoregui.packet

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
abstract class SinglePacket @JvmOverloads constructor(
    name: String,
    description: String,
    arguments: List<Argument> = listOf(),
    usageMessage: String = "",
    singlePacketBuilder: SinglePacketBuilder = SinglePacketBuilder(
        name,
        description,
        arguments,
        usageMessage,
    ),
) : AbstractPacket(
    name = singlePacketBuilder.name,
    description = singlePacketBuilder.description,
    arguments = singlePacketBuilder.arguments,
    usageMessage = singlePacketBuilder.usageMessage
) {
    constructor(
        singlePacketBuilder: SinglePacketBuilder,
    ) : this(
        name = singlePacketBuilder.name,
        description = singlePacketBuilder.description,
        arguments = singlePacketBuilder.arguments,
        usageMessage = singlePacketBuilder.usageMessage
    )
}
