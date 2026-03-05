package ru.yarsu.keyforge.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.yarsu.keyforge.data.model.PasswordEntry
import ru.yarsu.keyforge.data.repository.PasswordRepository

class PasswordDetailViewModel(
    private val repository: PasswordRepository,
    entryId: Long
) : ViewModel() {

    val entry: LiveData<PasswordEntry> = repository.getById(entryId)

    val passwordVisible = MutableLiveData(false)

    private val _navigateBack = MutableLiveData(false)
    val navigateBack: LiveData<Boolean> = _navigateBack

    fun togglePasswordVisibility() {
        passwordVisible.value = !(passwordVisible.value ?: false)
    }

    fun delete() {
        val current = entry.value ?: return
        viewModelScope.launch {
            repository.delete(current)
            _navigateBack.postValue(true)
        }
    }
}
