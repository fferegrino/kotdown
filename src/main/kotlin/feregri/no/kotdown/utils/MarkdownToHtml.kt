package feregri.no.kotdown.utils

import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.springframework.stereotype.Component


@Component
class MarkdownToHtml {
    private val flavour = CommonMarkFlavourDescriptor()

    fun convert(markdown: String): String {
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        return HtmlGenerator(markdown, parsedTree, flavour).generateHtml()
    }
}