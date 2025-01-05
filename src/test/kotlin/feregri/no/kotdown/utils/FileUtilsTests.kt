package feregri.no.kotdown.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path
import java.util.UUID

@SpringBootTest
class FileUtilsTests {
	@TempDir(cleanup = CleanupMode.ON_SUCCESS)
	lateinit var tempDirectory: Path

	fun randomString(): String {
		return UUID.randomUUID().toString()
	}

	@Test
	fun `read contents successfully`() {
		val fileName = randomString()
		val file = tempDirectory.resolve("$fileName.md").toFile()
		file.writeText(
			"""
			---
			title: "Test"
			---
			
			# Test
			""".trimIndent()
		)

		val (frontMatter, content) = readPost(file)

		assert(frontMatter == "title: \"Test\"\n")
		assert(content == "\n# Test\n")
	}

	@Test
	fun `read contents with empty front matter`() {
		val fileName = randomString()
		val file = tempDirectory.resolve("$fileName.md").toFile()
		file.writeText("""# Test""")

		assertThrows<IllegalArgumentException> {
			readPost(file)
		}
	}

	@Test
	fun `read contents with empty content`() {
		val fileName = randomString()
		val file = tempDirectory.resolve("$fileName.md").toFile()
		file.writeText("""---
			title: "Test"
			---
			""".trimIndent())

		assertThrows<IllegalArgumentException> {
			readPost(file)
		}
	}
}
