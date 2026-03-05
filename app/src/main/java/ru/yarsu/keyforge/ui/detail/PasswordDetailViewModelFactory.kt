package ru.yarsu.keyforge.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.yarsu.keyforge.data.repository.PasswordRepository

class PasswordDetailViewModelFactory(
    private val repository: PasswordRepository,
    private val entryId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordDetailViewModel(repository, entryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
