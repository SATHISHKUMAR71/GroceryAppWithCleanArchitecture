package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.R
import com.google.android.material.button.MaterialButton


class FilterFragmentSearch(private var brandList:List<String>) : Fragment(),ButtonVisibleCheck {


    companion object{
        var isCheckBoxBrandClicked:MutableLiveData<Boolean> = MutableLiveData()
        var isCheckBoxDiscountClicked:MutableLiveData<Boolean> = MutableLiveData()
        var checkedList:MutableList<String> = mutableListOf()
        var checkedDiscountList:MutableList<Float> = mutableListOf()
        var clearAll:MutableLiveData<Boolean> = MutableLiveData()
        var checkboxClear:MutableLiveData<Boolean> = MutableLiveData()
    }
    var isDiscount:Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_filter_search, container, false)
        isDiscount = arguments?.getBoolean("isDiscount")
        val recyclerView = view.findViewById<RecyclerView>(R.id.brandList)
        val newList = mutableListOf<Boolean>()
        for(i in brandList){
            newList.add(false)
        }
        val searchBar = view.findViewById<SearchView>(R.id.searchViewBrand)
        var adapter = FilterCheckBoxItemsAdapter(brandList.toMutableList(),newList,isDiscount?:false,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        clearAll.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
            checkClearButtonIsVisible()
        }
        searchBar.setOnCloseListener{
            println("123 ON CLOSE LISTENER")
            true
        }


        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var list = brandList.filter { it.contains(newText?:"",ignoreCase = true) }
                println("NEW LIST 1234 $list")
                println("NEW LIST 1234 old $brandList")
                adapter.updateBrandName(list)
                return true
            }

        })
        println("IS DISCOUNT VALUE: $isDiscount")
        if(isDiscount==true){
            searchBar?.visibility = View.GONE
        }
        else{
            searchBar?.visibility = View.VISIBLE
        }

        view.findViewById<MaterialButton>(R.id.clearButton).setOnClickListener {
            if(isDiscount==true) {
                FilterFragmentSearch.checkedDiscountList = mutableListOf()
            }
            else{
                FilterFragmentSearch.checkedList = mutableListOf()
            }
            checkboxClear.value = true
            adapter.notifyDataSetChanged()
            view.findViewById<LinearLayout>(R.id.clearFilterTypeLayout).visibility = View.GONE
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkClearButtonIsVisible()
    }


    override fun checkClearButtonIsVisible(){
        println("878787 CALLED FROM THE FRAGMENT ${FilterFragmentSearch.checkedList.isEmpty()} ${checkedDiscountList.isEmpty()}")
        if(isDiscount==true) {
            println("878787 CALLED FROM THE FRAGMENT on discount if ${FilterFragmentSearch.checkedList.isEmpty()} ${checkedDiscountList.isEmpty()}")
            if (FilterFragmentSearch.checkedDiscountList.isNotEmpty()) {
                view?.findViewById<LinearLayout>(R.id.clearFilterTypeLayout)?.apply {
                    visibility = View.VISIBLE
                    view?.findViewById<MaterialButton>(R.id.clearButton)?.text = "Clear Discount Filters"
                }
            }
            else{
                view?.findViewById<LinearLayout>(R.id.clearFilterTypeLayout)?.visibility =
                    View.GONE
            }
        }
        else{
            println("878787 CALLED FROM THE FRAGMENT on discount else  $isDiscount ${FilterFragmentSearch.checkedList.isEmpty()} ${checkedDiscountList.isEmpty()}")
            if (FilterFragmentSearch.checkedList.isNotEmpty()) {
                view?.findViewById<LinearLayout>(R.id.clearFilterTypeLayout)?.apply {
                    view?.findViewById<MaterialButton>(R.id.clearButton)?.text = "Clear Brand Filters"
                    visibility = View.VISIBLE
                }
            }
            else{
                view?.findViewById<LinearLayout>(R.id.clearFilterTypeLayout)?.visibility =
                    View.GONE
            }
        }
    }

}