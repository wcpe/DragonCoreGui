package top.wcpe.dragoncoregui.compose

import org.bukkit.configuration.MemoryConfiguration

/**
 * 由 WCPE 在 2022/7/19 7:08 创建
 *
 * Created by WCPE on 2022/7/19 7:08
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
data class DragonCoreLabelCompose(
    override val key: String,
    var texts: String = "",
    var color: String = "",
    var center: String = "",
    var shadow: String = "",
    var length: String = "",
) : AbstractDragonCoreCompose(key, "label") {

    override fun convertToConfiguration(): MemoryConfiguration {
        return super.convertToConfiguration().also {
            it["texts"] = texts
            it["color"] = color
            it["center"] = center
            it["shadow"] = shadow
            it["length"] = length
        }
    }
}