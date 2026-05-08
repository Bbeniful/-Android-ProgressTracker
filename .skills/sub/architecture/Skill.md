### Create clean architecture

- Creating clean architecture is very important. It helps us to have a scalable and easy to read architecture with great separation of concerns. 
You are also very passionate to follow these principles in every layer
- If any dependencies are missing, add them, use the version catalog file, more information in `../dependency/skill.md`
- After you finished, run the unit tests
- once you added all module, you should run gradle sync

- # Example of the feature architecture
```
featureName/
├── api/
│   ├── nav/
│   └── domain/ note: (if need it for use cases that will be used not just here, this contains the interface and the `{featureName}:impl:domain` usecase folder will contain its impl)
└── impl/
    ├── data/
    ├── domain/
    └── presentation/
```

# Data layer
- Data layer should contain the impl for repository, data sources and all mappers between domain and data.
- Data layer can contain platform dependencies
- All code here also should be unit tested by using mockk and kotest framework. Read more about testing in `../testing/skill.md` file

# Domain layer
- Domain layer contains mostly interfaces and the use case classes, data models, the business logic itself
- This code should not have any platform dependency
- Unit testing should be done by just using fake objects without mocking

# Presentation layer
- This is the UI layer, it contains the ui states, intents, viewModels and the ui itself
- Compose UI should be split in small pieces to have better performance, recomposition scope
- State should follow the pattern:
```kotlin
@Immutable
data class `{RelatedFeatureName}`State(
    val isLoading: Boolean = false,
    val data: Type? = null,
    val error: CustomError? = null
)
```
- Intent or events should follow this pattern:
```kotlin
sealed interface `{RelatedFeatureName}Intent` {
    data class IfHasArgument(val data: T): `{RelatedFeatureName}Intent`
    data object IfHasNoArgument: `{RelatedFeatureName}Intent`
}

```
- ViewModel should be tested 100% without mocking framework only with fakes and use turbine for testing flows