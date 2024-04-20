package com.ristu.hometaskapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.ristu.hometaskapp.constant.Constant.Companion.CREATE_TODO
import com.ristu.hometaskapp.databinding.ActivityAddTodoBinding
import com.ristu.hometaskapp.db.TodoDao
import com.ristu.hometaskapp.db.TodoDatabase
import com.ristu.hometaskapp.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTodoBinding
    private lateinit var todoDb: TodoDatabase
    private lateinit var todoDao: TodoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_todo)
        initDb()
        todoDao = todoDb.getTodoDao()
        binding.apply {
            onItemClick = View.OnClickListener { view ->
                when (view.id) {
                    R.id.create_todo_button -> {
                        createTodo()
                    }
                }
            }
        }
    }

    private fun createTodo() {
        lifecycleScope.launch(Dispatchers.IO) {
            todoDao.createTodo(
                Todo(
                    title = binding.titleEditText.text.toString().trim(),
                    body = binding.bodyEditText.text.toString().trim(),
                )
            )
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddTodoActivity, "TODO created", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent().putExtra("isTodoCreated", true)
                setResult(CREATE_TODO, resultIntent)
                this@AddTodoActivity.finish()
            }
        }
    }

    private fun initDb() {
        todoDb = synchronized(this) {
            Room.databaseBuilder(
                applicationContext,
                TodoDatabase::class.java, "todo-database"
            ).build()
        }
    }
}