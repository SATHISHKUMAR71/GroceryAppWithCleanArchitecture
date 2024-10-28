package com.example.shoppinggroceryapp.views.userviews.offer

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.products.Images
import com.core.domain.products.Product
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.data.authentication.AuthenticationDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.address.AddressDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.cart.CartDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.help.HelpDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.order.OrderDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.search.SearchDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.subscription.SubscriptionDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.user.UserDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.dao.RetailerDao
import com.example.shoppinggroceryapp.framework.db.dao.UserDao
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.ResetFilterValues
import com.example.shoppinggroceryapp.views.sharedviews.sort.BottomSheetDialogFragment
import com.example.shoppinggroceryapp.views.sharedviews.sort.ProductSorter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.button.MaterialButton
import java.io.File


class OfferFragment : Fragment() {

    companion object {
        var dis50Val: Boolean? = null
        var dis40Val: Boolean? = null
        var dis30Val: Boolean? = null
        var dis20Val: Boolean? = null
        var dis10Val: Boolean? = null
//        var offerFilterCount:Int  = 0
        var offerListFirstVisiblePos:Int? = null
    }
    private lateinit var filterAndSortLayout:LinearLayout
    private lateinit var filterCount:TextView
    private var realList = mutableListOf<Product>()
    var productEntities = listOf<Product>()
    private lateinit var offerList:RecyclerView
    private lateinit var adapter: ProductListAdapter
    private lateinit var offerViewModel: OfferViewModel
    private lateinit var noItemsFoundImageText:TextView
    private lateinit var sortButton:MaterialButton
    private lateinit var filterButton: MaterialButton
    private lateinit var userDao:UserDao
    private lateinit var retailerDao:RetailerDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStaticVariable()
        val db1 = AppDatabase.getAppDatabase(requireContext())
        userDao = db1.getUserDao()
        retailerDao = db1.getRetailerDao()
        ResetFilterValues.resetFilterValues()
    }

    private fun initStaticVariable() {

        BottomSheetDialogFragment.selectedOption.value = null
//        offerFilterCount = 0
        dis10Val = false
        dis20Val = false
        dis30Val = false
        dis40Val = false
        dis50Val =false
        ProductListFragment.dis10Val = false
        ProductListFragment.dis20Val = false
        ProductListFragment.dis30Val = false
        ProductListFragment.dis40Val = false
        ProductListFragment.dis50Val = false
        FilterFragment.list = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_offer, container, false)
        noItemsFoundImageText = view.findViewById(R.id.noItemsFoundText)
        offerList = view.findViewById(R.id.offerList)
        filterCount = view.findViewById(R.id.filterCountTextViewOffer)
        filterAndSortLayout = view.findViewById(R.id.linearLayout15)
        sortButton = view.findViewById(R.id.sortButton)
        filterButton = view.findViewById(R.id.filterButton)
        var fileDir = File(requireContext().filesDir,"AppImages")
        adapter = ProductListAdapter(this,fileDir,"O",false,productListViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java],null)

        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        offerViewModel = ViewModelProvider(this,
            GroceryAppUserVMFactory(userDao, retailerDao)
        )[OfferViewModel::class.java]

        if(FilterFragment.list!=null){
            updateVisibility(FilterFragment.list!!)
            adapter.setProducts(FilterFragment.list!!)
            if(offerList.adapter==null) {
                offerList.adapter = adapter
                offerList.layoutManager = LinearLayoutManager(context)
            }
        }
        offerViewModel.getOfferedProducts(fileDir)

        setUpOnCLickListeners()
        setUpObservers()
        attachBadge()

        if(FilterFragment.badgeNumber >0){
            filterCount.text = FilterFragment.badgeNumber.toString()
            filterCount.visibility = View.VISIBLE
        }
        else{
            filterCount.visibility = View.INVISIBLE
        }

        return view
    }

    @OptIn(ExperimentalBadgeUtils::class)
    private fun attachBadge() {
        val filterBadge = BadgeDrawable.create(requireContext())
        filterBadge.number = 10
        filterBadge.badgeTextColor = Color.WHITE
        filterBadge.backgroundColor = Color.RED
        BadgeUtils.attachBadgeDrawable(filterBadge,filterButton)
    }

    private fun setUpObservers() {
        offerViewModel.offeredProductEntityList.observe(viewLifecycleOwner){ offeredProductList ->
            realList = offeredProductList.toMutableList()
            if(productEntities.isEmpty()) {
                productEntities = offeredProductList
            }
            if(FilterFragment.list==null){
                updateVisibility(productEntities)
                if(BottomSheetDialogFragment.selectedOption.value==null) {
                    adapter.setProducts(offeredProductList)
                }
                else{
                    adapter.setProducts(productEntities)
                }
                if(offerList.adapter==null) {
                    offerList.adapter = adapter
                    offerList.layoutManager = LinearLayoutManager(context)
                }
            }
        }

        BottomSheetDialogFragment.selectedOption.observe(viewLifecycleOwner){
            it?.let {
                offerViewModel.doSorting(adapter, it, productEntities, ProductSorter())?.let { list ->
                    productEntities = list
                }
            }
            offerList.layoutManager?.let {layoutManager ->
                (layoutManager as LinearLayoutManager).scrollToPosition(0)
            }
        }

    }

    private fun setUpOnCLickListeners() {
        sortButton.setOnClickListener {
            val bottomSheet = BottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager,"Bottom Sort Sheet")
        }

        filterButton.setOnClickListener {
//            offerFilterCount = 0
            productEntities = realList
            FragmentTransaction.navigateWithBackstack(
                parentFragmentManager,
                FilterFragment(realList),
                "Filter Fragment"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if(FilterFragment.list!=null){
            adapter.setProducts(FilterFragment.list!!)
            offerList.adapter = adapter
            offerList.layoutManager = LinearLayoutManager(context)
        }

        if(FilterFragment.list?.isNotEmpty()==true){
            adapter.setProducts(FilterFragment.list!!)
        }
        else if (productEntities.isNotEmpty()) {
            adapter.setProducts(productEntities)
        }
        offerList.scrollToPosition(offerListFirstVisiblePos ?:0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BottomSheetDialogFragment.selectedOption.removeObservers(viewLifecycleOwner)
    }
    override fun onStop() {
        super.onStop()
        offerListFirstVisiblePos = (offerList.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        offerList.stopScroll()
    }


    override fun onDestroy() {
        super.onDestroy()
        FilterFragment.list = null
//        offerFilterCount = 0
        offerListFirstVisiblePos = null
        ResetFilterValues.resetFilterValues()
        dis10Val = false
        dis20Val = false
        dis30Val = false
        dis40Val = false
        dis50Val =false
        ProductListFragment.dis10Val = false
        ProductListFragment.dis20Val = false
        ProductListFragment.dis30Val = false
        ProductListFragment.dis40Val = false
        ProductListFragment.dis50Val = false
    }


    private fun updateVisibility(products:List<Product>){
        if(products.isEmpty()){
            offerList.visibility = View.GONE
            noItemsFoundImageText.visibility = View.VISIBLE
        }
        else{
            offerList.visibility = View.VISIBLE
            noItemsFoundImageText.visibility = View.GONE
        }
    }

}

