plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = Dependencies.android.compileSdk
    buildToolsVersion = Dependencies.android.buildTools

    defaultConfig {
        applicationId = "com.alacrity.alacritybet"
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
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

    buildFeatures {
        compose  = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    core()
    material()
    di()
    room()
    async()
    jetpack()
    compose()
}

fun DependencyHandlerScope.core() {
    implementation(Dependencies.other.kotlin)
    implementation(Dependencies.other.ktxCore)
    implementation(Dependencies.other.kotlinxDatetime)
    implementation(Dependencies.other.appcompat)
    implementation(Dependencies.other.constraintLayout)
}

fun DependencyHandlerScope.material() {
    implementation(Dependencies.other.material)
}

fun DependencyHandlerScope.room() {
    implementation(Dependencies.room.runtime)
    kapt(Dependencies.room.compiler)
    implementation(Dependencies.room.ktx)
}

fun DependencyHandlerScope.di() {
    implementation(Dependencies.di.dagger2)
    kapt(Dependencies.di.dagger2compiler)
}

fun DependencyHandlerScope.retrofit() {
    implementation(Dependencies.retrofit.retrofit2)
    implementation(Dependencies.retrofit.converterGson)
    implementation(Dependencies.retrofit.interceptor)
}

fun DependencyHandlerScope.imageLoading() {
    implementation(Dependencies.image.glide)
    annotationProcessor(Dependencies.image.glideCompiler)
}

fun DependencyHandlerScope.async() {
    implementation(Dependencies.async.coroutinesCore)
    implementation(Dependencies.async.coroutinesAndroid)
}

fun DependencyHandlerScope.jetpack() {
    implementation(Dependencies.jetpack.lifecycleExtensions)
    implementation(Dependencies.jetpack.lifecycleViewModel)
}

fun DependencyHandlerScope.compose() {
    implementation(Dependencies.compose.runtime)
    implementation(Dependencies.compose.ui)
    implementation(Dependencies.compose.material)
    implementation(Dependencies.compose.icons)
    implementation(Dependencies.compose.foundationLayout)
    implementation(Dependencies.compose.foundation)
    implementation(Dependencies.compose.animation)
    implementation(Dependencies.compose.uiTooling)
    implementation(Dependencies.compose.liveData)
    implementation(Dependencies.compose.activity)
    implementation(Dependencies.compose.uiController)
    implementation(Dependencies.compose.navigation)
    implementation(Dependencies.compose.insets)
}

