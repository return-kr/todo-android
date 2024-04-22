package com.ristu.hometaskapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ristu.hometaskapp.model.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM all_todo")
    fun getAllTodo(): List<Todo>

    @Query("SELECT * FROM all_todo WHERE uid = :uid")
    fun getTodo(uid: Long): Todo

    @Query("UPDATE all_todo SET title = :title, body = :body WHERE uid = :uid")
    fun updateTodo(title: String, body: String, uid: Long)

    @Insert
    fun createTodo(user: Todo)

    @Query("DELETE FROM all_todo WHERE uid = :uid")
    fun deleteTodo(uid: Long)
}