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
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppViewModelFactory
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment.Companion.productListFilterCount
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
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
        var offerFilterCount:Int  = 0
        var offerListFirstVisiblePos:Int? = null
    }
    private lateinit var filterAndSortLayout:LinearLayout
    private lateinit var filterCount:TextView
    private var realList = mutableListOf<Product>()
    var productEntities = listOf<Product>()
    private lateinit var offerList:RecyclerView
    private lateinit var adapter: ProductListAdapter
    private lateinit var offerViewModel: OfferViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productListFilterCount = 0
        BottomSheetDialogFragment.selectedOption.value = null
        offerFilterCount = 0
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

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_offer, container, false)
        val noItemsFoundImage = view.findViewById<ImageView>(R.id.noItemFoundImageOfferFragment)
        val noItemsFoundImageText = view.findViewById<TextView>(R.id.noItemsFoundText)
        offerList = view.findViewById(R.id.offerList)
        filterCount = view.findViewById<TextView>(R.id.filterCountTextViewOffer)
        filterAndSortLayout = view.findViewById(R.id.linearLayout15)
        val sortButton = view.findViewById<MaterialButton>(R.id.sortButton)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        val userRepository = UserRepository(UserDataSourceImpl(userDao))
        val authenticationRepository = AuthenticationRepository(AuthenticationDataSourceImpl(userDao))
        val cartRepository: CartRepository = CartRepository(CartDataSourceImpl(userDao))
        val helpRepository: HelpRepository = HelpRepository(
            HelpDataSourceImpl(retailerDao),
            HelpDataSourceImpl(retailerDao)
        )
        val orderRepository: OrderRepository = OrderRepository(
            OrderDataSourceImpl(retailerDao),
            OrderDataSourceImpl(retailerDao)
        )
        val productRepository: ProductRepository = ProductRepository(
            ProductDataSourceImpl(retailerDao),
            ProductDataSourceImpl(retailerDao)
        )
        val searchRepository: SearchRepository = SearchRepository(SearchDataSourceImpl(userDao))
        val subscriptionRepository: SubscriptionRepository = SubscriptionRepository(
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao),
            SubscriptionDataSourceImpl(userDao)
        )
        val addressRepository: AddressRepository = AddressRepository(AddressDataSourceImpl(userDao))

        val filterButton = view.findViewById<MaterialButton>(R.id.filterButton)
        val fileDir = File(requireContext().filesDir,"AppImages")
        adapter = ProductListAdapter(this,fileDir,"O",false,productListViewModel = ViewModelProvider(this,
            GroceryAppViewModelFactory(userRepository, authenticationRepository, cartRepository, helpRepository, orderRepository, productRepository, searchRepository, subscriptionRepository, addressRepository)
        )[ProductListViewModel::class.java])
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        offerViewModel = ViewModelProvider(this,
            GroceryAppViewModelFactory(userRepository, authenticationRepository, cartRepository, helpRepository, orderRepository, productRepository, searchRepository, subscriptionRepository, addressRepository)
        )[OfferViewModel::class.java]
        if(FilterFragment.list!=null){
            if(FilterFragment.list!!.isEmpty()){
                noItemsFoundImageText.visibility = View.VISIBLE
                noItemsFoundImage.visibility =View.VISIBLE
            }
            else{
                noItemsFoundImageText.visibility = View.GONE
                noItemsFoundImage.visibility =View.GONE

            }
            adapter.setProducts(FilterFragment.list!!)
            if(offerList.adapter==null) {
                offerList.adapter = adapter
                offerList.layoutManager = LinearLayoutManager(context)
            }
        }
        offerViewModel.getOfferedProducts()


        offerViewModel.offeredProductEntityList.observe(viewLifecycleOwner){ offeredProductList ->

            realList = offeredProductList.toMutableList()
//            products = realList
            if(productEntities.isNotEmpty()){
            }
            if(productEntities.isEmpty()) {
                productEntities = offeredProductList
            }
            if(FilterFragment.list==null){
                if(productEntities.isEmpty()){
                    noItemsFoundImageText.visibility = View.VISIBLE
                    noItemsFoundImage.visibility =View.VISIBLE
                }
                else{
                    noItemsFoundImageText.visibility = View.GONE
                    noItemsFoundImage.visibility =View.GONE

                }
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
        if(offerFilterCount !=0){
            filterCount.text = offerFilterCount.toString()
            filterCount.visibility = View.VISIBLE
        }
        else{
            filterCount.visibility = View.INVISIBLE
        }
        val filterBadge = BadgeDrawable.create(requireContext())
        filterBadge.number = 10
        filterBadge.badgeTextColor = Color.WHITE
        filterBadge.backgroundColor = Color.RED
        BadgeUtils.attachBadgeDrawable(filterBadge,filterButton)

        filterButton.setOnClickListener {
            offerFilterCount = 0
//            FilterFragment.list = realList.toMutableList()
            productEntities = realList
            if(FilterFragment.list!=null) {
                FragmentTransaction.navigateWithBackstack(
                    parentFragmentManager,
                    FilterFragment(realList),
                    "Filter"
                )
            }
            else{
                FragmentTransaction.navigateWithBackstack(
                    parentFragmentManager,
                    FilterFragment(realList),
                    "Filter"
                )
            }
        }

        sortButton.setOnClickListener {
            val bottomSheet = BottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager,"Bottom Sort Sheet")
        }
        val sorter  = ProductSorter()
        BottomSheetDialogFragment.selectedOption.observe(viewLifecycleOwner){
            var newList: List<Product> = mutableListOf()
            if(it==0){
                if(FilterFragment.list==null) {
                    newList = sorter.sortByDate(productEntities)
                }
                else{
                    newList = sorter.sortByDate(FilterFragment.list!!)
                }
                adapter.setProducts(newList)
            }
            else if(it == 1){
                if(FilterFragment.list==null) {
                    newList = sorter.sortByExpiryDate(productEntities)
                }
                else{
                    newList = sorter.sortByExpiryDate(FilterFragment.list!!)
                }
                adapter.setProducts(newList)
            }
            else if(it == 2){
                if(FilterFragment.list==null) {
                    newList = sorter.sortByDiscount(productEntities)
                }
                else{
                    newList = sorter.sortByDiscount(FilterFragment.list!!)
                }
                adapter.setProducts(newList)
            }
            else if(it == 3){
                if(FilterFragment.list==null) {
                    newList = sorter.sortByPriceLowToHigh(productEntities)
                }
                else{
                    newList = sorter.sortByPriceLowToHigh(FilterFragment.list!!)
                }
                adapter.setProducts(newList)
            }
            else if(it == 4){
                if(FilterFragment.list==null) {
                    newList = sorter.sortByPriceHighToLow(productEntities)
                }
                else{
                    newList = sorter.sortByPriceHighToLow(FilterFragment.list!!)
                }
                adapter.setProducts(newList)
            }
            if(newList.isNotEmpty()){
                productEntities = newList
                if(FilterFragment.list!=null){
                    if(FilterFragment.list!!.size==newList.size){
                        FilterFragment.list = newList.toMutableList()
                    }
                }
            }
            offerList.layoutManager?.let {layoutManager ->
                (layoutManager as LinearLayoutManager).scrollToPosition(0)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if(FilterFragment.list!=null){
            adapter.setProducts(FilterFragment.list!!)
            offerList.adapter = adapter
            offerList.layoutManager = LinearLayoutManager(context)
        }
        else{
//            adapter.setProducts(products)
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
        offerListFirstVisiblePos = (offerList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        offerList.stopScroll()
    }

    override fun onPause() {
        super.onPause()
    }



    override fun onDestroy() {
        super.onDestroy()
        FilterFragment.list = null
        offerFilterCount = 0
        offerListFirstVisiblePos = null
        ProductListFragment.productListFilterCount = 0
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
}

