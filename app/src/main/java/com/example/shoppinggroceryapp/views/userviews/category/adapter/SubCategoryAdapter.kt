package com.example.shoppinggroceryapp.views.userviews.category.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.dataclass.ChildCategoryName
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment


class SubCategoryAdapter(var fragment: Fragment,var categoryList: List<ChildCategoryName>):RecyclerView.Adapter<SubCategoryAdapter.SubcategoryHolder>() {

    var size =0
    inner class SubcategoryHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val categoryName =itemView.findViewById<TextView>(R.id.subcategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoryHolder {
        return SubcategoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.sub_category_layout,parent,false))
    }

    override fun getItemCount(): Int {
        size = categoryList.size
        return if(size==0)1 else size
    }

    override fun onBindViewHolder(holder: SubcategoryHolder, position: Int) {
        if(size == 0){
            val text = "No Items in this Category"
            holder.categoryName.text = text
        }
        else{
            holder.categoryName.text = categoryList[position].categoryName
            holder.itemView.setOnClickListener {
                Toast.makeText(fragment.context,"Item Clicked",Toast.LENGTH_SHORT).show()
            }
        }
        holder.itemView.setOnClickListener {
            var productListFrag = ProductListFragment().apply {
                arguments = Bundle().apply {
                    putString("category",categoryList[position].categoryName)
                }
            }
//
//            productListFrag.arguments = Bundle().apply {
//
//            }
            fragment.parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.fragmentMainLayout,
                    productListFrag
                )
                .addToBackStack("Product List")
                .commit()
        }
    }
}