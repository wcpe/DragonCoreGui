package top.wcpe.dragoncoregui.compose

import org.bukkit.configuration.MemoryConfiguration

/**
 * 由 WCPE 在 2022/7/19 23:21 创建
 *
 * Created by WCPE on 2022/7/19 23:21
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
data class DragonCoreTextBoxCompose(
    override val key: String,
    var length: String = "",
    var drawBackground: String = "",
    var text: String = "",
    var focused: String = "",
) : AbstractDragonCoreCompose(key, "textbox") {

    override fun convertToConfiguration(): MemoryConfiguration {
        return super.convertToConfiguration().also {
            it["length"] = length
            it["drawBackground"] = drawBackground
            it["text"] = text
            it["focused"] = focused
        }
    }
}