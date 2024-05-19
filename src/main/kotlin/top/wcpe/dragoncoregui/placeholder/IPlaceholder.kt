package top.wcpe.dragoncoregui.placeholder

import org.bukkit.entity.Player
import top.wcpe.dragoncoregui.DragonCoreGui

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
 * @since  : v2.0.0-SNAPSHOT
 */
interface IPlaceholder {
    val placeholderManager: PlaceholderManager
    val key: String

    fun getSubPlaceholder(): SubPlaceholder? {
        val className = javaClass.simpleName
        DragonCoreGui.debug { logger ->
            logger.info("find sub placeholder for $className")
        }
        val placeholder = placeholderManager.getPlaceholder(className) ?: return null
        return placeholder.placeholderMap[key]
    }

    fun putData(
        data: MutableMap<String, String>,
        args: Array<Pair<String, String>>,
        value: String,
    ) {
        data[format(args, true)] = value
        for (format in formats(args, true)) {
            data[format] = value
        }
        data[oldCompatibleFormat(args)] = value
        for (format in oldCompatibleFormats(args)) {
            data[format] = value
        }
    }

    fun putData(data: MutableMap<String, String>, value: String) {
        data[format(emptyArray(), true)] = value
        for (format in formats(emptyArray(), true)) {
            data[format] = value
        }
        data[oldCompatibleFormat(emptyArray())] = value
        for (format in oldCompatibleFormats(emptyArray())) {
            data[format] = value
        }
    }

    fun sendPlaceholder(player: Player, value: String) {
        val data = mutableMapOf<String, String>()

        data[format(emptyArray(), true)] = value
        for (format in formats(emptyArray(), true)) {
            data[format] = value
        }
        data[oldCompatibleFormat(emptyArray())] = value
        for (format in oldCompatibleFormats(emptyArray())) {
            data[format] = value
        }
        top.wcpe.dragoncoregui.extend.sendPlaceholder(
            player.player, data
        )

    }

    fun sendPlaceholder(player: Player, args: Array<Pair<String, String>>, value: String) {
        val data = mutableMapOf<String, String>()
        data[format(args, true)] = value
        for (format in formats(args, true)) {
            data[format] = value
        }
        data[oldCompatibleFormat(args)] = value
        for (format in oldCompatibleFormats(args)) {
            data[format] = value
        }
        top.wcpe.dragoncoregui.extend.sendPlaceholder(
            player.player, data
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
        for (f in formats(args, true)) {
            consumer(player, f)
        }
        consumer(player, oldFormat)
        for (f in oldCompatibleFormats(args)) {
            consumer(player, f)
        }
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
            DragonCoreGui.debug { logger ->
                logger.info("Can't find sub placeholder for $key")
            }
            return ""
        }
        return format(subPlaceholder.oldCompatibleFormat, args, false)
    }

    fun oldCompatibleFormats(args: Array<Pair<String, String>>): List<String> {
        val subPlaceholder = getSubPlaceholder()
        if (subPlaceholder == null) {
            DragonCoreGui.debug { logger ->
                logger.info("Can't find sub placeholder for $key")
            }
            return listOf()
        }
        return subPlaceholder.oldCompatibleFormats.map { format(it, args, false) }
    }

    fun format(args: Array<Pair<String, String>>, hasPrefix: Boolean = true): String {
        val subPlaceholder = getSubPlaceholder()
        if (subPlaceholder == null) {
            DragonCoreGui.debug { logger ->
                logger.info("Can't find sub placeholder for $key")
            }
            return ""
        }
        return format(subPlaceholder.format, args)
    }

    fun formats(args: Array<Pair<String, String>>, hasPrefix: Boolean = true): List<String> {
        val subPlaceholder = getSubPlaceholder()
        if (subPlaceholder == null) {
            DragonCoreGui.debug { logger ->
                logger.info("Can't find sub placeholder for $key")
            }
            return listOf()
        }

        return subPlaceholder.formats.map { format(it, args) }
    }
}