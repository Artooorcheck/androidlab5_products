package com.example.androidlab5_products.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab5_products.R

class IdentifierAdapter(var activity: Activity): RecyclerView.Adapter<IdentifierAdapter.ContactViewHolder>(){

    var items: List<Identifier> = listOf()

    private var onSelectItem: ((Long) -> Unit)? = null
    private var onDeleteItem: ((Long) -> Unit)? = null

    class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val deleteButton = view.findViewById<Button>(R.id.delete_button)
        val typeArea = view.findViewById<TextView>(R.id.type_area)
        val selectButton = view.findViewById<LinearLayout>(R.id.list_item)
    }

    fun updateList(newList: List<Identifier>) {
        items = newList
        activity.runOnUiThread{
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  items.count()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = items[position]
        holder.typeArea.setText(item.name)

        holder.deleteButton.setOnClickListener{
            onDeleteItem?.invoke(item.id)
        }

        holder.selectButton.setOnClickListener {
            onSelectItem?.invoke(item.id)
        }
    }

    fun setOnClickItemListener(action: (Long) -> Unit){
        onSelectItem = action
    }

    fun setOnDeleteItemListener(action: (Long) -> Unit){
        onDeleteItem = action
    }
}