package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R
import com.google.android.material.button.MaterialButton

class FilterAdapter(var filterTypeList: List<String>,var brandData:List<String>,var fragment:Fragment,var discountList:List<String>):RecyclerView.Adapter<FilterAdapter.FilterTypeViewHolder>() {
    var highlightedPos = -1
    inner class FilterTypeViewHolder(filterTypeView:View):RecyclerView.ViewHolder(filterTypeView){
        val layout = filterTypeView.findViewById<ConstraintLayout>(R.id.filterOptionsDiscount)
        val button  = filterTypeView.findViewById<MaterialButton>(R.id.filterOptionsDiscountBtn)
        val badge = filterTypeView.findViewById<TextView>(R.id.filterCountTextView)
    }
    var discountBadge = 0
    var brandBadge = 0
    var expiryDateBadge = 0
    var priceBadge = 0
    var manufactureBadge = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterTypeViewHolder {
        return FilterTypeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.filter_button_option,parent,false))
    }

    override fun getItemCount(): Int {
        return filterTypeList.size
    }

    override fun onBindViewHolder(holder: FilterTypeViewHolder, position: Int) {
        holder.button.text = filterTypeList[position]
        FilterFragment.badgeNumber = (if(discountBadge!=0) 1 else 0) + (if(brandBadge!=0)1 else 0) + (if(expiryDateBadge!=0)1 else 0)+ (if(priceBadge!=0)1 else 0)+(if(manufactureBadge!=0)1 else 0)
        println("FILTER ADAPTER: VALUE: ${FilterFragment.badgeNumber}")
        if(position==0){
            if(discountBadge!=0){
                setBadgeVisibility(discountBadge,holder)
            }
            else{
                setBadgeInVisibility(holder)
            }
        }
        else if(position==1){
            if(brandBadge!=0){
                setBadgeVisibility(brandBadge,holder)
            }
            else{
                setBadgeInVisibility(holder)
            }
        }
        else if(position==2){
            println("98416 EXPIRY DATE CHANGED CALLED notfied $expiryDateBadge")
            if(expiryDateBadge!=0){
                setBadgeVisibility(expiryDateBadge,holder)
            }
            else{
                setBadgeInVisibility(holder)
            }
        }
        else if(position==3){
            if(priceBadge!=0){
                setBadgeVisibility(priceBadge,holder)
            }
            else{
                setBadgeInVisibility(holder)
            }
        }
        else if(position==4){
            if(manufactureBadge!=0){
                setBadgeVisibility(manufactureBadge,holder)
            }
            else{
                setBadgeInVisibility(holder)
            }
        }
        if(highlightedPos==position){
            holder.layout.setBackgroundColor(Color.WHITE)
        }
        else{
            holder.layout.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.button.setOnClickListener {
            highlightedPos = holder.absoluteAdapterPosition
            resetViews()
            when(filterTypeList[position]){
                "Brand" -> {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.detailOptions,FilterFragmentSearch(brandData)
                            .apply {
                                arguments = Bundle().apply {
                                    putBoolean("isDiscount",false)
                                }
                            })
                        .commit()
                }
                "Price" -> {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.detailOptions,FilterPrice())
                        .commit()
                }
                "Expiry Date" -> {

                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.detailOptions,FilterExpiry().apply {
                            arguments = Bundle().apply {
                                putBoolean("isExpiry",true)
                            }
                        })
                        .commit()
                }
                "Manufacture Date" -> {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.detailOptions,FilterExpiry().apply {
                            arguments = Bundle().apply {
                                putBoolean("isExpiry",false)
                            }
                        })
                        .commit()
                }
                "Discounts" -> {
                    fragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.detailOptions,FilterFragmentSearch(discountList)
                            .apply {
                                arguments = Bundle().apply {
                                    putBoolean("isDiscount",true)
                                }
                            })
                        .commit()
                }
            }
        }

    }

    private fun setBadgeVisibility(discountBadge: Int,holder: FilterTypeViewHolder) {
        holder.badge.text =discountBadge.toString()
        holder.badge.visibility = View.VISIBLE
    }

    private fun setBadgeInVisibility(holder: FilterTypeViewHolder) {
        holder.badge.visibility = View.GONE
    }

    fun resetViews(){
        println("8787878 notify data set changed")
        notifyDataSetChanged()
    }

    fun setBadgeForDiscount(badgeNumber:Int){
        println("8787878 notify item changed called: for discount")
        discountBadge = badgeNumber
        notifyItemChanged(0)
    }

    fun setBadgeForBrand(badgeNumber:Int){
        println("8787878 notify item changed called: for brand")
        brandBadge = badgeNumber
        notifyItemChanged(1)
    }
    fun setBadgeForExpiryDate(badgeNumber:Int){
        println("8787878 notify item changed called: for expiry")
        println("98416 EXPIRY DATE CHANGED CALLED $badgeNumber")
        expiryDateBadge = badgeNumber
        notifyItemChanged(2)
    }
    fun setBadgeForPrice(badgeNumber:Int){
        println("8787878 notify item changed called: for price")
        priceBadge = badgeNumber
        notifyItemChanged(3)
    }
    fun setBadgeForManufactureDate(badgeNumber:Int){
        println("8787878 notify item changed called: for manufacture")
        manufactureBadge = badgeNumber
        notifyItemChanged(4)
    }
}