package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R


class FilterFragmentSearch(private var brandList:List<String>) : Fragment() {


    companion object{
        var isCheckBoxBrandClicked:MutableLiveData<Boolean> = MutableLiveData()
        var isCheckBoxDiscountClicked:MutableLiveData<Boolean> = MutableLiveData()
        var checkedList:MutableList<String> = mutableListOf()
        var checkedDiscountList:MutableList<Float> = mutableListOf()
        var clearAll:MutableLiveData<Boolean> = MutableLiveData()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_filter_search, container, false)
        var isDiscount = arguments?.getBoolean("isDiscount")
        val recyclerView = view.findViewById<RecyclerView>(R.id.brandList)
        val newList = mutableListOf<Boolean>()
        for(i in brandList){
            newList.add(false)
        }
        var adapter = FilterCheckBoxItemsAdapter(brandList,newList,isDiscount?:false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        clearAll.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
        }
        return view
    }


}