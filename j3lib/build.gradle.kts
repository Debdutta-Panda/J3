plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    namespace = "com.debduttapanda.j3lib"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.fragment:fragment-ktx:1.8.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:2.8.1")
    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.02"))



    ///////
    api("androidx.appcompat:appcompat:1.7.0")
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    api("androidx.activity:activity-compose:1.9.2")
    api(platform("androidx.compose:compose-bom:2024.09.02"))
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-graphics")
    api("androidx.compose.ui:ui-tooling-preview")
    api("androidx.compose.material3:material3")
    api("androidx.fragment:fragment-ktx:1.8.3")
    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.2.1")
    androidTestApi("androidx.test.espresso:espresso-core:3.6.1")
    androidTestApi("androidx.compose.ui:ui-test-junit4")
    debugApi("androidx.compose.ui:ui-tooling")
    debugApi("androidx.compose.ui:ui-test-manifest")
    api("androidx.navigation:navigation-compose:2.8.1")
    api("com.airbnb.android:lottie-compose:6.1.0")
    api("com.google.accompanist:accompanist-permissions:0.33.2-alpha")
    api("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")
    androidTestApi(platform("androidx.compose:compose-bom:2024.09.02"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.debduttapanda"
            artifactId = "j3"
            version = "1.1.35"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
