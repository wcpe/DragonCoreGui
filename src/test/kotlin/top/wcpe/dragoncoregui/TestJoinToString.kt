package top.wcpe.dragoncoregui

/**
 * 由 WCPE 在 2022/7/27 9:46 创建
 *
 * Created by WCPE on 2022/7/27 9:46
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
fun main(args: Array<String>) {
    val list = listOf("a", "n", "sad", "at", "s")
    println(list.subList(3, list.size))
    val f = list.filter { it.isNotEmpty() }
    println(f.isEmpty())
    println(
        "方法.发包('DragonCoreGui_actions', '%gui_full_path%', 'test', '%compose_key%'%get_value_method%)".replace(
            "%get_value_method%", list.joinToString(prefix = ", ", separator = ", ", postfix = "")
        )
    )
}