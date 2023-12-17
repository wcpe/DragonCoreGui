package top.wcpe.dragoncoregui

import org.bukkit.entity.Player
import java.util.function.BiConsumer

/**
 * 由 WCPE 在 2023/12/14 11:24 创建
 * <p>
 * Created by WCPE on 2023/12/14 11:24
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-SNAPSHOT
 */
object DragonCoreGuiApi {

    fun registerPacketHandler(packetIdentifier: String, consumer: BiConsumer<Player, List<String>>) {
        DragonCoreGui.instance.registerPacketHandler(packetIdentifier, consumer)
    }
}