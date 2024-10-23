package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.products.Product
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton


class FilterFragment(var products:MutableList<Product>) : Fragment() {

    var category:String?= null
    private var type:String? = null
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var dis50:CheckBox
    private lateinit var dis40:CheckBox
    private lateinit var dis30:CheckBox
    private lateinit var dis20:CheckBox
    private lateinit var adapter: FilterAdapter
    private lateinit var dis10:CheckBox
    private lateinit var availableProducts:TextView
    var PRICE_START_VALUE = 0F
    var PRICE_MAX_VALUE = 2010f
    companion object{
        var list:MutableList<Product>? = null
        var badgeNumber = 0
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        category = arguments?.getString("category",null)
        type = arguments?.getString("type")
        val view =  inflater.inflate(R.layout.fragment_filter, container, false)
        dis50 = view.findViewById(R.id.fragmentOptionDiscount50)
        filterViewModel = ViewModelProvider(this,GroceryAppSharedVMFactory(AppDatabase.getAppDatabase(requireContext()).getRetailerDao(),AppDatabase.getAppDatabase(requireContext()).getUserDao()))[FilterViewModel::class.java]
        dis40 = view.findViewById(R.id.fragmentOptionDiscount40)
        dis30 = view.findViewById(R.id.fragmentOptionDiscount30)
        dis20 = view.findViewById(R.id.fragmentOptionDiscount20)
        dis10 = view.findViewById(R.id.fragmentOptionDiscount10)
        val applyButton = view.findViewById<MaterialButton>(R.id.applyFilterButton)
        val clearAllButton = view.findViewById<MaterialButton>(R.id.clearAllFilterButton)
        availableProducts = view.findViewById(R.id.availableProducts)
        availableProducts.text =products.size.toString()
        view.findViewById<MaterialToolbar>(R.id.materialToolbarFilter).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        val recyclerViewFilterType = view.findViewById<RecyclerView>(R.id.categoryType)
        adapter = FilterAdapter(listOf("Discounts","Brand","Expiry Date","Price","Manufacture Date"),
            listOf(),this,
            listOf("10% or more","20% or more","30% or more","40% or more","50% or more")
        )
        filterViewModel.getBrandNames()
        filterViewModel.brandList.observe(viewLifecycleOwner){
            adapter = FilterAdapter(listOf("Discounts","Brand","Expiry Date","Price","Manufacture Date"),it,this,
                listOf("10% or more","20% or more","30% or more","40% or more","50% or more")
            )
            adapter.setBadges()
            if(recyclerViewFilterType.adapter==null){
                recyclerViewFilterType.adapter = adapter
                recyclerViewFilterType.layoutManager = LinearLayoutManager(context)
            }
        }
        applyButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        println("98416 ${FilterExpiry.isDataChanged.value}")
        FilterExpiry.isDataChanged.observe(viewLifecycleOwner){
            Thread {
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    availableProducts.text = list?.size.toString()
//                    setBadges()
                }
            }.start()
        }

