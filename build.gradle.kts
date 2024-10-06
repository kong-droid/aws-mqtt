import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	id("org.springframework.boot") version "2.7.18"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.example.mqtt"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	// spring boot library
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-data")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.data:spring-data-envers")

	// swagger
	implementation("org.springdoc:springdoc-openapi-ui:1.6.13")
	implementation("org.springdoc:springdoc-openapi-security:1.6.13")

	// aws sdk
	implementation("software.amazon.awssdk.iotdevicesdk:aws-iot-device-sdk:1.20.7")
	implementation("software.amazon.awssdk.crt:aws-crt:0.29.19")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	testCompileOnly("org.projectlombok:lombok:1.18.12")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.12")
}

tasks.apply {
	withType<BootJar> {
		archiveFileName = "aws-mqtt-${version}.jar"
	}
	withType<Test> {
		useJUnitPlatform()
	}
}