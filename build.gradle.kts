import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
    id("com.diffplug.spotless") version "6.9.0"
    id("com.google.cloud.tools.jib") version "3.2.1"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    kotlin("plugin.jpa") version "1.7.10"
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
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

ext {
    set("spring_cloud_version", "2021.0.3")
    set("testcontainers_version", "1.17.3")
    set("springdoc_openapi_version", "1.6.9")
    set("jsoup_version", "1.15.2")
    set("commons_lang_version", "3.12.0")
    set("commons_io_version", "2.11.0")
    set("opencsv_version", "5.6")
    set("jjwt_version", "0.9.1")
    set("problem_spring_web_version", "0.27.0")
    set("archunit_version", "0.23.1")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:${property("jjwt_version")}")
    implementation("org.zalando:problem-spring-web-starter:${property("problem_spring_web_version")}")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.flywaydb:flyway-core")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springdoc:springdoc-openapi-ui:${property("springdoc_openapi_version")}")
    implementation("com.opencsv:opencsv:${property("opencsv_version")}")
    implementation("org.jsoup:jsoup:${property("jsoup_version")}")
    implementation("org.apache.commons:commons-lang3:${property("commons_lang_version")}")
    implementation("commons-io:commons-io:${property("commons_io_version")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.tngtech.archunit:archunit-junit5:${property("archunit_version")}")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("spring_cloud_version")}")
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainers_version")}")
    }
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
    imageName = "sivaprasadreddy/devzone-api-springboot-kotlin"
}

jib {
    from {
        image = "eclipse-temurin:17-jre-focal"
    }
    to {
        image = "sivaprasadreddy/devzone-api-springboot-kotlin"
        tags = setOf("latest")
    }
    container {
        jvmFlags = listOf("-Xms512m", "-Xdebug")
        mainClass = "com.sivalabs.devzone.ApplicationKt"
        args = listOf()
        ports = listOf("8080/tcp")
    }
}

gitProperties {
    failOnNoGitDirectory = false
    keys = listOf(
        "git.branch",
        "git.commit.id.abbrev",
        "git.commit.user.name",
        "git.commit.message.full"
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

tasks {
    wrapper {
        gradleVersion = "7.5"
    }
}
