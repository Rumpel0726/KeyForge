package ru.yarsu.keyforge.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.yarsu.keyforge.data.model.PasswordEntry

@Dao
interface PasswordDao {

    @Query("SELECT * FROM passwords ORDER BY createdAt DESC")
    fun getAll(): LiveData<List<PasswordEntry>>

    @Query("SELECT * FROM passwords WHERE id = :id")
    fun getById(id: Long): LiveData<PasswordEntry>

    @Query("SELECT * FROM passwords WHERE id = :id")
    suspend fun getByIdDirect(id: Long): PasswordEntry

    @Query("SELECT * FROM passwords WHERE title LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun search(query: String): LiveData<List<PasswordEntry>>

    @Insert
    suspend fun insert(entry: PasswordEntry)

    @Update
    suspend fun update(entry: PasswordEntry)

    @Delete
    suspend fun delete(entry: PasswordEntry)
}
