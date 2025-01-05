package feregri.no.kotdown.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path
import java.util.UUID

@SpringBootTest
class MarkdownToHtmlTests {

	@Test
	fun `convert markdown to html`() {
		val markdown = "# Hello"
		val html = MarkdownToHtml().convert(markdown)
		assert(html == "<h1>Hello</h1>")
	}
}
