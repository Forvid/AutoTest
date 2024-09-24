plugins {
    kotlin("jvm") version "1.7.0" // Используйте нужную версию Kotlin
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit:junit:4.13.2") // JUnit 4
}

application {
    mainClass.set("MainKt")
}

// Убедитесь, что вы используете JUnit 4
tasks.test {
    useJUnit()
}
