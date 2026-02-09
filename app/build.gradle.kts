plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "com.example.niundiagratis"
    compileSdk = 35
    buildTypes {
        getByName("release") {
            // Así es como configuras crunchPngs, no accedes a isCrunchPngs directamente
            isCrunchPngs = true // o false
        }
    }

    defaultConfig {
        applicationId = "com.niundiagratis"
        minSdk = 29
        targetSdk = 35
        versionCode = 7
        versionName = "1.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8) // O JVM_11, JVM_17, etc.
            // Aquí puedes añadir otras opciones del compilador si las necesitas
            // freeCompilerArgs.add("-Xcompiler-argument")
        }
    }
    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
}
kapt {
    correctErrorTypes = true
    //if (JavaVersion.current().isJava9Compatible) {
        /*
        val javacArgs = listOf(
            "--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            // ... incluye todas las líneas --add-opens de arriba
            "--add-opens=jdk.compiler/com.sun.tools.javac.zip=ALL-UNNAMED"
        )*/
        // Para Kotlin DSL
        // javacOptions { options.addAll(javacArgs.map { JavacOption.option(it) }) }
    //}
}

dependencies {

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:2.2.0-2.0.2")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.fragment:fragment-ktx:1.8.8")
    //implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
    implementation("com.google.firebase:firebase-crashlytics:20.0.0")
    //implementation("com.android.support:support-annotations:28.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    dependencies {
        //No tocar versiones
        implementation("androidx.core:core-ktx:1.16.0")
        implementation("androidx.appcompat:appcompat:1.7.1")

        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.2.1")

        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")
        implementation("androidx.lifecycle:lifecycle-common-java8:2.9.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")

        implementation("androidx.navigation:navigation-fragment-ktx:2.9.1")
        implementation("androidx.navigation:navigation-ui-ktx:2.9.1")
        implementation("androidx.activity:activity-ktx:1.10.1")
        //hasta aqui
        val roomVersion = "2.7.2"
        implementation("androidx.room:room-runtime:$roomVersion")
        ksp("androidx.room:room-compiler:$roomVersion")
        implementation("androidx.room:room-ktx:$roomVersion")
        ksp("androidx.room:room-testing:$roomVersion")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

        implementation("androidx.recyclerview:recyclerview:1.4.0")
        implementation("androidx.recyclerview:recyclerview-selection:1.2.0")

       

        //implementation("")

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    }
}