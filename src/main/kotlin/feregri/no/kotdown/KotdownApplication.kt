package feregri.no.kotdown

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotdownApplication : CommandLineRunner {

    private val log = LoggerFactory.getLogger(KotdownApplication::class.java)

    override fun run(vararg args: String?) {
        log.info(args.joinToString())
        println("Hello World")
    }
}

fun main(args: Array<String>) {
    runApplication<KotdownApplication>(*args)
}
