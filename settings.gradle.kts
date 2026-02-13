pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KPlayer"
include(":app")
include(":core:common")
include(":core:media")
include(":core:data")
include(":core:domain")
include(":feature:videopicker")
include(":feature:player")
include(":feature:setting")
