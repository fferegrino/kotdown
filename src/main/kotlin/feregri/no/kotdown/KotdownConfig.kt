package feregri.no.kotdown

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.FileTemplateResolver

@Configuration
class KotdownConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).registerKotlinModule()
    }

}
