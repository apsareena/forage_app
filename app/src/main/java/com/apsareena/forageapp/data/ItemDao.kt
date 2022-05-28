package com.apsareena.forageapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item ORDER BY forage_Name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE forageId = :forageId")
    fun getItem(forageId: Int) : Flow<Item>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item:Item)

    @Delete
    suspend fun delete(item: Item)
}