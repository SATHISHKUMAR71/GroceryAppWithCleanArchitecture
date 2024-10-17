package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R


class FilterFragmentSearch(var brandList:List<String>) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_filter_search, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.brandList)
        val newList = mutableListOf<Boolean>()
        for(i in brandList){
            newList.add(false)
        }
        recyclerView.adapter = FilterCheckBoxItemsAdapter(brandList,newList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }


}