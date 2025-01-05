plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "feregri.no"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
	implementation("com.github.ajalt.clikt:clikt:5.0.1")
	implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.3.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains:markdown:0.7.3")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
