package top.wcpe.dragoncoregui.extend

import top.wcpe.dragoncoregui.compose.*
import top.wcpe.dragoncoregui.gui.AbstractDragonCoreGui

/**
 * 由 WCPE 在 2022/7/27 10:44 创建
 *
 * Created by WCPE on 2022/7/27 10:44
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
inline fun <T : AbstractDragonCoreGui> T.texture(
    key: String, crossinline runnable: DragonCoreTextureCompose.() -> Unit
): DragonCoreTextureCompose {
    return DragonCoreTextureCompose(key).also {
        runnable(it)
        addCompose(it)
    }
}

inline fun <E : AbstractDragonCoreGui> E.entity(
    key: String, crossinline runnable: DragonCoreEntityCompose.() -> Unit
): DragonCoreEntityCompose {
    return DragonCoreEntityCompose(key).also {
        runnable(it)
        addCompose(it)
    }
}

inline fun <L : AbstractDragonCoreGui> L.label(
    key: String, crossinline runnable: DragonCoreLabelCompose.() -> Unit
): DragonCoreLabelCompose {
    return DragonCoreLabelCompose(key).also {
        runnable(it)
        addCompose(it)
    }
}

inline fun <T : AbstractDragonCoreGui> T.textbox(
    key: String, crossinline runnable: DragonCoreTextBoxCompose.() -> Unit
): DragonCoreTextBoxCompose {
    return DragonCoreTextBoxCompose(key).also {
        runnable(it)
        addCompose(it)
    }
}

inline fun <S : AbstractDragonCoreGui> S.slot(
    key: String, crossinline runnable: DragonCoreSlotCompose.() -> Unit
): DragonCoreSlotCompose {
    return DragonCoreSlotCompose(key).also {
        runnable(it)
        addCompose(it)
    }
}
