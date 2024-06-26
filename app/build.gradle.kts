plugins {
    id("com.android.application")
}

android {
    namespace = "is.hi.afk6.hbv2"
    compileSdk = 34

    defaultConfig {
        applicationId = "is.hi.afk6.hbv2"
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
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            res {
                srcDirs("src\\main\\res", "src\\main\\res\\layouts\\LogIn",
                    "src\\main\\res",
                    "src\\main\\res\\layots\\waitingListRequest", "src\\main\\res", "src\\main\\res\\layouts\\waitingListRequest",
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\questionnaire", "src\\main\\res", "src\\main\\res\\layouts\\question",
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\user", "src\\main\\res", "src\\main\\res\\layouts\\questionAnswerGroup",
                    "src\\main\\res",
                    "src\\main\\res\\layouts\\signup", "src\\main\\res", "src\\main\\res\\layouts\\navigation"
                )
            }
        }
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.android.gms:play-services-location:latest_version")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("androidx.annotation:annotation:1.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}