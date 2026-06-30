---
name: viewmodel_guidance
description: ViewModel implementation rules — presentation-layer responsibility, business-logic-only, MVI intent handling, runCatching error handling, and separate private state-update functions (updateLoading/updateSuccess/updateError). Use whenever writing or reviewing a ViewModel.
---

### ViewModel
- its is part of the presentation layer
- it should never contain UI logic, only business logic
- it follows MVI `./architectrure/mvi.md`

- Example:
```kotlin
sealed class PostsUiState {
    data object Initial : PostsUiState()
    data object Loading : PostsUiState()
    data class Success(val posts: List<String>) : PostsUiState()
    data class Error(val message: String) : PostsUiState()
}


sealed interface PostsIntent {
    data object LoadPosts : PostsIntent
    data object Retry : PostsIntent
}

class PostsViewModel : ViewModel() {
    val uiState: StateFlow<PostsUiState>
        field = MutableStateFlow<PostsUiState>(PostsUiState.Initial)

    private val _intent = MutableSharedFlow<PostsIntent>()

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

    private suspend fun handleIntent(intent: PostsIntent) = when (intent) {
        is PostsIntent.LoadPosts -> loadPosts()
        is PostsIntent.Retry -> loadPosts()
    }

    private suspend fun loadPosts() {
        updateLoading()
        runCatching {
            val posts = fetchPosts()
            updateSuccess(posts)
        }.onFailure { e ->
            updateError(e.message ?: "Unknown error")
        }
    }

    private suspend fun fetchPosts(): List<String> {
        // Real API call here
        delay(1000)
        return listOf("Post 1", "Post 2", "Post 3")
    }

    private fun updateLoading() {
        uiState.update { PostsUiState.Loading }
    }

    private fun updateSuccess(posts: List<String>) {
        uiState.update { PostsUiState.Success(posts) }
    }

    private fun updateError(message: String) {
        uiState.update { PostsUiState.Error(message) }
    }

    fun setIntent(intent: PostsIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }
}
```
