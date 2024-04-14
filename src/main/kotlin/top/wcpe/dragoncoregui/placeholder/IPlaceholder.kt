package top.wcpe.dragoncoregui.placeholder

import org.bukkit.entity.Player
import top.wcpe.dragoncoregui.DragonCoreGui
import kotlin.math.log

/**
 * 由 WCPE 在 2024/4/12 9:53 创建
 * <p>
 * Created by WCPE on 2024/4/12 9:53
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */
interface IPlaceholder {
    val placeholderManager: PlaceholderManager
    val key: String

    fun getSubPlaceholder(): SubPlaceholder? {
        val className = javaClass.simpleName
        val placeholder = placeholderManager.getPlaceholder(className) ?: return null
        return placeholder.placeholderMap[key]
    }

    fun putData(
        data: MutableMap<String, String>,
        args: Array<Pair<String, String>>,
        value: String,
    ) {
        data[format(args, true)] = value
        data[oldCompatibleFormat(args)] = value
    }

    fun putData(data: MutableMap<String, String>, value: String) {
        data[format(emptyArray(), true)] = value
        data[oldCompatibleFormat(emptyArray())] = value
    }

    fun sendPlaceholder(player: Player, value: String) {
        top.wcpe.dragoncoregui.extend.sendPlaceholder(
            player.player, mapOf(format(emptyArray(), true) to value, oldCompatibleFormat(emptyArray()) to value)
        )
    }

    fun sendPlaceholder(player: Player, args: Array<Pair<String, String>>, value: String) {
        top.wcpe.dragoncoregui.extend.sendPlaceholder(
            player.player, mapOf(format(args, true) to value, oldCompatibleFormat(args) to value)
        )
    }

    fun consumerFormat(
        player: Player,
        args: Array<Pair<String, String>>,
        consumer: (player: Player, format: String) -> Unit,
    ) {
        val format = format(args, true)
        val oldFormat = oldCompatibleFormat(args)
        consumer(player, format)
        consumer(player, oldFormat)
    }

    fun format(format: String, args: Array<Pair<String, String>>, hasPrefix: Boolean = true): String {
        var f = format
        for (arg in args) {
            f = f.replace("{${arg.first}}", arg.second)
        }
        return if (hasPrefix) {
            "${javaClass.simpleName}_$f"
        } else {
            f
        }
    }

    fun oldCompatibleFormat(args: Array<Pair<String, String>>): String {
        val subPlaceholder = getSubPlaceholder()
        if (subPlaceholder == null) {
            DragonCoreGui.debug{logger->
                logger.info("Can't find sub placeholder for $key")
            }
            return ""
        }
        return format(subPlaceholder.oldCompatibleFormat, args, false)
    }

    fun format(args: Array<Pair<String, String>>, hasPrefix: Boolean = true): String {
        val subPlaceholder = getSubPlaceholder()
        if (subPlaceholder == null) {
            DragonCoreGui.debug{logger->
                logger.info("Can't find sub placeholder for $key")
            }
            return ""
        }
        return format(subPlaceholder.format, args)
    }
}