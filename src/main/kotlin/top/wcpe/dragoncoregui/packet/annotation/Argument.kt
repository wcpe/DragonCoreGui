package top.wcpe.dragoncoregui.packet.annotation

/**
 * 由 WCPE 在 2024/1/14 18:04 创建
 * <p>
 * Created by WCPE on 2024/1/14 18:04
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */

@Retention(AnnotationRetention.RUNTIME)
annotation class Argument(
    val name: String, val required: Boolean = true, val description: String = "",
)