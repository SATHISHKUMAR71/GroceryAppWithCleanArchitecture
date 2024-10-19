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
import com.example.shoppinggroceryapp.views.userviews.offer.OfferFragment
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

    companion object{
        var list:MutableList<Product>? = null
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
                }
            }.start()
        }

        FilterPrice.isPriceDataChanged.observe(viewLifecycleOwner){
            Thread {
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    availableProducts.text = list?.size.toString()
                }
            }.start()
        }

        FilterFragmentSearch.isCheckBoxClicked.observe(viewLifecycleOwner){
            Thread {
                list = filterViewModel.doFilter(products).toMutableList()
                MainActivity.handler.post {
                    availableProducts.text = list?.size.toString()
                }
            }.start()
        }
//        FilterExpiry.startExpiryDate.observe(viewLifecycleOwner){
//            list = filterViewModel.filterListByDate(products,it,FilterExpiry.endExpiryDate.value?:"").toMutableList()
//            availableProducts.text = list?.size.toString()
//        }
//        FilterExpiry.endExpiryDate.observe(viewLifecycleOwner){
//            list = filterViewModel.filterListByDate(products,FilterExpiry.startExpiryDate.value?:"",it).toMutableList()
//            availableProducts.text = list?.size.toString()
//        }
//        FilterExpiry.startExpiryDate.observe(viewLifecycleOwner){
//            list = filterViewModel.filterListByDate(products,it,FilterExpiry.endExpiryDate.value?:"").toMutableList()
//            availableProducts.text = list?.size.toString()
//        }
//        FilterExpiry.endExpiryDate.observe(viewLifecycleOwner){
//            list = filterViewModel.filterListByDate(products,FilterExpiry.startExpiryDate.value?:"",it).toMutableList()
//            availableProducts.text = list?.size.toString()
//        }
        return view
    }



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

    override fun onDestroyView() {
//        resetStaticValues()
        super.onDestroyView()
    }

//    private fun assignList(){
//        val tmpList:List<Product>
//        if(dis50.isChecked){
//            tmpList = filterViewModel.filterList(products,50f)
//            availableProducts.text = tmpList.size.toString()
//            list = tmpList.toMutableList()
//        }
//        else if(dis40.isChecked){
//            tmpList = filterViewModel.filterList(products,40f)
//            availableProducts.text = tmpList.size.toString()
//            list = tmpList.toMutableList()
//        }
//        else if(dis30.isChecked){
//            tmpList = filterViewModel.filterList(products,30f)
//            availableProducts.text = tmpList.size.toString()
//            list = tmpList.toMutableList()
//        }
//        else if(dis20.isChecked){
//            tmpList = filterViewModel.filterList(products,20f)
//            availableProducts.text = tmpList.size.toString()
//            list = tmpList.toMutableList()
//        }
//        else if(dis10.isChecked){
//            tmpList = filterViewModel.filterList(products,10f)
//            availableProducts.text = tmpList.size.toString()
//            list = tmpList.toMutableList()
//        }
//        else{
//            tmpList = products
//            availableProducts.text = tmpList.size.toString()
//            list = null
//        }
//    }
//
//    private fun resetStaticValues() {
//        OfferFragment.dis10Val = dis10.isChecked
//        OfferFragment.dis20Val = dis20.isChecked
//        OfferFragment.dis30Val = dis30.isChecked
//        OfferFragment.dis40Val = dis40.isChecked
//        OfferFragment.dis50Val =dis50.isChecked
//        ProductListFragment.dis10Val = dis10.isChecked
//        ProductListFragment.dis20Val = dis20.isChecked
//        ProductListFragment.dis30Val = dis30.isChecked
//        ProductListFragment.dis40Val = dis40.isChecked
//        ProductListFragment.dis50Val = dis50.isChecked
//    }

//    fun setExpiryFilter(){
//        println("98416 SET EXPIRY IS CALLED")
//        if(FilterExpiry.startExpiryDate.isNotEmpty() && FilterExpiry.endExpiryDate.isNotEmpty()){
//            adapter.setBadgeForExpiryDate(2)
//        }
//        else if(FilterExpiry.startExpiryDate.isNotEmpty() && FilterExpiry.endExpiryDate.isEmpty()){
//            adapter.setBadgeForExpiryDate(1)
//        }
//        else if(FilterExpiry.startExpiryDate.isEmpty() && FilterExpiry.endExpiryDate.isNotEmpty()){
//            adapter.setBadgeForExpiryDate(1)
//        }
//        else if(FilterExpiry.startExpiryDate.isEmpty() && FilterExpiry.endExpiryDate.isEmpty()){
//            adapter.setBadgeForExpiryDate(-1)
//        }
//        if(FilterExpiry.startManufactureDate.isNotEmpty() && FilterExpiry.endManufactureDate.isNotEmpty()){
//            adapter.setBadgeForManufactureDate(2)
//        }
//        else if(FilterExpiry.startManufactureDate.isNotEmpty() && FilterExpiry.endManufactureDate.isEmpty()){
//            adapter.setBadgeForManufactureDate(1)
//        }
//        else if(FilterExpiry.startManufactureDate.isEmpty() && FilterExpiry.endManufactureDate.isNotEmpty()){
//            adapter.setBadgeForManufactureDate(1)
//        }
//        else if(FilterExpiry.startManufactureDate.isEmpty() && FilterExpiry.endManufactureDate.isEmpty()){
//            adapter.setBadgeForManufactureDate(-1)
//        }
//    }
//    fun returnNonNullList():MutableList<Product>{
//        return if(list==null){
//            products
//        }
//        else{
//            list!!
//        }
//    }
}