package feregri.no.kotdown

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotdownApplication

fun main(args: Array<String>) {
	runApplication<KotdownApplication>(*args)
}
