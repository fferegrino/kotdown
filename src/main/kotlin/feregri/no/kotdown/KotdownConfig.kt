package feregri.no.kotdown

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.FileTemplateResolver

@Configuration
class KotdownConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).registerKotlinModule()
    }

    @Bean
    fun templateEngine(): TemplateEngine {
        val resolver = FileTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            isCacheable = false
        }

        return TemplateEngine().apply {
            setTemplateResolver(resolver)
        }
    }
}
