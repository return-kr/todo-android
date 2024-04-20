package com.ristu.hometaskapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ristu.hometaskapp.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao
}