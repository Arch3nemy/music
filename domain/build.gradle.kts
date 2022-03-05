plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    core()
    di()
    gson()
    async()
    reflection()
}

fun DependencyHandlerScope.core() {
    implementation(Dependencies.other.kotlin)
}

fun DependencyHandlerScope.di() {
    api(Dependencies.di.javaxInject)
}

fun DependencyHandlerScope.async() {
    implementation(Dependencies.async.coroutinesCore)
}

fun DependencyHandlerScope.reflection() {
    implementation(Dependencies.other.reflection)
}

fun DependencyHandlerScope.gson() {
    implementation(Dependencies.other.gson)
}
