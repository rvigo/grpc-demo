import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    id("org.springframework.boot") version "3.0.3" apply false
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

dependencies {
    kover(project(":client"))
    kover(project(":server"))
}

allprojects {
    group = "com.rvigo"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // gRPC (needed to build the proto files)
        implementation("io.grpc:grpc-protobuf:1.53.0")
        implementation("io.grpc:grpc-kotlin-stub:1.3.0")
        implementation("com.google.protobuf:protobuf-java:3.22.0")
        implementation("com.google.protobuf:protobuf-kotlin:3.22.0")
        implementation("io.grpc:grpc-netty-shaded:1.53.0")

        // log
        implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

        // tests
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

koverReport {
    defaults {
        verify {
            rule {
                bound {
                    minValue = 75
                    maxValue = 100
                }
            }
        }
    }
}
