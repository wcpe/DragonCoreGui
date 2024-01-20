package top.wcpe.dragoncoregui.packet.annotation

/**
 * 由 WCPE 在 2024/1/14 18:09 创建
 * <p>
 * Created by WCPE on 2024/1/14 18:09
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.3.0-SNAPSHOT
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChildPacket(
    val name: String,
    val description: String = "",
    val arguments: Array<Argument> = [],
    val usageMessage: String = "",
    val shouldDisplay: Boolean = false,
)