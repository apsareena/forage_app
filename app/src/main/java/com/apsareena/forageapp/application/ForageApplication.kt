package com.apsareena.forageapp.application

import android.app.Application
import com.apsareena.forageapp.data.ItemRoomDatabase

class ForageApplication : Application() {
    // using by lazy so the database is only created when needed
    // rather than when the application starts
    val database: ItemRoomDatabase by lazy {
        ItemRoomDatabase.getDatabase(this)
    }
}