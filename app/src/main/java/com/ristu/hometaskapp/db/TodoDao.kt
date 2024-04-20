package com.ristu.hometaskapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ristu.hometaskapp.model.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM all_todo")
    fun getAllTodo(): List<Todo>

    @Insert
    fun createTodo(user: Todo)

    @Query("DELETE FROM all_todo WHERE uid = :uid")
    fun deleteTodo(uid: Long)
}