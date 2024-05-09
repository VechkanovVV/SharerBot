plugins {
    java
    checkstyle
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-webflux:6.1.6")
    implementation("com.github.pengrad:java-telegram-bot-api:6.6.0")
    implementation("io.micrometer:micrometer-registry-prometheus:1.11.0")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.1.0")



    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.projectlombok:lombok:1.18.32")

    annotationProcessor("org.projectlombok:lombok:1.18.32")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
