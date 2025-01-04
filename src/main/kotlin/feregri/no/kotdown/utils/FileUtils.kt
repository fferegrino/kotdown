package feregri.no.kotdown.utils

import java.io.File

fun readPost(file: File): Pair<String, String> {
    val articleLines = file.readLines()

    val frontMatterBuilder = StringBuilder()
    val contentBuilder = StringBuilder()

    var frontMatterCount = 0

    articleLines.forEach {
        if (it == "---") {
            frontMatterCount++
            return@forEach
        }

        if (frontMatterCount < 2) {
            frontMatterBuilder.append(it, "\n")
        } else {
            contentBuilder.append(it, "\n")
        }
    }

    return Pair(frontMatterBuilder.toString(), contentBuilder.toString())
}