plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "live.codeland"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring boot basic initiation of server
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.projectlombok:lombok:1.18.26")
	testImplementation("org.springframework.boot:spring-boot-starter-test")


	// mongoDB
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	// validate if the data match the entity by the annotation in model
	implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

	// For handle Http request and response
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

	// JWT token
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Spring Security for authentication and authorization and password encode
	implementation("org.springframework.boot:spring-boot-starter-security")


	runtimeOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
