package top.wcpe.dragoncoregui.compose

import org.bukkit.configuration.MemoryConfiguration

/**
 * 由 WCPE 在 2022/7/18 5:22 创建
 *
 * Created by WCPE on 2022/7/18 5:22
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
data class DragonCoreTextureCompose(
    override val key: String,
    var texture: String = "",
    var textureHovered: String = "",
    var textureWidth: String = "",
    var textureHeight: String = "",
    var v: String = "",
    var h: String = "",
    var text: String = "",
    var color: String = "",
    var font: String = "",
) : AbstractDragonCoreCompose(key, "texture") {

    override fun convertToConfiguration(): MemoryConfiguration {
        return super.convertToConfiguration().also {
            it["texture"] = texture
            it["textureHovered"] = textureHovered
            it["textureWidth"] = textureWidth
            it["textureHeight"] = textureHeight
            it["v"] = v
            it["h"] = h
            it["text"] = text
            it["color"] = color
            it["font"] = font
        }
    }
}