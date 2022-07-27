package top.wcpe.dragoncoregui.compose

import org.bukkit.configuration.MemoryConfiguration

/**
 * 由 WCPE 在 2022/7/19 23:23 创建
 *
 * Created by WCPE on 2022/7/19 23:23
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
data class DragonCoreEntityCompose(
    override val key: String,
    var entity: String = "",
    var head: String = "",
    var followMouse: String = "",
) : AbstractDragonCoreCompose(key, "entity") {

    override fun convertToConfiguration(): MemoryConfiguration {
        return super.convertToConfiguration().also {
            it["entity"] = entity
            it["head"] = head
            it["followMouse"] = followMouse
        }
    }
}