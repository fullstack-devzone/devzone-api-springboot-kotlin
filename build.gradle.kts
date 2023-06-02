import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
    id("com.diffplug.spotless") version "6.19.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.jpa") version "1.8.21"
}

group = "com.sivalabs"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
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

ext {
    set("springdoc_openapi_version", "1.7.0")
    set("commons_lang_version", "3.12.0")
    set("commons_io_version", "2.12.0")
    set("opencsv_version", "5.7.1")
    set("jjwt_version", "0.11.5")
    set("instancio_version", "2.16.0")
}

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
    testImplementation("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springdoc:springdoc-openapi-ui:${property("springdoc_openapi_version")}")
    implementation("com.opencsv:opencsv:${property("opencsv_version")}")
    implementation("org.apache.commons:commons-lang3:${property("commons_lang_version")}")
    implementation("commons-io:commons-io:${property("commons_io_version")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.instancio:instancio-junit:${property("instancio_version")}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
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
    keys = listOf(
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
