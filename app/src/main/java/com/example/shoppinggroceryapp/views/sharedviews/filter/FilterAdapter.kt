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
    var highlightedPos = 0
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
            println("78687 ON CLICK LISTENER IN FILTER ADAPTER")
            if(highlightedPos!=holder.absoluteAdapterPosition) {
                highlightedPos = holder.absoluteAdapterPosition
                resetViews()
                when (filterTypeList[position]) {
                    "Brand" -> {
                        fragment.parentFragmentManager.beginTransaction()
                            .replace(R.id.detailOptions, FilterFragmentSearch(brandData)
                                .apply {
                                    arguments = Bundle().apply {
                                        putBoolean("isDiscount", false)
                                    }
                                })
                            .commit()
                    }

                    "Price" -> {
                        fragment.parentFragmentManager.beginTransaction()
                            .replace(R.id.detailOptions, FilterPrice())
                            .commit()
                    }

                    "Expiry Date" -> {

                        fragment.parentFragmentManager.beginTransaction()
                            .replace(R.id.detailOptions, FilterExpiry().apply {
                                arguments = Bundle().apply {
                                    putBoolean("isExpiry", true)
                                }
                            })
                            .commit()
                    }

                    "Manufacture Date" -> {
                        fragment.parentFragmentManager.beginTransaction()
                            .replace(R.id.detailOptions, FilterExpiry().apply {
                                arguments = Bundle().apply {
                                    putBoolean("isExpiry", false)
                                }
                            })
                            .commit()
                    }

                    "Discounts" -> {
                        fragment.parentFragmentManager.beginTransaction()
                            .replace(R.id.detailOptions, FilterFragmentSearch(discountList)
                                .apply {
                                    arguments = Bundle().apply {
                                        putBoolean("isDiscount", true)
                                    }
                                })
                            .commit()
                    }
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
        notifyDataSetChanged()
    }

    fun setBadgeForDiscount(badgeNumber:Int){
        discountBadge = badgeNumber
        notifyItemChanged(0)
    }

    fun setBadgeForBrand(badgeNumber:Int){
        brandBadge = badgeNumber
        notifyItemChanged(1)
    }
    fun setBadgeForExpiryDate(badgeNumber:Int){
        expiryDateBadge = badgeNumber
        notifyItemChanged(2)
    }
    fun setBadgeForPrice(badgeNumber:Int){
        priceBadge = badgeNumber
        notifyItemChanged(3)
    }
    fun setBadgeForManufactureDate(badgeNumber:Int){
        manufactureBadge = badgeNumber
        notifyItemChanged(4)
    }
    fun setBadges() {
        if(FilterExpiry.endExpiryDate.isEmpty() && FilterExpiry.startExpiryDate.isEmpty()){
            setBadgeForExpiryDate(0)
        }
        else if(FilterExpiry.endExpiryDate.isEmpty() && FilterExpiry.startExpiryDate.isNotEmpty()){
            setBadgeForExpiryDate(1)
        }
        else if(FilterExpiry.endExpiryDate.isNotEmpty() && FilterExpiry.startExpiryDate.isEmpty()){
            setBadgeForExpiryDate(1)
        }
        else if(FilterExpiry.endExpiryDate.isNotEmpty() && FilterExpiry.startExpiryDate.isNotEmpty()){
            setBadgeForExpiryDate(2)
        }
        if(FilterExpiry.endManufactureDate.isEmpty() && FilterExpiry.startManufactureDate.isEmpty()){
            setBadgeForManufactureDate(0)
        }
        else if(FilterExpiry.endManufactureDate.isEmpty() && FilterExpiry.startManufactureDate.isNotEmpty()){
            setBadgeForManufactureDate(1)
        }
        else if(FilterExpiry.endManufactureDate.isNotEmpty() && FilterExpiry.startManufactureDate.isEmpty()){
            setBadgeForManufactureDate(1)
        }
        else if(FilterExpiry.endManufactureDate.isNotEmpty() && FilterExpiry.startManufactureDate.isNotEmpty()){
            setBadgeForManufactureDate(2)
        }
        if(FilterPrice.priceStartFrom!=0f && FilterPrice.priceEndTo!=FilterPrice.MAX_PRICE_VALUE){
            setBadgeForPrice(2)
        }
        else if(FilterPrice.priceStartFrom==0f && FilterPrice.priceEndTo!=FilterPrice.MAX_PRICE_VALUE){
            setBadgeForPrice(1)
        }
        else if(FilterPrice.priceStartFrom!=0f && FilterPrice.priceEndTo==FilterPrice.MAX_PRICE_VALUE){
            setBadgeForPrice(1)
        }
        else{
            setBadgeForPrice(0)
        }
        setBadgeForDiscount(FilterFragmentSearch.checkedDiscountList.size)
        setBadgeForBrand(FilterFragmentSearch.checkedList.size)

    }



}