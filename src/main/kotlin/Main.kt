import java.io.File
import java.nio.file.Files
import java.time.Instant
data class Link(val folderTitle: String, val articleTitle: String, val url: String)
fun main(args: Array<String>) {
    val epochSecond = Instant.now().epochSecond
    Files.readAllLines(File("/Users/shmuel/Downloads/microsoft_edge_collections.tsv").toPath())
        .asSequence()
        .drop(1)
        .map {
            it
                .split("\t")
                .iterator()
                .run {
                    Link(
                        next(),
                        next(),
                        next().let {
                            it.substring(12, it.lastIndexOf("\"\",\"\"websiteName\"\""))
                        }
                    )
                }
        }
        .groupBy { it.folderTitle }
        .map { (title, list) ->
            joinToString(
                list,
                StringBuilder(),
                "\n",
                "<DT><H3 FOLDED ADD_DATE=\"$epochSecond\">$title</H3>\n<DL><p>\n",
                "</DL><p>"
            ) {
                "<DT><A HREF=\"${it.url}\" ADD_DATE=\"$epochSecond\" LAST_VISIT=\"$epochSecond\" LAST_MODIFIED=\"$epochSecond\">${it.articleTitle}</A>"
            }
        }
        .joinToString("\n", "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n" +
                "\t<HTML>\n" +
                "\t<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n" +
                "\t<Title>Bookmarks</Title>\n" +
                "\t<H1>Bookmarks</H1>", "</HTML>").also { println(it) }
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