package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R
import com.google.android.material.button.MaterialButton

class FilterAdapter(var filterTypeList: List<String>,var brandData:List<String>,var fragment:Fragment):RecyclerView.Adapter<FilterAdapter.FilterTypeViewHolder>() {
    var highlightedPos = -1
    inner class FilterTypeViewHolder(filterTypeView:View):RecyclerView.ViewHolder(filterTypeView){
        val button = filterTypeView.findViewById<MaterialButton>(R.id.filterOptionsDiscount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterTypeViewHolder {
        return FilterTypeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.filter_button_option,parent,false))
    }

    override fun getItemCount(): Int {
        return filterTypeList.size
    }

    override fun onBindViewHolder(holder: FilterTypeViewHolder, position: Int) {
        holder.button.text = filterTypeList[position]
        if(highlightedPos==position){
            holder.button.setBackgroundColor(Color.WHITE)
        }
        else{
            holder.button.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.itemView.setOnClickListener {
            highlightedPos = holder.absoluteAdapterPosition
            resetViews()
            when(filterTypeList[position]){
                "Brand" -> {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.detailOptions,FilterFragmentSearch(brandData))
                        .commit()
                }
                "Price" -> {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.detailOptions,FilterPrice())
                        .commit()
                }
            }
        }

    }

    fun resetViews(){
        notifyDataSetChanged()
    }
}