import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    //alias(libs.plugins.androidx.navigation.safe.args)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.israelaguilar.prohiringandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.israelaguilar.prohiringandroid"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Carga de la clave de la API de Maps desde local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        val mapsApiKey = localProperties.getProperty("MAPS_API_KEY") ?: ""
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    packagingOptions {
        resources {
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
            excludes += "google/protobuf/empty.proto"

        }
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit y Gson para consumir APIs
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Interceptor para logging en Retrofit
    implementation(libs.logging.interceptor)

    // Librerías para manejo de imágenes
    implementation(libs.glide)
    implementation(libs.picasso)

    // Corrutinas con alcance de lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Imágenes con bordes redondeados
    implementation(libs.roundedimageview)

    // Compatibilidad con versiones específicas
    implementation(libs.androidx.constraintlayout.v220)

    // SplashScreen API
    implementation(libs.androidx.core.splashscreen)

    // Firebase
    implementation(libs.core)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    // Google Maps
    implementation(libs.play.services.maps)

    // Dependencias de navegación
    implementation(libs.androidx.navigation.fragment.ktx.v273)
    implementation(libs.androidx.navigation.ui.ktx.v273)

    // Material Design
    implementation(libs.material.v121)

    // RecyclerView y otros componentes de la UI
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.appcompat.v151)

    // Safe Args para pasar datos entre fragmentos
    //implementation(libs.androidx.navigation.safe.args.gradle.plugin)

    // Dependencias para pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
