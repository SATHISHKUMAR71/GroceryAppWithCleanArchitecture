package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R

class FilterCheckBoxItemsAdapter(var items:List<String>, var isChecked:List<Boolean>,var isDiscount:Boolean):RecyclerView.Adapter<FilterCheckBoxItemsAdapter.FilterCheckBoxHolder>() {


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
            println("980123 ${items[position]} is checked ${FilterFragmentSearch.checkedList}")
            if(isDiscount) {
                println("98982  on else ${FilterFragmentSearch.checkedDiscountList}")
                when(items[position]){
                    "10% or more" -> {
                        checkDiscounts(holder,position,10f)
                    }
                    "20% or more" -> {
                        checkDiscounts(holder,position,20f)
                    }
                    "30% or more" -> {
                        checkDiscounts(holder,position,30f)
                    }
                    "40% or more" -> checkDiscounts(holder,position,40f)
                    "50% or more" -> checkDiscounts(holder,position,50f)
                }

            }
            else{
//                println("98982  on else ${FilterFragmentSearch.checkedDiscountList}")
//                when(items[position]){
//                    "10% or more" -> {
//                        checkDiscounts(holder,position,10f)
//                    }
//                    "20% or more" -> {
//                        checkDiscounts(holder,position,20f)
//                    }
//                    "30% or more" -> {
//                        checkDiscounts(holder,position,30f)
//                    }
//                    "40% or more" -> checkDiscounts(holder,position,40f)
//                    "50% or more" -> checkDiscounts(holder,position,50f)
//                }
                println("98982  on else ${FilterFragmentSearch.checkedDiscountList}")
                if (items[position] in FilterFragmentSearch.checkedList) {
                    println("980123 980123 980123 980123 980123 980123  is checked called for ${items[position]}")
                    holder.isItemChecked.isChecked = true
                } else {
                    holder.isItemChecked.isChecked = false
                }

            }

            holder.isItemChecked.setOnClickListener {

                if(holder.isItemChecked.isChecked){
                    if(isDiscount){
                        when(holder.isItemChecked.text.toString()) {
                            "10% or more" -> FilterFragmentSearch.checkedDiscountList.add(10f)
                            "20% or more" -> FilterFragmentSearch.checkedDiscountList.add(20f)
                            "30% or more" -> FilterFragmentSearch.checkedDiscountList.add(30f)
                            "40% or more" -> FilterFragmentSearch.checkedDiscountList.add(40f)
                            "50% or more" -> FilterFragmentSearch.checkedDiscountList.add(50f)
                        }
                    }
                    else{
                        FilterFragmentSearch.checkedList.add(holder.isItemChecked.text.toString())
                    }
                }
                else{
                    if(isDiscount){
                        when(holder.isItemChecked.text.toString()) {
                            "10% or more" -> FilterFragmentSearch.checkedDiscountList.remove(10f)
                            "20% or more" -> FilterFragmentSearch.checkedDiscountList.remove(20f)
                            "30% or more" -> FilterFragmentSearch.checkedDiscountList.remove(30f)
                            "40% or more" -> FilterFragmentSearch.checkedDiscountList.remove(40f)
                            "50% or more" -> FilterFragmentSearch.checkedDiscountList.remove(50f)
                        }
                    }
                    else {
                        FilterFragmentSearch.checkedList.remove(holder.isItemChecked.text.toString())
                    }
                }
                if(isDiscount){
                    FilterFragmentSearch.isCheckBoxDiscountClicked.value = true
                }
                else{
                    FilterFragmentSearch.isCheckBoxBrandClicked.value = true
                }
//                FilterFragmentSearch.isCheckBoxClicked.value = true
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun checkDiscounts(holder: FilterCheckBoxHolder, position: Int, discountVal:Float){
        println("98982 ${FilterFragmentSearch.checkedDiscountList}")
        if (discountVal in FilterFragmentSearch.checkedDiscountList) {
            println("980123 980123 980123 980123 980123 980123  is checked called for ${items[position]}")
            holder.isItemChecked.isChecked = true
        } else {
            holder.isItemChecked.isChecked = false
        }
    }

}