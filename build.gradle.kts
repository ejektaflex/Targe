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


    //implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.desktop:desktop-jvm-windows-x64:1.6.0-rc03")
    implementation(compose.material3)

    implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha04")

    //implementation("io.coil-kt.coil3:coil-network-okhttp:[coil-version]")

    implementation("io.ktor:ktor-client-apache5:2.3.8")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        // Targe, an image tagger.
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "targe"
            packageVersion = "1.0.0"
        }
    }
}
