package com.example.randomstringgeneratorapp.feature


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomstringgeneratorapp.models.RandomStringData
import com.example.randomstringgeneratorapp.repo.RandomStringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class RandomStringViewModel @Inject constructor(application: Application)
    :  ViewModel() {

    private val repository = RandomStringRepository(application)

    private val _state = MutableStateFlow(RandomStringScreenState())
    val state: StateFlow<RandomStringScreenState>
        get() = _state.asStateFlow()


    //Generate random string
    fun fetchRandomString(maxLength: Int) {
        viewModelScope.launch {
            val newString = repository.fetchRandomString(maxLength)
            newString?.let {
                val updatedList = state.value.randomStrings + RandomStringData(value = it.value, length = it.length,
                    created = it.created
                )

                // Update state with the new list
                _state.value = state.value.copy(randomStrings = updatedList)

            }
        }
    }

    //As content provider was not accessible so Generated rando strings manually
    fun generateRandomString(length: Int) {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
         val randomString = (1..length)
            .map { chars.random() }
            .joinToString("")
        val updatedList = state.value.randomStrings + RandomStringData(value = randomString, length = length,
           created = Instant.now().toString()
        )

        // Update state with the new list
        _state.value = state.value.copy(randomStrings = updatedList)

    }


    // Delete all generated item
    fun deleteAllStrings() {
        _state.value = state.value.copy(randomStrings = emptyList())
    }

    // Delete specific item
    fun deleteString(index: Int) {
        val updatedList = state.value.randomStrings.toMutableList().apply {
            if (index in indices) removeAt(index)
        }
        _state.value = state.value.copy(randomStrings = updatedList)
    }
}

data class RandomStringScreenState(
    val randomStrings: List<RandomStringData> = mutableListOf()
)