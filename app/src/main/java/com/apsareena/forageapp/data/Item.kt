package com.apsareena.forageapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate=true)
    val forageId : Int = 0,
    @ColumnInfo(name = "forage_name")
    val itemName: String,
    @ColumnInfo(name="forage_location")
    val itemLocation: String,
    @ColumnInfo(name="forage_season")
    val itemSeason: Boolean,
    @ColumnInfo(name = "forage_notes")
    val itemNotes: String
)


