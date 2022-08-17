import java.time.Instant

fun main(args: Array<String>) {
    val title = "Interesting science and psychology articles to read - part 2"
    val epochSecond = Instant.now().epochSecond
    val list = listOf(Pair("title", "url"))
    joinToString(
        list,
        StringBuilder(),
        "\n",
        "<DT><H3 FOLDED ADD_DATE=\"$epochSecond\">$title</H3>\n<DL><p>\n",
        "</DL><p>"
    ) {
        "<DT><A HREF=\"${it.second}\" ADD_DATE=\"$epochSecond\" LAST_VISIT=\"$epochSecond\" LAST_MODIFIED=\"$epochSecond\">${it.first}</A>"
    }.also { println(it) }
}

fun <T> joinToString(
    iterable: Iterable<T>,
    buffer: StringBuilder,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
): StringBuilder {
    buffer.append(prefix)
    var count = 0
    for (element in iterable) {
        if (++count > 1) buffer.append(separator)
        if (limit < 0 || count <= limit) {
            buffer.append(if (transform != null) transform(element) else element)
        } else break
    }
    if (limit >= 0 && count > limit) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}