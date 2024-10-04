package com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.MutableLiveData
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
import com.example.shoppinggroceryapp.MainActivity
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
import com.example.shoppinggroceryapp.views.userviews.cartview.FindNumberOfCartItems
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.example.shoppinggroceryapp.views.userviews.offer.OfferFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.retailerviews.addeditproduct.AddOrEditProductFragment
import com.example.shoppinggroceryapp.views.sharedviews.sort.BottomSheetDialogFragment
import com.example.shoppinggroceryapp.views.sharedviews.sort.ProductSorter
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail.ProductDetailFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


class ProductListFragment : Fragment() {
    companion object{
        var selectedProductEntity:MutableLiveData<Product> = MutableLiveData()
        var selectedPos:Int? = null
        var totalCost:MutableLiveData<Float> = MutableLiveData(0f)
        var position = 0
        var dis50Val: Boolean? = null
        var dis40Val: Boolean? = null
        var dis30Val: Boolean? = null
        var dis20Val: Boolean? = null
        var dis10Val: Boolean? = null
        var productListFirstVisiblePos:Int? = null
        var productListFilterCount =0
    }
    private var firstTime = 0
    var category:String? = null
    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var fileDir:File
    private var realProductEntityList = mutableListOf<Product>()
    private lateinit var filterCountText:TextView
    private lateinit var productRV:RecyclerView
    var searchViewOpened = false
    private lateinit var selectedProductEntity: Product
    private lateinit var toolbar:MaterialToolbar
    private var productEntityList:MutableList<Product> = mutableListOf()
    private lateinit var adapter: ProductListAdapter
    private lateinit var noItemsImage:ImageView
    private lateinit var notifyNoItems:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BottomSheetDialogFragment.selectedOption.value = null
        category = arguments?.getString("category")
        FilterFragment.list = null
        productListFilterCount = 0
        OfferFragment.offerFilterCount = 0
        OfferFragment.dis10Val = false
        OfferFragment.dis20Val = false
        OfferFragment.dis30Val = false
        OfferFragment.dis40Val = false
        OfferFragment.dis50Val =false
        dis10Val = false
        firstTime = 0
        dis20Val = false
        dis30Val = false
        dis40Val = false
        dis50Val = false
    }

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        println("ON CREATE VIEW CALLED")
        val view =  inflater.inflate(R.layout.fragment_product_list, container, false)

        fileDir = File(requireContext().filesDir,"AppImages")
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        productListViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java]
        adapter = ProductListAdapter(this,fileDir,"P",false,productListViewModel)
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        filterCountText = view.findViewById(R.id.filterCountTextView)
        toolbar = view.findViewById<MaterialToolbar>(R.id.productListToolBar)
        if(productListFilterCount !=0){
            filterCountText.text = productListFilterCount.toString()
            filterCountText.visibility = View.VISIBLE
        }
        else{
            filterCountText.visibility = View.INVISIBLE
        }
        var badgeDrawableListFragment = BadgeDrawable.create(requireContext())
        toolbar.setTitle(category)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        if(MainActivity.isRetailer){
            toolbar.menu.findItem(R.id.cart).isVisible = false
        }
        else{
            toolbar.menu.findItem(R.id.cart).isVisible = true
        }
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.searchProductInProductList -> {
                    InitialFragment.openSearchView.value = true
                }
                R.id.mic ->{
                    InitialFragment.openMicSearch.value = true
                }
                R.id.cart ->{
                    FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                        CartFragment(),"Going to cart")
                }
            }
            true
        }
        productRV = view.findViewById(R.id.productListRecyclerView)
        notifyNoItems = view.findViewById(R.id.notifyNoItemsAvailable)
        noItemsImage = view.findViewById(R.id.noItemsFound)
        val totalCostButton = view.findViewById<MaterialButton>(R.id.totalPriceWorthInCart)
        val exploreCategoryButton = view.findViewById<MaterialButton>(R.id.categoryButtonProductList)
        val sortButton = view.findViewById<MaterialButton>(R.id.sortButton)
        val filterButton = view.findViewById<MaterialButton>(R.id.filterButton)

        searchViewOpened = (arguments?.getBoolean("searchViewOpened")==true)
        productListViewModel.getCartItems(MainActivity.cartId.toInt())

        productListViewModel.cartEntityList.observe(viewLifecycleOwner){
            FindNumberOfCartItems.productCount.value = it.size
        }

        FindNumberOfCartItems.productCount.observe(viewLifecycleOwner){
            badgeDrawableListFragment.text = it.toString()
            if(it!=0) {
                badgeDrawableListFragment.isVisible = true
                BadgeUtils.attachBadgeDrawable(badgeDrawableListFragment, toolbar, R.id.cart)
            }
            else{
                badgeDrawableListFragment.isVisible = false
            }
        }
        filterButton.setOnClickListener {
            productListFilterCount = 0
            productEntityList = realProductEntityList
            var filterFragment = FilterFragment(realProductEntityList).apply {
                arguments = Bundle().apply { putString("category",category) }
            }
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,filterFragment,"Filter")
        }
        totalCost.observe(viewLifecycleOwner){
            val str ="₹"+ (it?:0).toString()
            totalCostButton.text =str
        }
        totalCostButton.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, CartFragment(),"Going to cart")
        }
        exploreCategoryButton.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, CategoryFragment(),"Exploring Category")
        }

        totalCost.value = 0f
        productListViewModel.getCartItems(cartId = MainActivity.cartId)
        productListViewModel.cartEntityList.observe(viewLifecycleOwner){
            for(cart in it){
                val totalItems = cart.totalItems
                val price = cart.unitPrice
                totalCost.value = totalCost.value!!+(totalItems * price)
            }
        }


        if(FilterFragment.list!=null){
            if(productRV.adapter==null) {
                productRV.adapter = adapter
                productRV.layoutManager = LinearLayoutManager(requireContext())
            }
            adapter.setProducts(FilterFragment.list!!)
            if(FilterFragment.list!!.size==0){
                hideProductRV()
            }
            else{
                showProductRV()
            }
        }
        if(category==null){
            productListViewModel.getOnlyProducts()
        }
        else{
            productListViewModel.getProductsByCategory(category!!)
        }
        productListViewModel.productEntityList.observe(viewLifecycleOwner){
            if(productEntityList.isEmpty()) {
                productEntityList = it.toMutableList()
            }
            if(BottomSheetDialogFragment.selectedOption.value==null){
                productEntityList  = it.toMutableList()
            }
            BottomSheetDialogFragment.selectedOption.value?.let {data ->

                productEntityList  = it.toMutableList()
                BottomSheetDialogFragment.selectedOption.value = data
            }


            realProductEntityList = it.toMutableList()
            for (i in productEntityList){
                println("AAAAA ${i.productName}")
            }
            for (i in it){
                println("AAAAAB ${i.productName}")
            }
            if(FilterFragment.list==null) {
                if(BottomSheetDialogFragment.selectedOption.value==null) {
                    println("AAAAA  adapter if called")
                    adapter.setProducts(it)
                }
                else{
                    println("AAAAA  adapter else called")
                    adapter.setProducts(productEntityList)
                }
                if (productRV.adapter == null) {
                    productRV.adapter = adapter
                    productRV.layoutManager = LinearLayoutManager(context)
                }
                if (productEntityList.size == 0) {
                    hideProductRV()
                }
                else{
                    showProductRV()
                }
            }
        }
        productListViewModel.productEntityCategoryList.observe(viewLifecycleOwner){
            if(productEntityList.isEmpty()) {
                productEntityList = it.toMutableList()
            }
            realProductEntityList = it.toMutableList()
            if(FilterFragment.list==null) {
                if(BottomSheetDialogFragment.selectedOption.value==null) {
                    adapter.setProducts(it)
                }
                else{
                    adapter.setProducts(productEntityList)
                }
                if (productRV.adapter == null) {
                    productRV.adapter = adapter
                    productRV.layoutManager = LinearLayoutManager(requireContext())
                }
                if (productEntityList.size == 0) {
                    hideProductRV()
                }
                else{
                    showProductRV()
                }
            }
        }
        sortButton.setOnClickListener {
            val bottomSheet = BottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager,"Bottom Sort Sheet")
        }
        val sorter  = ProductSorter()
        BottomSheetDialogFragment.selectedOption.observe(viewLifecycleOwner){
            if(it!=null) {
                println("AAAAA value of each product: on Bottom Framgnet $it")
                var newList = listOf<Product>()
                if (it == 0) {
                    if (FilterFragment.list == null) {
                        newList = sorter.sortByDate(productEntityList)
                    } else {
                        newList = sorter.sortByDate(FilterFragment.list!!)
                    }
                    adapter.setProducts(newList)
                } else if (it == 1) {
                    if (FilterFragment.list == null) {
                        newList = sorter.sortByExpiryDate(productEntityList)
                    } else {
                        newList = sorter.sortByExpiryDate(FilterFragment.list!!)
                    }
                    adapter.setProducts(newList)
                } else if (it == 2) {
                    if (FilterFragment.list == null) {
                        newList = sorter.sortByDiscount(productEntityList)
                    } else {
                        newList = sorter.sortByDiscount(FilterFragment.list!!)
                    }
                    adapter.setProducts(newList)
                } else if (it == 3) {
                    if (FilterFragment.list == null) {
                        newList = sorter.sortByPriceLowToHigh(productEntityList)
                    } else {
                        newList = sorter.sortByPriceLowToHigh(FilterFragment.list!!)
                    }
                    adapter.setProducts(newList)
                } else if (it == 4) {
                    if (FilterFragment.list == null) {
                        newList = sorter.sortByPriceHighToLow(productEntityList)
                    } else {
                        newList = sorter.sortByPriceHighToLow(FilterFragment.list!!)
                    }
                    adapter.setProducts(newList)
                }
                if (newList.isNotEmpty()) {
                    println("AAAAA List changed")
                    if (FilterFragment.list != null) {
                        if (FilterFragment.list!!.size == newList.size) {
                            FilterFragment.list = newList.toMutableList()
                        }
                    }
                    else{
                        productEntityList = newList.toMutableList()
                    }
                }
                productRV.layoutManager?.let { layoutManager ->
                    println("Scrolled to on create vie: ### $productListFirstVisiblePos")
                    (layoutManager as LinearLayoutManager).scrollToPosition(productListFirstVisiblePos?:0)
                }
            }
        }
        for(i in productEntityList){
            println("I VALUES : ${i.productName}")
        }
        return view
    }


    override fun onResume() {
        super.onResume()
        val fab = view?.findViewById<FloatingActionButton>(R.id.addProductsToInventory)
        if(MainActivity.isRetailer){
            toolbar.visibility =View.GONE
            val params = (view?.layoutParams as FrameLayout.LayoutParams)
            params.setMargins(0,220,0,0)
            view?.layoutParams = params
            fab?.visibility = View.VISIBLE
            view?.findViewById<FloatingActionButton>(R.id.addProductsToInventory)?.setOnClickListener {
                Companion.selectedProductEntity.value = null
                FragmentTransaction.navigateWithBackstack(parentFragmentManager, AddOrEditProductFragment(),"Edit in Product Fragment")
            }
            view?.findViewById<LinearLayout>(R.id.linearLayout8)?.visibility = View.GONE
        }
        else{
            fab?.visibility = View.GONE
            view?.findViewById<LinearLayout>(R.id.linearLayout8)?.visibility = View.VISIBLE
        }
        if(!MainActivity.isRetailer) {
            InitialFragment.hideSearchBar.value = true
            InitialFragment.hideBottomNav.value = true
        }
        if(MainActivity.isRetailer && toolbar.title == null){
            toolbar.visibility =View.GONE
            InitialFragment.hideSearchBar.value = false
            InitialFragment.hideBottomNav.value = false
        }
        else{
            val params = (view?.layoutParams as FrameLayout.LayoutParams)
            params.setMargins(0,0,0,0)
            view?.layoutParams = params
            val fabParams = fab?.layoutParams as CoordinatorLayout.LayoutParams
            fabParams.setMargins(0,0,0,80)
            fab.layoutParams = fabParams
            toolbar.visibility =View.VISIBLE
            InitialFragment.hideSearchBar.value = true
            InitialFragment.hideBottomNav.value = true
        }

        if(InitialFragment.searchQueryList.isNotEmpty()){
            InitialFragment.hideSearchBar.value = true
            InitialFragment.hideBottomNav.value = true
            toolbar.visibility = View.VISIBLE
        }
        if(FilterFragment.list!=null){
            if(productRV.adapter==null) {
                productRV.adapter = adapter
                productRV.layoutManager = LinearLayoutManager(requireContext())
            }
            adapter.setProducts(FilterFragment.list!!)
            checkDeletedItem()
            adapter.setProducts(FilterFragment.list!!)
            if (FilterFragment.list!!.size == 0) {
                hideProductRV()
            }
            else{
                showProductRV()
            }
        }

        if(FilterFragment.list?.isNotEmpty()==true){
            adapter.setProducts(FilterFragment.list!!)
        }
        else if (productEntityList.isNotEmpty()) {
            adapter.setProducts(productEntityList)
        }
        println("Scrolled to : ### $productListFirstVisiblePos")
        productRV.scrollToPosition(productListFirstVisiblePos ?: 0)
    }


    override fun onPause() {
        super.onPause()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
        productListFirstVisiblePos = (productRV.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
        productRV.stopScroll()
        productListViewModel.cartEntityList.value = mutableListOf()
        if(InitialFragment.searchQueryList.size <2){
            InitialFragment.searchHint.value = ""
            InitialFragment.searchQueryList = mutableListOf()
        }
        else{
            InitialFragment.searchHint.value = InitialFragment.searchQueryList[1]
            InitialFragment.searchQueryList.removeAt(0)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        productListFirstVisiblePos =null
        productListFilterCount = 0
        OfferFragment.offerFilterCount = 0
        OfferFragment.dis10Val = false
        OfferFragment.dis20Val = false
        OfferFragment.dis30Val = false
        OfferFragment.dis40Val = false
        OfferFragment.dis50Val =false
        dis10Val = false
        dis20Val = false
        dis30Val = false
        dis40Val = false
        dis50Val = false
    }



    fun checkDeletedItem(){
        try {
            if (ProductDetailFragment.deletePosition != null) {
                productEntityList.removeAt(ProductDetailFragment.deletePosition!!)
                if (FilterFragment.list != null) {
                    FilterFragment.list!!.removeAt(ProductDetailFragment.deletePosition!!)
                }
                productRV.adapter?.notifyItemRemoved(ProductDetailFragment.deletePosition ?: 0)
                ProductDetailFragment.deletePosition = null
            }
        }
        catch (e:Exception){
        }
    }

    private fun showProductRV(){
        productRV.animate()
            .alpha(1f)
            .setDuration(50)
            .withEndAction { productRV.visibility = View.VISIBLE
            }
            .start()
        notifyNoItems.animate()
            .alpha(0f)
            .setDuration(50)
            .withEndAction { notifyNoItems.visibility = View.GONE }
            .start()
        noItemsImage.animate()
            .alpha(0f)
            .setDuration(50)
            .withEndAction { noItemsImage.visibility = View.GONE }
            .start()
    }

    private fun hideProductRV(){
        productRV.animate()
            .alpha(0f)
            .setDuration(50)
            .withEndAction { productRV.visibility = View.GONE }
            .start()
        notifyNoItems.animate()
            .alpha(1f)
            .setDuration(50)
            .withEndAction { notifyNoItems.visibility = View.VISIBLE }
            .start()
        noItemsImage.animate()
            .alpha(1f)
            .setDuration(50)
            .withEndAction { noItemsImage.visibility = View.VISIBLE }
            .start()
    }
}