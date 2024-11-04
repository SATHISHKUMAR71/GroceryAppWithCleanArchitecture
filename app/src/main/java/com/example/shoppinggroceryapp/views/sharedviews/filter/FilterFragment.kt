package com.example.shoppinggroceryapp.views.sharedviews.filter

import android.icu.util.TimeUnit
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.domain.products.Product
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Time


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
    companion object{
        var list:MutableList<Product>? = null
        var badgeNumber = 0
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        adapter = FilterAdapter(listOf("Discounts","Brand","Expiry Date","Price"),
            listOf(),this,
            listOf("10% or more","20% or more","30% or more","40% or more","50% or more")
        )
        parentFragmentManager.beginTransaction()
            .replace(R.id.detailOptions, FilterFragmentSearch(listOf("10% or more","20% or more","30% or more","40% or more","50% or more"))
                .apply {
                    arguments = Bundle().apply {
                        putBoolean("isDiscount", true)
                    }
                })
            .commit()
        filterViewModel.getBrandNames()
        filterViewModel.brandList.observe(viewLifecycleOwner){
            adapter = FilterAdapter(listOf("Discounts","Brand","Expiry Date","Price"),it,this,
                listOf("10% or more","20% or more","30% or more","40% or more","50% or more")
            )
            adapter.setBadges()
            if(recyclerViewFilterType.adapter==null){
                recyclerViewFilterType.adapter = adapter
                recyclerViewFilterType.layoutManager = LinearLayoutManager(context)
            }

//            adapter.performFirstClick()
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
                    adapter.setBadges()
                }
            }.start()
        }

        FilterPrice.isPriceDataChanged.observe(viewLifecycleOwner){
            Thread {
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    availableProducts.text = list?.size.toString()
                    if(FilterPrice.priceStartFrom!=PRICE_START_VALUE && FilterPrice.priceEndTo!=FilterPrice.MAX_PRICE_VALUE){
                        adapter.setBadgeForPrice(2)
                    }
                    else if(FilterPrice.priceStartFrom==PRICE_START_VALUE && FilterPrice.priceEndTo!=FilterPrice.MAX_PRICE_VALUE){
                        adapter.setBadgeForPrice(1)
                    }
                    else if(FilterPrice.priceStartFrom!=PRICE_START_VALUE && FilterPrice.priceEndTo==FilterPrice.MAX_PRICE_VALUE){
                        adapter.setBadgeForPrice(1)
                    }
                    else if(FilterPrice.priceStartFrom==PRICE_START_VALUE && FilterPrice.priceEndTo==FilterPrice.MAX_PRICE_VALUE){
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


        FilterFragmentSearch.checkboxClear.observe(viewLifecycleOwner){
            if(FilterFragmentSearch.checkedList.isEmpty()){
                adapter.setBadgeForBrand(0)
                println("323232 ON IF FOR BRAND LIST EMPTY")
            }
            if(FilterFragmentSearch.checkedDiscountList.isEmpty()){
                adapter.setBadgeForDiscount(0)
                println("323232 ON IF FOR DISCOUNT LIST EMPTY")
            }
            lifecycleScope.launch(Dispatchers.IO){
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    list?.let {
                        availableProducts.text = it.size.toString()
                    }
                }
            }
        }

        clearAllButton.setOnClickListener {
            FilterExpiry.startExpiryDate = ""
            FilterExpiry.startManufactureDate = ""
            FilterExpiry.endExpiryDate = ""
            FilterExpiry.endManufactureDate = ""
            FilterPrice.priceStartFrom = PRICE_START_VALUE
            FilterPrice.priceEndTo = FilterPrice.MAX_PRICE_VALUE
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
            lifecycleScope.launch(Dispatchers.IO){
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view?.postDelayed({
//            view?.findViewById<RecyclerView>(R.id.categoryType)?.post{
//                println("78687 on view created on click ${view?.findViewById<RecyclerView>(R.id.categoryType)?.findViewHolderForAdapterPosition(0)?.itemView}")
//                view?.findViewById<RecyclerView>(R.id.categoryType)?.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<MaterialButton>(R.id.filterOptionsDiscountBtn)?.performClick()
//            }
//        },5L)
    }


    override fun onResume() {
        super.onResume()
        InitialFragment.hideBottomNav.value = true
        InitialFragment.hideSearchBar.value = true
        if(adapter.highlightedPos==-1) {
            view?.postDelayed({
                view?.findViewById<RecyclerView>(R.id.categoryType)?.post {
                    println(
                        "78687 on view created on click ${
                            view?.findViewById<RecyclerView>(R.id.categoryType)
                                ?.findViewHolderForAdapterPosition(0)?.itemView
                        }"
                    )
                    view?.findViewById<RecyclerView>(R.id.categoryType)
                        ?.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<MaterialButton>(
                        R.id.filterOptionsDiscountBtn
                    )?.performClick()
                }
            }, 5L)
        }
    }

    override fun onPause() {
        super.onPause()
        InitialFragment.hideBottomNav.value = false
        InitialFragment.hideSearchBar.value = false
    }

}