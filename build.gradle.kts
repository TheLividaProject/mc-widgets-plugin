plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("maven-publish")
}

group = "com.monsterxsquad"
version = "1.0.4"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.3")

    implementation("org.bstats:bstats-bukkit:3.0.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    relocate("org.bstats", "com.monsterxsquad.widgets.libs.bstats")
}

tasks.jar {
    dependsOn(tasks.shadowJar)
}