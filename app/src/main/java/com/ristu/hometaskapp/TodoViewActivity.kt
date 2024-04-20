package com.ristu.hometaskapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ristu.hometaskapp.databinding.ActivityTodoViewBinding

class TodoViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_view)

    }
}