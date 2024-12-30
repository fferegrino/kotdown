package feregri.no.kotdown

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.FileTemplateResolver
//import org.thymeleaf.engine.Temp
import java.io.File

data class PostMetadataIn(val title: String, val author: String, val date: String)

class BuildCommand : CliktCommand() {
    val output by option("-o", "--output").default("output")
    val content by option("-c", "--content").default("content")

    override fun run() {
        val outputPath = File(output)
        val contentPath = File(content)

        outputPath.mkdirs()

        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

        val resolver = FileTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            isCacheable = false
        }

        val templateEngine = TemplateEngine().apply {
            setTemplateResolver(resolver)
        }


        println("Reading from $outputPath")
        println("Writing to $contentPath")

        contentPath.walkBottomUp().forEach {
            if( it.isDirectory) {
                return@forEach
            }

            val articleLines = it.readLines()

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
                }
                else
                {
                    contentBuilder.append(it, "\n")
                }
            }

            val postMetadata = mapper.readValue(frontMatterBuilder.toString(), PostMetadataIn::class.java)
            val contentSource = contentBuilder.toString()

            val flavour = CommonMarkFlavourDescriptor()
            val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(contentSource)
            val contentHtml = HtmlGenerator(contentSource, parsedTree, flavour).generateHtml()

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

@SpringBootApplication
class KotdownApplication : CommandLineRunner {
    override fun run(args: Array<String>) = BuildCommand().main(args)
}

fun main(args: Array<String>) {
    runApplication<KotdownApplication>(*args)
}
