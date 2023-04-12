package top.wcpe.dragoncoregui.gui

import java.util.function.Consumer

/**
 * 由 WCPE 在 2023/4/13 1:42 创建
 * <p>
 * Created by WCPE on 2023/4/13 1:42
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-1
 */
object DragonCoreGuiManager {

    @JvmStatic
    private val dragonCoreGuiMap = mutableMapOf<String, AbstractDragonCoreGui>()

    @JvmStatic
    fun registerDragonCoreGui(dragonCoreGui: AbstractDragonCoreGui) {
        dragonCoreGuiMap[dragonCoreGui.fullPath] = dragonCoreGui
    }

    @JvmStatic
    fun consumerDragonCoreGui(fullPath: String, consumer: Consumer<AbstractDragonCoreGui>) {
        consumer.accept(dragonCoreGuiMap[fullPath] ?: return)
    }
}