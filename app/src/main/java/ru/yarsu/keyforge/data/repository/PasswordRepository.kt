package ru.yarsu.keyforge.data.repository

import androidx.lifecycle.LiveData
import ru.yarsu.keyforge.data.db.PasswordDao
import ru.yarsu.keyforge.data.model.PasswordEntry

class PasswordRepository(private val dao: PasswordDao) {

    val allEntries: LiveData<List<PasswordEntry>> = dao.getAll()

    fun getById(id: Long): LiveData<PasswordEntry> = dao.getById(id)

    suspend fun getByIdDirect(id: Long): PasswordEntry = dao.getByIdDirect(id)

    fun search(query: String): LiveData<List<PasswordEntry>> = dao.search(query)

    suspend fun insert(entry: PasswordEntry) = dao.insert(entry)

    suspend fun update(entry: PasswordEntry) = dao.update(entry)

    suspend fun delete(entry: PasswordEntry) = dao.delete(entry)
}
