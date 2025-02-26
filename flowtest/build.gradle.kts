import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.test)
    testImplementation(libs.junit)
}

val dokkaPath = layout.buildDirectory.dir("dokka")
dokka {
    moduleName = "Flow Test"
    dokkaPublications.html {
        // output dir = ./flowtest/build/dokka
        outputDirectory.set(dokkaPath)
    }
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").reader())
}

// example of uploading a generated documentation on remote server
// through ssh with key-based authentication
tasks.register("uploadDokka") {
    
    // 1. this task works on Linux-based operation systems 
    //    with pre-installed ssh, scp and sshpass;
    
    // 2. make sure you set correct values in /local.properties 
    //    and /flowtest/local-pass.txt files
    
    dependsOn("dokkaGenerate")
    doLast {
        exec {
            val uploadDokkaSshKey: String by properties
            val uploadDokkaSshPassFile: String by properties
            val uploadDokkaSshHost: String by properties
            val uploadDokkaSshRemoteDir: String by properties
            workingDir(layout.buildDirectory)
            commandLine(
                "sh", "../upload-dokka.sh",
                uploadDokkaSshKey, uploadDokkaSshPassFile,
                uploadDokkaSshHost, uploadDokkaSshRemoteDir,
            )
        }
    }
}

// archive all sources to 'flowtest-1.0.0-sources.jar'
val sourcesJar by tasks.registering(Jar::class) {
    val mainSourceSet = sourceSets.main.get()
    archiveClassifier.set("sources")
    from(mainSourceSet.java.sourceDirectories)
    from(mainSourceSet.kotlin.sourceDirectories)
}

// archive documentation files to 'flowtest-1.0.0-javadoc.jar'
val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaGenerate)
    archiveClassifier.set("javadoc")
    from(dokkaPath)
}

signing {
    val signingKeyId: String by properties
    val signingKey: String by properties
    val signingPassword: String by properties
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("maven"))
        }
    }
    publications {
        create<MavenPublication>("release") {
            groupId = "com.uandcode"
            artifactId = "flowtest"
            version = "1.0.0"

            artifact(sourcesJar) // library sources
            artifact(javadocJar) // library documentation
            from(components["java"]) // compiled library

            pom {
                name = "Flow Test"
                description = "A tiny library for easier testing of Kotlin Flows"
                licenses {
                    license {
                        name = "Apache License 2.0"
                        url = "https://github.com/romychab/flow-test/blob/main/LICENSE"
                    }
                }
                developers {
                    developer {
                        id = "romychab"
                        name = "Roman Andrushchenko"
                        email = "rom.andrushchenko@gmail.com"
                    }
                }
                scm {
                    connection = "scm:git:github.com/romychab/flow-test.git"
                    developerConnection = "scm:git:ssh://github.com/romychab/flow-test.git"
                    url = "https://github.com/romychab/flow-test/tree/main"
                }
            }
        }
    }
}
