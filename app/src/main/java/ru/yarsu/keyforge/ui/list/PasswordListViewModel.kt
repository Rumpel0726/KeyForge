package ru.yarsu.keyforge.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.yarsu.keyforge.data.db.AppDatabase
import ru.yarsu.keyforge.data.model.PasswordEntry
import ru.yarsu.keyforge.data.repository.PasswordRepository

class PasswordListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PasswordRepository
    val allEntries: LiveData<List<PasswordEntry>>

    init {
        val dao = AppDatabase.getDatabase(application).passwordDao()
        repository = PasswordRepository(dao)
        allEntries = repository.allEntries
    }

    fun delete(entry: PasswordEntry) {
        viewModelScope.launch {
            repository.delete(entry)
        }
    }
}
