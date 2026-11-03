pluginManagement {
    repositories {
        maven(url = "https://maven.myket.ir")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://maven.myket.ir")
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "H2OHealthy"
include(":app")
include(":common")
include(":home")
include(":profile")
include(":auth")
include(":common:linearprogressbar")
