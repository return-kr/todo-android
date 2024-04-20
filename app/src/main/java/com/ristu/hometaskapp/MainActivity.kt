package com.ristu.hometaskapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room.databaseBuilder
import com.ristu.hometaskapp.constant.Constant.Companion.CREATE_TODO
import com.ristu.hometaskapp.databinding.ActivityMainBinding
import com.ristu.hometaskapp.db.TodoDao
import com.ristu.hometaskapp.db.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TodoAdapter
    private lateinit var todoDb: TodoDatabase
    private lateinit var todoDao: TodoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initValues()
        initDb()
        todoDao = todoDb.getTodoDao()
        getTodo()
        binding.apply {
            onItemClick = View.OnClickListener { view ->
                when (view.id) {
                    R.id.add_todo_button -> {
                        startActivityForResult(Intent(this@MainActivity, AddTodoActivity::class.java), 999)
                    }
                }
            }
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.appcompat.app.AppCompatActivity"
        )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == CREATE_TODO && data?.getBooleanExtra("isTodoCreated", false) == true) {
            getTodo()
        }
    }

    private fun getTodo() {
        lifecycleScope.launch(Dispatchers.IO) {
            val allTodo = todoDao.getAllTodo()
            withContext(Dispatchers.Main) {
                if (allTodo.isNotEmpty()) {
                    binding.isEmptyTodo = false
                    adapter.setTodoData(allTodo)
                } else {
                    binding.isEmptyTodo = true
                }
            }
        }
    }

    private fun initValues() {
        adapter = TodoAdapter {uid ->
            lifecycleScope.launch(Dispatchers.IO) {
                todoDao.deleteTodo(uid)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "TODO Deleted", Toast.LENGTH_SHORT).show()
                    getTodo()
                }
            }
        }
        binding.apply {
            todoRecyclerView.adapter = adapter
            todoRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun initDb() {
        todoDb = synchronized(this) {
            databaseBuilder(
                applicationContext,
                TodoDatabase::class.java, "todo-database"
            ).build()
        }
    }
}