package top.wcpe.dragoncoregui.gui

/**
 * 由 WCPE 在 2022/7/19 23:58 创建
 *
 * Created by WCPE on 2022/7/19 23:58
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
enum class DragonCoreGuiFunctions(val functionName: String) {
    OPEN("open"),
    CLOSE("close"),
    KEY_PRESS("keyPress"),
    WHEEL("wheel"),
    CHAT_OPEN("chatOpen"),
    CHAT_CLOSE("chatClose"),
    MESSAGE("message"),
    RELOAD("reload");

    val callBackMethod = "方法.发包('DragonCoreGui_Functions', '%gui_full_path%', '$functionName')"

}