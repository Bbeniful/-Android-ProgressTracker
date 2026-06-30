### MVI Pattern
- Model -> View -> Intent
- unidirectional flow


### State
- state contains only UI related properties
- we have two type of states
- Data class:
```kotlin
data class UIState(
    val isLoading: Boolean = false,
    val data: Type? = null,
    val error: Error? = null)
```
- Sealed class:

```kotlin
sealed class UIState {
    data object Initial: UIState()
    data object Loading : UIState()
    data class Success(val data: Data) : UIState()
    data class Error(val error: Error) : UIState()
}

data class Data(
    val list: List<Type>
)
```
- state definition in viewmodel:
```kotlin
private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
val uiState = _uiState.asStateFlow()
```
- if the project has kotlin 2.3+ version, you need to use explicit backing fields
```kotlin
val uiState: StateFlow<UIState>
     field = MutableStateFlow<UIState>(UIState.Initial)
```
- the necessary code you can find in `./gradle/skills.md`




### Updating state
- you need to prefer thread safe
- Updating state in data class
```kotlin
fun updateLoading(isLoading: Boolean) = uiState.update {
        it.copy(isLoading = isLoading)
    }

```
- Updating sealed class state
```kotlin
fun updateLoading() = uiState.update {
        UIState.Loading
    }
```

### Intent
- intents names should be: UserNameChanged
- example intent
```kotlin
sealed interface Intent {
    data class UserNameChanged(val name: String): Intent
    data object SubmitButtonPressed: Intent
}
```
- intents is a one time event.
- example in viewModel
```kotlin
private val _intent = MutableSharedFlow<Intent>()

init {
    subscribeToIntent()
}

private fun subscribeToIntent() {
    viewModelScope.launch {
        _intent.collect { intent ->
            handleIntent(intent = intent)
        }
    }
}

private fun handleIntent(intent: Intent) = when(intent) {}


fun setIntent(intent: Intent) {
    viewModelScope.launch {
        _intent.emit(intent)
    }
}
```
- UI should never call any function directly from viewmodel only the setIntent()

### Observe on the UI
- UI just observes the data from viewModel, but never change it directly
- UI can call only the setIntent function to communicate to viewModel
```kotlin
val viewModel = viewModel()
val state by viewModel.uiState.collectAsStateWithLifecycle()
```

- init call
```kotlin
LaunchedEffect(Unit) {
    viewModel.setIntent(Intent.LoadData)
}
```