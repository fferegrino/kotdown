package feregri.no.kotdown.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import feregri.no.kotdown.PostMetadataIn
import feregri.no.kotdown.utils.readPost
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.io.File

@Component
class BuildCommand(
    private val objectMapper: ObjectMapper,
    private val templateEngine: TemplateEngine
) : CliktCommand() {
    val output by option("-o", "--output").default("output")
    val content by option("-c", "--content").default("content")

    override fun run() {
        val outputPath = File(output)
        val contentPath = File(content)

        outputPath.mkdirs()

        println("Reading from $outputPath")
        println("Writing to $contentPath")

        contentPath.walkBottomUp().forEach {
            if (it.isDirectory) {
                return@forEach
            }

            val (frontMatter, rawContent) = readPost(it)
            val postMetadata = objectMapper.readValue(frontMatter, PostMetadataIn::class.java)

            val flavour = CommonMarkFlavourDescriptor()
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(rawContent)
            val contentHtml = HtmlGenerator(rawContent, parsedTree, flavour).generateHtml()

            val context = Context().apply {
                setVariable("post", postMetadata)
                setVariable("content", contentHtml)
            }

            val outputHtml = templateEngine.process("post", context)

            val outputHtmlFile = File(outputPath, it.nameWithoutExtension + ".html")

            outputHtmlFile.writeText(outputHtml)
        }


    }
}