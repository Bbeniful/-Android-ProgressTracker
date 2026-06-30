---
name: unit_testing
description: Unit testing conventions — behavior-not-implementation, test doubles vs mocking frameworks per layer, Kotest BehaviorSpec, JUnit 5/6, Google Truth, Turbine for flows, and MockK only for the data layer. Use whenever writing, fixing, or reviewing unit tests for use cases, ViewModels, repositories, or data sources.
---

### Unit testing
- Unit test should never call any UI related code like compose or view from android system
- Unit tests should test the behavior not the implementation
- Unit tests name should clearly tell its purpose
- if you test something from domain or presentation layer, like use cases or viewModel, never use mocking frameworks
prefer test doubles like stub, mock or fake classes
- for data layer, you can use mocking frameworks

### Unit testing dependencies
- for unit testing we can use:
  - Google truth
  - Kotest
  - Junit5 or 6
- for mocking we use
  - mocKK
- for flows
  - turbine
### Example Unit test
- Google truth
```kotlin
class UserViewModelTest {
  private lateinit var fakeRepository: FakeUserRepository
  private lateinit var viewModel: UserViewModel

  @Before
  fun setup() {
    fakeRepository = FakeUserRepository()
    viewModel = UserViewModel(fakeRepository)
  }

  @Test
  fun loadUser_success() = runTest {
    viewModel.state.test {
      assertThat(awaitItem()).isEqualTo(UserState())

      viewModel.handleIntent(UserIntent.LoadUser("123"))
      val loadingState = awaitItem()
      assertThat(loadingState.isLoading).isTrue()

      val successState = awaitItem()
      assertThat(successState.name).isEqualTo("John Doe")
      assertThat(successState.isLoading).isFalse()
      assertThat(successState.error).isNull()
    }
  }
}
```
- Kotest
```kotlin
class UserViewModelTest : BehaviorSpec({
    val fakeRepository = FakeUserRepository()
    val viewModel = UserViewModel(fakeRepository)
 
    given("a user view model") {
        `when`("loading a user succeeds") {
            then("should emit loading then success state") {
                runTest {
                    viewModel.state.test {
                        assertThat(awaitItem()).isEqualTo(UserState())
 
                        viewModel.handleIntent(UserIntent.LoadUser("123"))
                        val loadingState = awaitItem()
                        assertThat(loadingState.isLoading).isTrue()
 
                        val successState = awaitItem()
                        assertThat(successState.name).isEqualTo("John Doe")
                        assertThat(successState.isLoading).isFalse()
                        assertThat(successState.error).isNull()
                    }
                }
            }
        }
 
        `when`("loading a user fails") {
            then("should emit loading then error state") {
                runTest {
                    fakeRepository.shouldFail = true
                    fakeRepository.failureMessage = "User not found"
 
                    viewModel.state.test {
                        awaitItem() // initial state
 
                        viewModel.handleIntent(UserIntent.LoadUser("999"))
                        awaitItem() // loading state
 
                        val errorState = awaitItem()
                        assertThat(errorState.error).contains("User not found")
                        assertThat(errorState.isLoading).isFalse()
                    }
                }
            }
        }
    }
})
```

- junit6
```kotlin
class UserViewModelTest {
    private lateinit var fakeRepository: FakeUserRepository
    private lateinit var viewModel: UserViewModel
 
    @BeforeEach
    fun setup() {
        fakeRepository = FakeUserRepository()
        viewModel = UserViewModel(fakeRepository)
    }
 
    @Test
    suspend fun loadUser_success() {
        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(UserState())
 
            viewModel.handleIntent(UserIntent.LoadUser("123"))
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()
 
            val successState = awaitItem()
            assertThat(successState.name).isEqualTo("John Doe")
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.error).isNull()
        }
    }
 
    @Test
    suspend fun loadUser_failure() {
        fakeRepository.shouldFail = true
        fakeRepository.failureMessage = "User not found"
 
        viewModel.state.test {
            awaitItem() // initial state
 
            viewModel.handleIntent(UserIntent.LoadUser("999"))
            awaitItem() // loading state
 
            val errorState = awaitItem()
            assertThat(errorState.error).contains("User not found")
            assertThat(errorState.isLoading).isFalse()
        }
    }
}
```

