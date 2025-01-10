I started by creating the project in [https://start.spring.io/](https://start.spring.io/).

## Removing the CLI banner

https://stackoverflow.com/a/50829986/605482

```
spring.main.banner-mode=off
```

I also discovered [SDKMAN!](https://sdkman.io/), a toolkit to manage multiple SDK versions ina single system.

# Let's start by adding a CLI

https://ajalt.github.io/clikt/

```kotlin
val output by option("-o", "--output").default("output")
val content by option("-c", "--content").default("content")
```

(Explain what does `by` mean)

# Now I can use `File` to iterate over the content folder

```kotlin
contentPath.walkBottomUp().forEach {
    if( it.isDirectory) {
        return@forEach
    }
    println(it.name)
}
```

# Usually posts have Markdnow and Frontmatter

```markdown
---
title: Post 1
author: Cruyff
date: 2025-01-03
---

# Post 1

Hello world
```

So we need to parse that.

We can just read the file lines:

# To parse the frontmatter

```kotlin
implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")
implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
```

Add a data class to represent the frontmatter:

```kotlin
data class PostMetadataIn(val title: String, val author: String, val date: String)
```

Create an ObjectMapper:

```kotlin
val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
```

And parse the frontmatter:

```kotlin
val postMetadata = mapper.readValue(frontMatterBuilder.toString(), PostMetadataIn::class.java)
```

# To parse the markdown

```kotlin
implementation("org.jetbrains:markdown:0.7.3")
```

And generate the HTML:

```kotlin
val flavour = CommonMarkFlavourDescriptor()
val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(contentSource)
val contentHtml = HtmlGenerator(contentSource, parsedTree, flavour).generateHtml()
```

# Using ThymeLeaf to generate the HTML

```kotlin
implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")
```

Create a template resolver:

```kotlin
val resolver = FileTemplateResolver().apply {
    prefix = "templates/"
    suffix = ".html"
    templateMode = TemplateMode.HTML
    characterEncoding = "UTF-8"
    isCacheable = false
}
```

Create an engine:

```kotlin
val templateEngine = TemplateEngine().apply { 
    setTemplateResolver(resolver) 
}
```

As shown before, the resolver will look for templates in the `templates/` folder. So we need to create a `post.html` template:

```html
<!DOCTYPE html>
<html>
    <head>
        <title th:text="${post.title}">Page Title</title>
    </head>
    <body>

    <h1 th:text="${post.title}">Page Title</h1>
        <div th:utext="${content}">Page Content</div>
    </body>
</html>
```

Then we can render the template, don't forget that this needs a `Context`:

```kotlin
val context = Context().apply {
    setVariable("post", postMetadata)
    setVariable("content", contentHtml)
}

val outputHtml = templateEngine.process("post", context)
println(outputHtml)
```

## Access the resources folder as a File

From StackOverflow: https://stackoverflow.com/a/3924722/605482

```kotlin
File( object {}.javaClass.getResource("/template/").file )
```
