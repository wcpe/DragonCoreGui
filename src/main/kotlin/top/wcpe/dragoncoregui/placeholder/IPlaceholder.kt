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
        val placeholder = placeholderManager.getPlaceholder(className) ?: return null
        return placeholder.placeholderMap[key]
    }

    fun putData(
        data: MutableMap<String, String>,
        args: Array<Pair<String, String>>,
        value: String,
    ) {
        consumerFormat(args) { format ->
            data[format] = value
        }
    }

    fun putData(data: MutableMap<String, String>, value: String) {
        consumerFormat(arrayOf()) { format ->
            data[format] = value
        }
    }

    fun sendPlaceholder(player: Player, value: String) {
        val data = mutableMapOf<String, String>()
        consumerFormat(player, arrayOf()) { _, format ->
            data[format] = value
        }
        top.wcpe.dragoncoregui.extend.sendPlaceholder(
            player, data
        )
    }

    fun sendPlaceholder(player: Player, args: Array<Pair<String, String>>, value: String) {
        val data = mutableMapOf<String, String>()
        consumerFormat(player, args) { _, format ->
            data[format] = value
        }
        top.wcpe.dragoncoregui.extend.sendPlaceholder(
            player, data
        )
    }

    private fun consumerSingleFormat(
        format: String,
        consumer: (format: String) -> Unit,
    ) {
        if (format.isEmpty()) {
            return
        }
        consumer(format)
    }

    fun consumerFormat(
        args: Array<Pair<String, String>>,
        consumer: (format: String) -> Unit,
    ) {
        val format = format(args, true)
        consumerSingleFormat(format, consumer)
        val oldFormat = oldCompatibleFormat(args)
        consumerSingleFormat(oldFormat, consumer)
        for (f in formats(args, true)) {
            consumerSingleFormat(f, consumer)
        }
        for (f in oldCompatibleFormats(args)) {
            consumerSingleFormat(f, consumer)
        }
    }

    private fun consumerSingleFormat(
        player: Player,
        format: String,
        consumer: (player: Player, format: String) -> Unit,
    ) {
        if (format.isEmpty()) {
            return
        }
        consumer(player, format)
    }

    fun consumerFormat(
        player: Player,
        args: Array<Pair<String, String>>,
        consumer: (player: Player, format: String) -> Unit,
    ) {
        val format = format(args, true)
        consumerSingleFormat(player, format, consumer)
        val oldFormat = oldCompatibleFormat(args)
        consumerSingleFormat(player, oldFormat, consumer)
        for (f in formats(args, true)) {
            consumerSingleFormat(player, f, consumer)
        }
        for (f in oldCompatibleFormats(args)) {
            consumerSingleFormat(player, f, consumer)
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
        val subPlaceholder = getSubPlaceholder() ?: return ""
        return format(subPlaceholder.oldCompatibleFormat, args, false)
    }

    fun oldCompatibleFormats(args: Array<Pair<String, String>>): List<String> {
        val subPlaceholder = getSubPlaceholder() ?: return listOf()
        return subPlaceholder.oldCompatibleFormats.filter { it.isEmpty() }.map { format(it, args, false) }
    }

    fun format(args: Array<Pair<String, String>>, hasPrefix: Boolean = true): String {
        val subPlaceholder = getSubPlaceholder() ?: return ""
        return format(subPlaceholder.format, args)
    }

    fun formats(args: Array<Pair<String, String>>, hasPrefix: Boolean = true): List<String> {
        val subPlaceholder = getSubPlaceholder() ?: return listOf()
        return subPlaceholder.formats.filter { it.isNotEmpty() }.map { format(it, args) }
    }
}