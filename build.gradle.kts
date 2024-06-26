import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.gorylenko.gradle-git-properties") version "2.4.2"
    id("com.diffplug.spotless") version "6.25.0"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
}

group = "com.sivalabs"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

extra["springdoc_openapi_version"] = "2.5.0"
extra["commons_io_version"] = "2.16.1"
extra["opencsv_version"] = "5.9"
extra["jjwt_version"] = "0.12.5"
extra["instancio_version"] = "4.8.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:${property("jjwt_version")}")
    implementation("io.jsonwebtoken:jjwt-impl:${property("jjwt_version")}")
    implementation("io.jsonwebtoken:jjwt-jackson:${property("jjwt_version")}")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    testImplementation("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("springdoc_openapi_version")}")
    implementation("com.opencsv:opencsv:${property("opencsv_version")}")
    implementation("org.apache.commons:commons-lang3")
    implementation("commons-io:commons-io:${property("commons_io_version")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.instancio:instancio-junit:${property("instancio_version")}")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events.addAll(listOf(FAILED, STANDARD_ERROR, SKIPPED, PASSED))
        exceptionFormat = FULL
    }
}

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName.set("sivaprasadreddy/devzone-api-springboot-kotlin")
}

gitProperties {
    failOnNoGitDirectory = false
    keys =
        listOf(
            "git.branch",
            "git.commit.id.abbrev",
            "git.commit.user.name",
            "git.commit.message.full",
        )
}

spotless {
    kotlin {
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
        ktlint()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}
