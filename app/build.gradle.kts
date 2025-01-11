import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)

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

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if(localPropertiesFile.exists()){
            localProperties.load(localPropertiesFile.inputStream())
        }

        val mapsApiKey = localProperties.getProperty("MAPS_API_KEY")

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

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

    //Para retrofit y Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Adicional para el interceptor
    implementation(libs.logging.interceptor)

    //Glide y Picasso
    implementation(libs.glide)
    implementation(libs.picasso)

    //Para las corrutinas con alcance lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Imágenes con bordes redondeados
    implementation(libs.roundedimageview)

    implementation (libs.androidx.constraintlayout.v220)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.core)
    implementation(libs.firebase.auth)

    implementation(libs.play.services.maps)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    //implementation(libs.material.v1110)
    //implementation(libs.androidx.navigation.fragment)

    // Dependencias de navegación
    implementation (libs.androidx.navigation.fragment.ktx.v273)
    implementation (libs.androidx.navigation.ui.ktx.v273) // For BottomNavigationView or Toolbar integration
    implementation (libs.material.v121)
    implementation(libs.androidx.leanback)

    // Para RecyclerView y otros componentes de la UI
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.appcompat.v151)
    implementation (libs.androidx.constraintlayout.v220)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}