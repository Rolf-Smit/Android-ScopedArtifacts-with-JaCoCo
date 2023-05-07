import com.android.build.api.variant.ScopedArtifacts
import com.android.build.api.artifact.ScopedArtifact

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("jacoco")
}

abstract class GetAllClassesTask : DefaultTask() {

    @get:InputFiles
    abstract val allDirectories: ListProperty<Directory>

    @get:InputFiles
    abstract val allJars: ListProperty<RegularFile>

    @TaskAction
    fun action() {
        println("directories: ${allDirectories.get().joinToString()}")
        if(allDirectories.get().any { it.toString().endsWith("/jacocoDebug/dirs")}) {
            error("Provided directories contain jacoco instrumented classes!")
        }
    }
}


android {
    namespace = "org.neotech.example.scopedartifact"
    compileSdk = 33

    defaultConfig {
        applicationId = "org.neotech.example.scopedartifact"
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

androidComponents {
    onVariants { variant ->
        val taskProvider = project.tasks.register<GetAllClassesTask>("${variant.name}GetAllClasses")
        taskProvider.configure {
            group = "example"
        }
        variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
            .use(taskProvider)
            .toGet(
                ScopedArtifact.CLASSES,
                GetAllClassesTask::allJars,
                GetAllClassesTask::allDirectories
            )
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}