pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ProgressTrackerGYM"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":data")
include(":feature:home:api:nav")
include(":feature:home:impl")
include(":feature:add:impl")
include(":feature:add:api:nav")
include(":feature:progress:impl")
include(":feature:progress:api:nav")
include(":navigation")
include(":domain")
include(":core:design")
include(":feature:exercise:api:nav")
include(":feature:exercise:impl")
include(":feature:settings:api:nav")
include(":feature:settings:impl")
include(":feature:statistic:api:nav")
include(":feature:statistic:impl")
