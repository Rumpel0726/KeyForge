package ru.yarsu.keyforge.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.yarsu.keyforge.data.repository.PasswordRepository

class AddEditViewModelFactory(
    private val repository: PasswordRepository,
    private val entryId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditViewModel(repository, entryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
