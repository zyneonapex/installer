plugins {
    java
    application
    id("org.graalvm.buildtools.native") version "0.10.3"
}

group = "com.zyneonstudios.apex"
version = "1.0.0"

val apexType = "alpha"
val apexVendor = "Zyneon Apex"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.avaje.webview:avaje-webview:0.29")
}

application {
    mainClass.set("com.zyneonstudios.apex.installer.Main")
}

graalvmNative {
    agent {
        defaultMode.set("standard")
    }
    binaries {
        named("main") {
            imageName.set("ApexInstaller")
            buildArgs.add("-H:+ReportExceptionStackTraces")
            buildArgs.add("-H:NativeLinkerOption=/SUBSYSTEM:WINDOWS")
            buildArgs.add("-H:NativeLinkerOption=/ENTRY:mainCRTStartup")
            excludeConfig.put("io.avaje.webview:avaje-webview:0.29", listOf("^/META-INF/native-image/.*"))
            buildArgs.add("--enable-native-access=ALL-UNNAMED")
            buildArgs.add("-H:IncludeResources=ui/.*")
        }
    }
}