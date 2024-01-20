package top.wcpe.dragoncoregui.extend

import org.bukkit.entity.Player
import top.wcpe.dragoncoregui.DragonCoreGuiApi
import top.wcpe.dragoncoregui.gui.AbstractDragonCoreGui
import java.util.function.BiConsumer

/**
 * 由 WCPE 在 2022/7/27 11:43 创建
 *
 * Created by WCPE on 2022/7/27 11:43
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
inline fun <I> I.dragonCoreGui(
    path: String, fileName: String, crossinline runnable: AbstractDragonCoreGui.() -> Unit,
): AbstractDragonCoreGui {
    return object : AbstractDragonCoreGui(path, fileName) {

    }.also(runnable)
}

@Deprecated("This function is deprecated. Use the PacketManager instead.",
    ReplaceWith("PacketManager.registerPacket(abstractPacket: AbstractPacket, pluginInstance: Any)")
)
fun <I> I.dragonCorePacket(
    packetIdentifier: String, consumer: BiConsumer<Player, List<String>>,
) {
    DragonCoreGuiApi.registerPacketHandler(packetIdentifier, consumer)
}
