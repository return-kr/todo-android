package com.ristu.hometaskapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.ristu.hometaskapp.constant.Constant
import com.ristu.hometaskapp.databinding.ActivityTodoViewBinding
import com.ristu.hometaskapp.db.TodoDao
import com.ristu.hometaskapp.db.TodoDatabase
import com.ristu.hometaskapp.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoViewBinding
    private var uid: Long? = null
    private lateinit var todoDb: TodoDatabase
    private lateinit var todoDao: TodoDao
    private lateinit var todo: Todo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_view)
        uid = intent.getLongExtra("UID", 0)
        initDb()
        todoDao = todoDb.getTodoDao()
        getTodo()
        binding.apply {
            onItemClick = View.OnClickListener { view ->
                when (view.id) {
                    R.id.save_button -> {
                        updateTodo()
                    }
                }
            }
        }
    }

    private fun updateTodo() {
        lifecycleScope.launch(Dispatchers.IO) {
            binding.apply {
                todoDao.updateTodo(
                    title = titleEditText.text.toString(),
                    body = bodyEditText.text.toString(),
                    uid = uid!!
                )
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@TodoViewActivity, "TODO updated", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent().putExtra("isTodoUpdated", true)
                setResult(Constant.UPDATE_TODO, resultIntent)
                this@TodoViewActivity.finish()
            }
        }
     }

    private fun getTodo() {
        lifecycleScope.launch(Dispatchers.IO) {
            todo = todoDao.getTodo(uid!!)
            withContext(Dispatchers.Main) {
                binding.apply {
                    titleEditText.setText(todo.title.toString())
                    bodyEditText.setText(todo.body.toString())
                }
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