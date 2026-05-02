package com.bbeniful.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.koin.core.annotation.Singleton

@Singleton
class Navigator(startDestination: Any) {
    val backStack : SnapshotStateList<Any> = mutableStateListOf(startDestination)

    fun goTo(destination: Any){
        backStack.add(destination)
    }

    fun goBack(){
        backStack.removeLastOrNull()
    }
}