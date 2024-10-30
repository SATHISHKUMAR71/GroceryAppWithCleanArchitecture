package com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
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
import com.example.shoppinggroceryapp.views.sharedviews.filter.ResetFilterValues
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
    lateinit var filterButton:MaterialButton
//    private lateinit var noItemsImage:ImageView
    private lateinit var notifyNoItems:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BottomSheetDialogFragment.selectedOption.value = null
        category = arguments?.getString("category")
        FilterFragment.list = null
        ResetFilterValues.resetFilterValues()
//        OfferFragment.offerFilterCount = 0
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

        val view =  inflater.inflate(R.layout.fragment_product_list, container, false)
        ProductDetailFragment.selectedProductEntityList = mutableListOf()
        fileDir = File(requireContext().filesDir,"AppImages")
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        productListViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java]
        adapter = ProductListAdapter(this,fileDir,"P",false,productListViewModel,null)
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        filterCountText = view.findViewById(R.id.filterCountTextView)
        toolbar = view.findViewById<MaterialToolbar>(R.id.productListToolBar)
        if(FilterFragment.badgeNumber !=0){
            filterCountText.text = FilterFragment.badgeNumber.toString()
            filterCountText.visibility = View.VISIBLE
        }
        else{
            filterCountText.visibility = View.INVISIBLE
        }
        val badgeDrawableListFragment = BadgeDrawable.create(requireContext())
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

//                    parentFragmentManager.popBackStack("Cart Fragment",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                        CartFragment(),"Cart Fragment")
                }
            }
            true
        }
        productRV = view.findViewById(R.id.productListRecyclerView)
        notifyNoItems = view.findViewById(R.id.notifyNoItemsAvailable)
//        noItemsImage = view.findViewById(R.id.noItemsFound)
        val totalCostButton = view.findViewById<MaterialButton>(R.id.totalPriceWorthInCart)
        val exploreCategoryButton = view.findViewById<MaterialButton>(R.id.categoryButtonProductList)
        val sortButton = view.findViewById<MaterialButton>(R.id.sortButton)
        filterButton = view.findViewById<MaterialButton>(R.id.filterButton)

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
            productEntityList = realProductEntityList
            var filterFragment = FilterFragment(realProductEntityList).apply {
                arguments = Bundle().apply { putString("category",category) }
            }
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,filterFragment,"Filter Fragment")
        }
        totalCost.observe(viewLifecycleOwner){
            val str ="₹"+ (it?:0).toString()
            totalCostButton.text =str
        }
        totalCostButton.setOnClickListener {
//            for(i in 0 until parentFragmentManager.backStackEntryCount){
//
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, CartFragment(),"Cart Fragment")
        }
        exploreCategoryButton.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, CategoryFragment(),"Category Fragment")
        }
        attachBadge()

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
            println("989898 adapter set product called on on line 224")
            adapter.setProducts(FilterFragment.list!!)
            if(FilterFragment.list!!.size==0){
                hideProductRV()
            }
            else{
                println("767623 show products called on 232")
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
            }
            for (i in it){
            }
            if(FilterFragment.list==null) {
                if(BottomSheetDialogFragment.selectedOption.value==null) {
                    println("989898 adapter set product called on line 260")
                    adapter.setProducts(it)
                    if (it.isEmpty()) {
                        hideProductRV()
                    }
                    else{
                        println("767623 show products called on 268")
                        showProductRV()
                    }
                }
                else{
                    println("989898 adapter set product called on line 270")
                    adapter.setProducts(productEntityList)
                    if (productEntityList.isEmpty()) {
                        hideProductRV()
                    }
                    else{
                        println("767623 show products called on 279")
                        showProductRV()
                    }
                }
                if (productRV.adapter == null) {
                    productRV.adapter = adapter
                    productRV.layoutManager = LinearLayoutManager(context)
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
                    println("989898 adapter set product called on line 293 $it ${it.isEmpty()}")
                    adapter.setProducts(it)
                    if (it.isEmpty()) {
                        hideProductRV()
                    }
                    else{
                        println("767623 show products called on 303")
                        showProductRV()
                    }
                }
                else{
                    println("989898 adapter set product called on line 303")
                    adapter.setProducts(productEntityList)
                    if (productEntityList.size == 0) {
                        hideProductRV()
                    }
                    else{
                        println("767623 show products called on 314 ")
                        showProductRV()
                    }
                }
                if (productRV.adapter == null) {
                    productRV.adapter = adapter
                    productRV.layoutManager = LinearLayoutManager(requireContext())
                }
//                if (productEntityList.size == 0) {
//                    hideProductRV()
//                }
//                else{
//                    println("767623 show products called on 326")
//                    showProductRV()
//                }
            }
        }
        sortButton.setOnClickListener {
            val bottomSheet = BottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager,"Bottom Sort Sheet")
        }

        BottomSheetDialogFragment.selectedOption.observe(viewLifecycleOwner){
            if(it!=null) {
                productListViewModel.doSorting(adapter,it,productEntityList,ProductSorter())?.let {list ->
                    productEntityList = list.toMutableList()
                }
                productRV.layoutManager?.let { layoutManager ->
                    (layoutManager as LinearLayoutManager).scrollToPosition(productListFirstVisiblePos?:0)
                }
            }
        }
