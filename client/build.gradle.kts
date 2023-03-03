import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.3"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	id("com.google.protobuf") version "0.9.2"
}

group = "com.rvigo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	// spring
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
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

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.22.0"
	}

	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.53.0"
		}
		id("grpckt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar"
		}
	}

	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				id("grpc")
				id("grpckt")
			}

			it.builtins {
				id("kotlin")
			}
		}
	}
}
