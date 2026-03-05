package ru.yarsu.keyforge.ui.addedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.yarsu.keyforge.data.model.PasswordEntry
import ru.yarsu.keyforge.data.repository.PasswordRepository

class AddEditViewModel(
    private val repository: PasswordRepository,
    private val entryId: Long
) : ViewModel() {

    val title = MutableLiveData("")
    val username = MutableLiveData("")
    val password = MutableLiveData("")
    val url = MutableLiveData("")
    val note = MutableLiveData("")

    val isEditMode = entryId != -1L

    private val _navigateBack = MutableLiveData(false)
    val navigateBack: LiveData<Boolean> = _navigateBack

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var existingEntry: PasswordEntry? = null

    init {
        if (isEditMode) {
            viewModelScope.launch {
                val entry = repository.getByIdDirect(entryId)
                existingEntry = entry
                title.value = entry.title
                username.value = entry.username
                password.value = entry.password
                url.value = entry.url
                note.value = entry.note
            }
        }
    }

    fun save() {
        val t = title.value?.trim() ?: ""
        val u = username.value?.trim() ?: ""
        val p = password.value?.trim() ?: ""

        if (t.isEmpty() || u.isEmpty() || p.isEmpty()) {
            _errorMessage.value = "Заполните обязательные поля: название, логин, пароль"
            return
        }

        viewModelScope.launch {
            if (isEditMode && existingEntry != null) {
                repository.update(
                    existingEntry!!.copy(
                        title = t,
                        username = u,
                        password = p,
                        url = url.value?.trim() ?: "",
                        note = note.value?.trim() ?: ""
                    )
                )
            } else {
                repository.insert(
                    PasswordEntry(
                        title = t,
                        username = u,
                        password = p,
                        url = url.value?.trim() ?: "",
                        note = note.value?.trim() ?: ""
                    )
                )
            }
            _navigateBack.postValue(true)
        }
    }

    fun setGeneratedPassword(pwd: String) {
        password.value = pwd
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}
