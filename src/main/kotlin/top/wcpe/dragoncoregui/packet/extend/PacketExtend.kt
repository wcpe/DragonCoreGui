package top.wcpe.dragoncoregui.packet.extend

import top.wcpe.dragoncoregui.packet.*

/**
 * 由 WCPE 在 2024/1/14 17:37 创建
 * <p>
 * Created by WCPE on 2024/1/14 17:37
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
inline fun singlePacket(
    name: String,
    description: String = "",
    arguments: List<Argument> = listOf(),
    usageMessage: String = "",
    packetExecutor: PacketExecutor? = null,
    tabCompleter: TabCompleter? = null,
    crossinline runnable: SinglePacket.() -> Unit = {},
): SinglePacket {
    return object : SinglePacket(
        name,
        description,
        arguments,
        usageMessage,
    ) {
        init {
            this.packetExecutor = packetExecutor
            this.tabCompleter = tabCompleter
        }
    }.also(runnable)
}

inline fun singlePacket(
    singlePacketBuilder: SinglePacketBuilder, crossinline runnable: SinglePacket.() -> Unit = {},
): SinglePacket {
    return singlePacket(
        name = singlePacketBuilder.name,
        description = singlePacketBuilder.description,
        arguments = singlePacketBuilder.arguments,
        usageMessage = singlePacketBuilder.usageMessage,
        packetExecutor = singlePacketBuilder.packetExecutor,
        tabCompleter = singlePacketBuilder.tabCompleter,
        runnable = runnable
    )
}


inline fun parentPacket(
    name: String,
    description: String = "",
    usageMessage: String = "",
    crossinline runnable: ParentPacket.() -> Unit = {},
): ParentPacket {
    return object : ParentPacket(
        name,
        description,
        usageMessage,
    ) {}.also(runnable)
}

inline fun parentPacket(
    parentPacketBuilder: ParentPacketBuilder, crossinline runnable: ParentPacket.() -> Unit = {},
): ParentPacket {
    return parentPacket(
        name = parentPacketBuilder.name,
        description = parentPacketBuilder.description,
        usageMessage = parentPacketBuilder.usageMessage,
        runnable = runnable
    )
}

inline fun ParentPacket.childPacket(
    name: String,
    description: String = "",
    arguments: List<Argument> = listOf(),
    usageMessage: String = "",
    shouldDisplay: Boolean = false,
    packetExecutor: PacketExecutor? = null,
    tabCompleter: TabCompleter? = null,
    crossinline runnable: ChildPacket.() -> Unit = {},
): ChildPacket {
    return childPacket(
        name,
        description,
        arguments,
        usageMessage,
        shouldDisplay,
        packetExecutor,
        tabCompleter
    ).also(runnable)
}

inline fun ParentPacket.childPacket(
    childPacketBuilder: ChildPacketBuilder, crossinline runnable: ChildPacket.() -> Unit = {},
): ChildPacket {
    return childPacket(
        name = childPacketBuilder.name,
        description = childPacketBuilder.description,
        arguments = childPacketBuilder.arguments,
        usageMessage = childPacketBuilder.usageMessage,
        packetExecutor = childPacketBuilder.packetExecutor,
        tabCompleter = childPacketBuilder.tabCompleter,
        runnable = runnable
    )
}

inline fun arguments(crossinline block: ArgumentsBuilder.() -> Unit): List<Argument> {
    val builder = ArgumentsBuilder()
    builder.block()
    return builder.arguments
}
