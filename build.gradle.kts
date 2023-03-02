import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
	id("org.springframework.boot") version "3.0.3"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	id("com.google.protobuf") version "0.8.18"
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

	// gRPC
	implementation("io.grpc:grpc-netty:1.44.1")
	implementation("io.grpc:grpc-protobuf:1.44.1")
	implementation("io.grpc:grpc-kotlin-stub:1.2.1")

	// database
	implementation("org.jetbrains.exposed", "exposed-core", "0.40.1")
	implementation("org.jetbrains.exposed", "exposed-dao", "0.40.1")
	implementation("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")
	implementation("org.postgresql:postgresql:42.2.2")

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
	generatedFilesBaseDir = "$buildDir/generated/sources/proto"
	protoc {
		artifact = "com.google.protobuf:protoc:3.12.2"
	}

	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.30.0"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc"){
					outputSubDir = "grpc"
				}
			}
		}
	}
}

sourceSets{
	main{
		proto{
			srcDir("$buildDir/generated/sources/proto/main/grpc")
		}
	}
}
