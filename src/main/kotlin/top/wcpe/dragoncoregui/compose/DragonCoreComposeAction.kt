package top.wcpe.dragoncoregui.compose

/**
 * 由 WCPE 在 2022/7/18 22:36 创建
 *
 * Created by WCPE on 2022/7/18 22:36
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
enum class DragonCoreComposeAction(val actionName: String, val enableSendPacker: Boolean) {
    CREATE("create", true),
    REMOVE("remove", true),
    PRE_RENDER("preRender", false),
    POST_RENDER("postRender", false),
    CLICK_LEFT("click_left", true),
    CLICK_RIGHT("click_right", true),
    CLICK_MIDDLE("click_middle", true),
    CLICK("click", true),
    RELEASE_LEFT("release_left", true),
    RELEASE_RIGHT("release_right", true),
    RELEASE_MIDDLE("release_middle", true),
    RELEASE("release", true),
    ENTER("enter", true),
    LEAVE("leave", true),
    WHEEL("wheel", true),
    TEXTCHANGE(
        "textChange", true
    );

    val callBackMethod =
        "方法.发包('DragonCoreGui_actions', '%gui_full_path%', '$actionName', '%compose_key%'%get_value_method%)"
}