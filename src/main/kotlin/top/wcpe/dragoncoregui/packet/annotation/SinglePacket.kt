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
annotation class SinglePacket(
    val name: String,
    val description: String = "",
    val aliases: Array<String> = [],
    val arguments: Array<Argument> = [],
    val playerOnly: Boolean = false,
    val playerOnlyMessage: String = "",
    val opOnly: Boolean = false,
    val opOnlyMessage: String = "",
    val usageMessage: String = "",
    val permission: String = "",
    val permissionMessage: String = "",
)