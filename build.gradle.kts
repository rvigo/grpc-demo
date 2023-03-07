plugins { kotlin("jvm") version "1.7.22" }

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":client"))
    implementation(project(":server"))
}