//        val onBackPressedCallback = object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                parentFragmentManager.popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("#232 tag set on destroy in product list category called ")
    }
    @OptIn(ExperimentalBadgeUtils::class)
    private fun attachBadge() {
        val filterBadge = BadgeDrawable.create(requireContext())
        filterBadge.number = 10
        filterBadge.badgeTextColor = Color.WHITE
        filterBadge.backgroundColor = Color.RED
        BadgeUtils.attachBadgeDrawable(filterBadge,filterButton)
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
                FragmentTransaction.navigateWithBackstack(parentFragmentManager, AddOrEditProductFragment(),"Add Or Edit Fragment")
            }
            view?.findViewById<CardView>(R.id.linearLayout8)?.visibility = View.GONE
        }
        else{
            fab?.visibility = View.GONE
            view?.findViewById<CardView>(R.id.linearLayout8)?.visibility = View.VISIBLE
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
            println("989898 adapter set product called on line 393")
            adapter.setProducts(FilterFragment.list!!)
            checkDeletedItem()
            println("989898 adapter set product called on line 396")
            adapter.setProducts(FilterFragment.list!!)
            if (FilterFragment.list!!.size == 0) {
                hideProductRV()
            }
            else{
                println("767623 show products called on 409")
                showProductRV()
            }
        }

//        if(FilterFragment.list?.isNotEmpty()==true){
//            println("989898 adapter set product called on line 407")
//            adapter.setProducts(FilterFragment.list!!)
//            if (FilterFragment.list!!.size == 0) {
//                hideProductRV()
//            }
//            else{
//                println("767623 show products called on 421")
//                showProductRV()
//            }
//        }
//        else if (productEntityList.isNotEmpty()) {
//            println("989898 adapter set product called on line 417")
//            adapter.setProducts(productEntityList)
//            if (productEntityList.size == 0) {
//                hideProductRV()
//            }
//            else{
//                println("767623 show products called on 432 ")
//                showProductRV()
//            }
//        }

        productRV.scrollToPosition(productListFirstVisiblePos ?: 0)
        if(arguments?.getBoolean("isEdit")==true){
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentMainLayout, AddOrEditProductFragment())
                .addToBackStack("Product Detail Fragment")
                .commit()
        }
        println("9090980 product list: $productEntityList")
    }


    override fun onPause() {
        super.onPause()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
//        productListFirstVisiblePos = (productRV.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        productListFirstVisiblePos = (productRV.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
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
//        OfferFragment.offerFilterCount = 0
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

    private fun showProductRV() {
        println("89890090 show called")
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

    }

    private fun hideProductRV(){
        println("89890090 before HIDE PRODUCT RV IS CALLED ${productRV.isVisible} ${notifyNoItems.isVisible}")
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
        println("89890090 after HIDE PRODUCT RV IS CALLED ${productRV.isVisible} ${notifyNoItems.isVisible}")
    }
}