package feregri.no.kotdown.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import feregri.no.kotdown.PostMetadataIn
import feregri.no.kotdown.utils.MarkdownToHtml
import feregri.no.kotdown.utils.readPost
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.FileTemplateResolver
import java.io.File

data class PostMetadataOut(val url: String, val title: String)

@Component
class BuildCommand(
    private val objectMapper: ObjectMapper,
//    private val templateEngine: TemplateEngine,
    private val markdownToHtml: MarkdownToHtml
) : CliktCommand() {
    val output by option("-o", "--output").default("output")
    val content by option("-c", "--content").default("content")
    val template by option("-t", "--template").default("")

    override fun run() {
        val outputPath = File(output)
        val contentPath = File(content)
        val templatePath = if (template.isNotBlank()) File(template) else  File( object {}.javaClass.getResource("/template/").file )

        val resolver = FileTemplateResolver().apply {
            prefix = "${templatePath.absolutePath}/"
            suffix = ".html"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            isCacheable = false
        }

        val templateEngine = TemplateEngine().apply {
            addDialect(LayoutDialect())
            setTemplateResolver(resolver)
        }

        outputPath.mkdirs()

        println("Reading from $outputPath")
        println("Writing to $contentPath")

        val outMetadataCollection = mutableListOf<PostMetadataOut>()

        contentPath.walkBottomUp().forEach {
            if (it.isDirectory) {
                return@forEach
            }

            val (frontMatter, rawContent) = readPost(it)
            val postMetadata = objectMapper.readValue(frontMatter, PostMetadataIn::class.java)

            val contentHtml = markdownToHtml.convert(rawContent)

            val context = Context().apply {
                setVariable("post", postMetadata)
                setVariable("content", contentHtml)
            }

            val outputHtml = templateEngine.process("post", context)

            val outputHtmlFile = File(outputPath, it.nameWithoutExtension + ".html")
            outMetadataCollection.add(PostMetadataOut(outputHtmlFile.name, postMetadata.title))
            outputHtmlFile.writeText(outputHtml)
        }

        val indexContext = Context().apply {
            setVariable("posts", outMetadataCollection)
        }

        val indexHtml = templateEngine.process("index", indexContext)

        val indexHtmlFile = File(outputPath, "index.html")

        indexHtmlFile.writeText(indexHtml)
    }
}