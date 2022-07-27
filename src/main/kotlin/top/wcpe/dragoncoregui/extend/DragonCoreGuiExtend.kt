package top.wcpe.dragoncoregui.extend

import top.wcpe.dragoncoregui.gui.AbstractDragonCoreGui

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
    path: String, fileName: String, crossinline runnable: AbstractDragonCoreGui.() -> Unit
): AbstractDragonCoreGui {
    return object : AbstractDragonCoreGui(path, fileName) {

    }.also(runnable)
}