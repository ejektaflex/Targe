import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "io.ejekta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {

    //

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing")

    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    //implementation("media.kamel:kamel-image:0.9.1")

    implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha04")

    //implementation("io.coil-kt.coil3:coil-network-okhttp:[coil-version]")

    implementation("io.ktor:ktor-client-apache5:2.3.8")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "targe"
            packageVersion = "1.0.0"
        }
    }
}
