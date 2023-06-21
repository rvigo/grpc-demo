import com.google.protobuf.gradle.id

plugins {
	id("com.google.protobuf") version "0.9.2"
	id("org.springframework.boot")
	kotlin("plugin.spring")
}

dependencies {
	// spring
	implementation("org.springframework.boot:spring-boot-starter-web")
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