        FilterPrice.isPriceDataChanged.observe(viewLifecycleOwner){
            Thread {
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    availableProducts.text = list?.size.toString()
                    if(FilterPrice.priceStartFrom!=PRICE_START_VALUE && FilterPrice.priceEndTo!=PRICE_MAX_VALUE){
                        adapter.setBadgeForPrice(2)
                    }
                    else if(FilterPrice.priceStartFrom==PRICE_START_VALUE && FilterPrice.priceEndTo!=PRICE_MAX_VALUE){
                        adapter.setBadgeForPrice(1)
                    }
                    else if(FilterPrice.priceStartFrom!=PRICE_START_VALUE && FilterPrice.priceEndTo==PRICE_MAX_VALUE){
                        adapter.setBadgeForPrice(1)
                    }
                    else if(FilterPrice.priceStartFrom==PRICE_START_VALUE && FilterPrice.priceEndTo==PRICE_MAX_VALUE){
                        adapter.setBadgeForPrice(0)
                    }
                }
            }.start()
        }

        FilterFragmentSearch.isCheckBoxBrandClicked.observe(viewLifecycleOwner){
            Thread {
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    availableProducts.text = list?.size.toString()
                    if(FilterFragmentSearch.checkedList.isNotEmpty()){
                        adapter.setBadgeForBrand(FilterFragmentSearch.checkedList.size)
                    }
                    else{
                        adapter.setBadgeForBrand(0)
                    }
                }
            }.start()
        }
        FilterFragmentSearch.isCheckBoxDiscountClicked.observe(viewLifecycleOwner){
            Thread {
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    availableProducts.text = list?.size.toString()
                    if(FilterFragmentSearch.checkedDiscountList.isNotEmpty()){
                        adapter.setBadgeForDiscount(FilterFragmentSearch.checkedDiscountList.size)
                    }
                    else{
                        adapter.setBadgeForDiscount(0)
                    }
                }
            }.start()
        }

        clearAllButton.setOnClickListener {
            FilterExpiry.startExpiryDate = ""
            FilterExpiry.startManufactureDate = ""
            FilterExpiry.endExpiryDate = ""
            FilterExpiry.endManufactureDate = ""
            FilterPrice.priceStartFrom = PRICE_START_VALUE
            FilterPrice.priceEndTo = PRICE_MAX_VALUE
            FilterFragmentSearch.checkedList = mutableListOf()
            FilterFragmentSearch.checkedDiscountList = mutableListOf()
            adapter.resetViews()
            FilterFragmentSearch.clearAll.value = true
            FilterPrice.clearAll.value = true
            FilterExpiry.clearAll.value = true
            adapter.setBadgeForBrand(0)
            adapter.setBadgeForDiscount(0)
            adapter.setBadgeForPrice(0)
            adapter.setBadgeForExpiryDate(0)
            adapter.setBadgeForManufactureDate(0)
            Thread {
                filterViewModel.doFilter(products)
                list = null
                MainActivity.handler.post {
                    availableProducts.text = products.size.toString()
                }
            }.start()
        }
//        setBadges()
        return view
    }

//    private fun setBadges() {
//        println("987654 ADAPTER SET BADGES CALLED")
//        if(FilterExpiry.endExpiryDate.isEmpty() && FilterExpiry.startExpiryDate.isEmpty()){
//            adapter.setBadgeForExpiryDate(0)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 177")
//        }
//        else if(FilterExpiry.endExpiryDate.isEmpty() && FilterExpiry.startExpiryDate.isNotEmpty()){
//            adapter.setBadgeForExpiryDate(1)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 181")
//        }
//        else if(FilterExpiry.endExpiryDate.isNotEmpty() && FilterExpiry.startExpiryDate.isEmpty()){
//            adapter.setBadgeForExpiryDate(1)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 185")
//        }
//        else if(FilterExpiry.endExpiryDate.isNotEmpty() && FilterExpiry.startExpiryDate.isNotEmpty()){
//            adapter.setBadgeForExpiryDate(2)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 189")
//        }
//        if(FilterExpiry.endManufactureDate.isEmpty() && FilterExpiry.startManufactureDate.isEmpty()){
//            adapter.setBadgeForManufactureDate(0)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 193")
//        }
//        else if(FilterExpiry.endManufactureDate.isEmpty() && FilterExpiry.startManufactureDate.isNotEmpty()){
//            adapter.setBadgeForManufactureDate(1)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 197")
//        }
//        else if(FilterExpiry.endManufactureDate.isNotEmpty() && FilterExpiry.startManufactureDate.isEmpty()){
//            adapter.setBadgeForManufactureDate(1)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 201")
//        }
//        else if(FilterExpiry.endManufactureDate.isNotEmpty() && FilterExpiry.startManufactureDate.isNotEmpty()){
//            adapter.setBadgeForManufactureDate(2)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 205")
//        }
//        if(FilterPrice.priceStartFrom!=0f && FilterPrice.priceEndTo!=2010f){
//            adapter.setBadgeForPrice(2)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 209")
//        }
//        else if(FilterPrice.priceStartFrom==0f && FilterPrice.priceEndTo!=2010f){
//            adapter.setBadgeForPrice(1)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 213")
//        }
//        else if(FilterPrice.priceStartFrom!=0f && FilterPrice.priceEndTo==2010f){
//            adapter.setBadgeForPrice(1)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 217")
//        }
//        else{
//            adapter.setBadgeForPrice(2)
//            println("987654 ADAPTER SET EXPIRY BADGES CALLED 221")
//        }
//    }


    override fun onResume() {
        super.onResume()
        InitialFragment.hideBottomNav.value = true
        InitialFragment.hideSearchBar.value = true
    }

    override fun onPause() {
        super.onPause()
        InitialFragment.hideBottomNav.value = false
        InitialFragment.hideSearchBar.value = false
    }

}