package feregri.no.kotdown

import com.github.ajalt.clikt.core.main
import feregri.no.kotdown.commands.BuildCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


data class PostMetadataIn(val title: String, val author: String, val date: String)

@SpringBootApplication
class KotdownApplication : CommandLineRunner {

    @Autowired
    lateinit var buildCommand: BuildCommand

    override fun run(args: Array<String>) {
        buildCommand.main(args)
    }
}

fun main(args: Array<String>) {
    runApplication<KotdownApplication>(*args)
}
