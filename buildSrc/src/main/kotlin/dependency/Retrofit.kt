@file:Suppress("unused")

object Retrofit {
    /**
     * [Converter: Gson](https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson)
     * A dependency.Retrofit Converter which uses Gson for serialization.
     */
    const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit2}"

    /**
     * [dependency.Retrofit](https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit)
     * A type-safe HTTP client for Android and Java.
     */
    const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit2}"

    /**
     * [OkHttp Logging Interceptor](https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor)
     * Square’s meticulous HTTP client for Java and Kotlin.
     */
    const val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
}