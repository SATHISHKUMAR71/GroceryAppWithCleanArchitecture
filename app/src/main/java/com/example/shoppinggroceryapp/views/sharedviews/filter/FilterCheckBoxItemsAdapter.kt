package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R

class FilterCheckBoxItemsAdapter(var items:List<String>, var isChecked:List<Boolean>):RecyclerView.Adapter<FilterCheckBoxItemsAdapter.FilterCheckBoxHolder>() {

    inner class FilterCheckBoxHolder(checkBoxHolder:View):RecyclerView.ViewHolder(checkBoxHolder){
        val isItemChecked =checkBoxHolder.findViewById<CheckBox>(R.id.fragmentOptionCheckbox)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FilterCheckBoxHolder {
        return FilterCheckBoxHolder(LayoutInflater.from(parent.context).inflate(R.layout.filter_checkbox,parent,false))
    }

    override fun onBindViewHolder(holder: FilterCheckBoxHolder, position: Int) {
        if(holder.absoluteAdapterPosition==position) {
            holder.isItemChecked.text = items[position]
            holder.isItemChecked.isChecked = isChecked[position]
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(itemsText:List<String>,isNewCheckedList:List<Boolean>){
        items = itemsText
        isChecked = isNewCheckedList
        notifyDataSetChanged()
    }

}