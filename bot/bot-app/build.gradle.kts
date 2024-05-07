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

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	

	testImplementation("org.springframework.boot:spring-boot-starter-test")


	implementation("com.github.pengrad:java-telegram-bot-api:6.6.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
