package com.example.mytodolist.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.databinding.TodoItemBinding
import com.example.mytodolist.model.TodoListData
import java.util.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private lateinit var todoItemBinding: TodoItemBinding
    var listData = mutableListOf<TodoListData>()
    var temp = mutableListOf<TodoListData>()
    private lateinit var context : Context
    private val checkBoxStatus = SparseBooleanArray()

    inner class TodoViewHolder(var todoItemBinding: TodoItemBinding) : RecyclerView.ViewHolder(todoItemBinding.root) {
        private var position : Int? = null
        var checkselected : CheckBox = todoItemBinding.checkBox
        var todoT : TextView = todoItemBinding.todoText

        fun bind(data : TodoListData, position: Int) {
            this.position = position
            todoItemBinding.todoText.text = data.content

            checkselected.isChecked = checkBoxStatus[adapterPosition]
            checkselected.isChecked = data.isChecked

            if (data.isChecked) {
                todoT.paintFlags = todoT.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                todoT.paintFlags = todoT.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            /*checkselected.setOnClickListener {
                if (!checkselected.isChecked) {
                    checkBoxStatus.put(adapterPosition,false)
                } else {
                    checkBoxStatus.put(adapterPosition,true)
                }
                notifyItemChanged(adapterPosition)
            }*/
            checkselected.setOnClickListener {
                itemCheckBoxClickListener.onClick(it,layoutPosition,listData[layoutPosition].id)
            }

            todoItemBinding.root.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,listData[layoutPosition].id)
            }

            todoItemBinding.removeIv.setOnClickListener {
                val builder : AlertDialog.Builder = AlertDialog.Builder(context)
                val ad : AlertDialog = builder.create()
                val deleteData = listData[this.layoutPosition].content
                builder.setTitle(deleteData)
                builder.setMessage("????????? ?????????????????????????")

                builder.setNegativeButton("???",
                    DialogInterface.OnClickListener { dialog, which ->
                        ad.dismiss()
                        removeData(this.layoutPosition)
                    })

                builder.setPositiveButton("?????????",
                    DialogInterface.OnClickListener { dialog, which ->
                        ad.dismiss()
                    })
                builder.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        var inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        todoItemBinding = TodoItemBinding.inflate(inflater, parent, false)

        return TodoViewHolder(todoItemBinding)
    }
    //checkboxstatus?????? indexoutofboundsexception
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(listData[position], position)
        //holder.checkselected.isChecked

        /*holder.checkselected.setOnClickListener {
            if (!holder.checkselected.isChecked) {
                checkBoxStatus.put(position,false)
                todoItemBinding.todoText.paintFlags = todoItemBinding.todoText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            } else {
                checkBoxStatus.put(position,true)
                todoItemBinding.todoText.paintFlags = todoItemBinding.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            notifyItemChanged(position)
        }*/
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    //????????? Handle ??????
    fun removeData(position: Int) {
        listData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun swapData(beforePos : Int, afterPos : Int) {
        Collections.swap(listData, beforePos, afterPos)
        notifyItemMoved(beforePos,afterPos)
    }

    fun update(newList: TodoListData) {
        temp.add(newList)
        this.listData = temp
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    interface ItemCheckBoxClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    private lateinit var itemClickListener: ItemClickListener
    private lateinit var itemCheckBoxClickListener: ItemCheckBoxClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun setItemCheckBoxClickListener(itemCheckBoxClickListener: ItemCheckBoxClickListener) {
        this.itemCheckBoxClickListener = itemCheckBoxClickListener
    }
}