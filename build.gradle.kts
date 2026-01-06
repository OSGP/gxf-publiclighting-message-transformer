import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.dependencyManagement)
    alias(libs.plugins.gradleWrapperUpgrade)
    alias(libs.plugins.jacoco)
    alias(libs.plugins.jacocoReportAggregation)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.spotless)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.javaTestFixtures)
    `jvm-test-suite`
}

extra["junit-jupiter.version"] = libs.junitJupiterApi.get().version

group = "org.lfenergy.gxf"
description = "GXF public lighting transformer between protobuf and object messages"

version = System.getenv("GITHUB_REF_NAME")?.replace("/", "-")?.lowercase() ?: "develop"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

sonar {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "OSGP_gxf-publiclighting-message-transformer")
        property("sonar.organization", "gxf")
    }
}

wrapperUpgrade {
    gradle {
        register("gxf-publiclighting-message-transformer") {
            repo.set("OSGP/gxf-publiclighting-message-transformer")
            baseBranch.set("main")
        }
    }
}

repositories {
    mavenCentral()
    maven {
        name = "GXFGithubPackages"
        url = uri("https://maven.pkg.github.com/osgp/*")
        credentials {
            username = project.findProperty("github.username") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("github.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    developmentOnly(libs.springBootCompose)
    developmentOnly(libs.springBootDevtools)

    implementation("org.lfenergy.gxf:gxf-publiclighting-contracts-internal")

    implementation(libs.jodaTime)

    implementation(libs.kotlinLoggingJvm)
    implementation(libs.kotlinReflect)
    implementation(libs.micrometerRegistryPrometheus)
    implementation(libs.pooledJms)
    implementation(libs.protobufKotlin)
    implementation(libs.springBootStarterActuator)
    implementation(libs.springBootStarterArtemis)
    implementation(libs.springBootStarterWeb)

    implementation(libs.springModulithStarterCore)

    runtimeOnly(libs.springModulithActuator)
    runtimeOnly(libs.springModulithObservability)

    annotationProcessor(libs.springBootConfigurationProcessor)

    testFixturesImplementation(libs.jodaTime)
    testFixturesImplementation(libs.protobufKotlin)
    testFixturesImplementation("org.lfenergy.gxf:gxf-publiclighting-contracts-internal")

    testImplementation(testFixtures(project))

    testImplementation(libs.assertJ)
    testImplementation(libs.kotlinJunit)
    testImplementation(libs.mockkJvm)
    testImplementation(libs.mockkSpring)
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springModulithStarterTest)

    testRuntimeOnly(libs.junitLauncher)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:${libs.versions.springModulith.get()}")
    }
}

kotlin { compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict") } }

extensions.configure<SpotlessExtension> {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.6.0")

        licenseHeaderFile(file("./spotless/license-header-template.kt"))
    }
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.named<Jar>("bootJar") { archiveFileName.set("gxf-publiclighting-message-transformer.jar") }

testing {
    suites {
        val integrationTest by registering(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))

                implementation("org.lfenergy.gxf:gxf-publiclighting-contracts-internal")

                implementation(libs.assertJ)
                implementation(libs.cucumberJava)
                implementation(libs.cucumberJunitPlatformEngine)
                implementation(libs.cucumberSpring)
                implementation(libs.junitJupiterApi)
                implementation(libs.junitPlatformSuite)
                implementation(libs.junitPlatformConsole)
                implementation(libs.pooledJms)
                implementation(libs.protobufKotlin)
                implementation(libs.springBootStarterArtemis)
                implementation(libs.springBootStarterTest)
                implementation(libs.springBootTestcontainers)
                implementation(libs.testContainers)
                implementation(libs.testContainersJUnit)
                implementation(libs.testContainersArtemis)
            }
            targets { all { testTask.configure { shouldRunAfter("test") } } }
        }
    }

    // Make `check` run integration tests
    tasks.named("check") { dependsOn("integrationTest") }

    // Jacoco code coverage report of unit and integration tests
    tasks.register<JacocoReport>("aggregateTestCodeCoverageReport") {
        description = "Generates code coverage report for all tests."
        group = "Verification"
        dependsOn("test", "integrationTest")

        executionData(
            fileTree(layout.buildDirectory.dir("jacoco")) {
                include("test.exec", "test/*.exec", "*.exec")
            },
        )
        sourceSets(sourceSets["main"])
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
        // filter out generated classes if needed:
        classDirectories.setFrom(
            classDirectories.files.map {
                fileTree(it) { exclude("**/generated/**") }
            },
        )
    }
}
