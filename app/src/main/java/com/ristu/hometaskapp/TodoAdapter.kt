package com.ristu.hometaskapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ristu.hometaskapp.databinding.EmptyTodoLayoutBinding
import com.ristu.hometaskapp.databinding.TodoItemBinding
import com.ristu.hometaskapp.model.Todo

class TodoAdapter(private val onClickDelete: (uid: Long) -> Unit) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var itemList: List<Todo>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setTodoData(itemList: List<Todo>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                titleTextView.text = itemList?.get(adapterPosition)?.title
                bodyTextView.text = itemList?.get(adapterPosition)?.body
                onItemClick = View.OnClickListener { view ->
                    when (view.id) {
                        R.id.delete_todo_button -> {
                            itemList?.get(adapterPosition)?.uid?.let { onClickDelete(it) }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }


    override fun getItemCount() = itemList?.size ?: 0
}
