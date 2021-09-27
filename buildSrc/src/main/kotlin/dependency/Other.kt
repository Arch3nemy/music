@file:Suppress("unused")

object Other {
    /**
     * [Material Components For Android](https://mvnrepository.com/artifact/com.google.android.material/material)
     * Material Components for Android is a static library that you can add to your Android application in order to use APIs that provide
     * implementations of the Material Design specification. Compatible on devices running API 14 or later.
     */
    const val material = "com.google.android.material:material:${Versions.material}"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"

    /**
     * [Timber](https://mvnrepository.com/artifact/com.jakewharton.timber/timber)
     * No-nonsense injectable logging.
     */
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    /**
     * [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime)
     */
    const val kotlinxDatetime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDatetime}"

    /**
     * [Core Kotlin Extensions](https://developer.android.com/kotlin/ktx#core)
     * Kotlin extensions for 'core' artifact
     */
    const val ktxCore = "androidx.core:core-ktx:${Versions.ktxCore}"

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

}