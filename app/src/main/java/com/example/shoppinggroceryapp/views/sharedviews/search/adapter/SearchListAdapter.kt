package com.example.shoppinggroceryapp.views.sharedviews.search.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.search.diffutil.SearchListDiffUtil

class SearchListAdapter(var fragment: Fragment) : RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>(){
    companion object{
        var searchList = mutableListOf<String>()
        var hideIcon = false
    }

    inner class SearchViewHolder(searchView:View):RecyclerView.ViewHolder(searchView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_view_holder,parent,false))
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.text).text = searchList[position]

        if(hideIcon){
            holder.itemView.findViewById<ImageView>(R.id.imageIconForSearch).setImageDrawable(ContextCompat.getDrawable(fragment.requireContext(),R.drawable.history_24px))
        }
        else{
            holder.itemView.findViewById<ImageView>(R.id.imageIconForSearch).setImageDrawable(ContextCompat.getDrawable(fragment.requireContext(),R.drawable.search_24px))
//            holder.itemView.findViewById<ImageView>(R.id.imageIconForSearch).visibility = View.VISIBLE
        }
        if(searchList[position]=="No Products Found"){
            holder.itemView.findViewById<ImageView>(R.id.imageIconForSearch).visibility =View.GONE
        }
        else {
            holder.itemView.findViewById<ImageView>(R.id.imageIconForSearch).visibility =View.VISIBLE
            holder.itemView.setOnClickListener {
                InitialFragment.closeSearchView.value = true
                InitialFragment.searchQueryList.add(0, searchList[position])
                InitialFragment.searchHint.value = searchList[position]
                InitialFragment.searchedQuery.value = searchList[position]
                val productListFragment = ProductListFragment()
                productListFragment.arguments = Bundle().apply {
                    putBoolean("searchViewOpened", true)
                    putString("category", searchList[position])
                }
                InitialFragment.category = searchList[position]
            }
        }
    }

    fun setSearch(newStringList:List<String>){
        val diffUtil = SearchListDiffUtil(searchList,newStringList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        searchList.clear()
        searchList.addAll(newStringList)
        diffResults.dispatchUpdatesTo(this)
    }
}