import java.io.File
import java.nio.file.Files
import java.time.Instant
data class Link(val folderTitle: String, val articleTitle: String, val url: String)
fun main(args: Array<String>) {
    val epochSecond = Instant.now().epochSecond
    val downloads = "/Users/shmuel/Downloads/"
    Files.readAllLines(File("${downloads}microsoft_edge_collections.tsv").toPath())
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
        .filterKeys { !it.startsWith("Interesting science and psychology articles to read - part ") }
        .map { (title, list) ->
            joinToString(
                list,
                StringBuilder(),
                "\n",
                "\n\t<DT><H3 FOLDED>${title.replace("\"{2,}".toRegex(), "\"")}</H3>\n\t<DL><p>\n",
                "\n\t</DL><p>"
            ) {
                "\t\t<DT><A HREF=\"${it.url}\">${it.articleTitle}</A>"
            }
        }
        .joinToString("\n", "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n" +
                "\t<HTML>\n" +
                "\t<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n" +
                "\t<Title>Bookmarks</Title>\n" +
                "\t<H1>Bookmarks</H1>", "\n</HTML>").also {
            println(it)
            File(downloads, "Edge collections.html").writeText(it)
                }
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