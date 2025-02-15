plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tenpm_hrm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tenpm_hrm"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.recyclerview)
    implementation(libs.volley)
    implementation(libs.play.services.location)
    implementation("com.sun.mail:android-activation:1.6.2")
    implementation("com.sun.mail:android-mail:1.6.2")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

}
