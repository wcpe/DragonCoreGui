package top.wcpe.dragoncoregui.compose

import org.bukkit.configuration.MemoryConfiguration

/**
 * 由 WCPE 在 2022/7/19 23:19 创建
 *
 * Created by WCPE on 2022/7/19 23:19
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
data class DragonCoreSlotCompose(
    override val key: String,
    var identifier: String = "",
    var drawBackground: String = "",
) : AbstractDragonCoreCompose(key, "slot") {

    override fun convertToConfiguration(): MemoryConfiguration {
        return super.convertToConfiguration().also {
            it["identifier"] = identifier
            it["drawBackground"] = drawBackground
        }
    }
}