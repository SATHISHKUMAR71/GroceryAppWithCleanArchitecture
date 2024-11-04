package com.example.shoppinggroceryapp.views.initialview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.data.datasource.productdatasource.RetailerProductDataSource
import com.core.data.repository.ProductRepository
import com.core.usecases.productusecase.getproductusecase.GetOfferedProducts
import com.core.usecases.productusecase.getproductusecase.GetParentAndChildCategories
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.MainActivity.Companion.isRetailer
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.data.product.ProductDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.PutExtras
import com.example.shoppinggroceryapp.views.userviews.cartview.FindNumberOfCartItems
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.permissionhandler.MicPermissionHandler
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.retailerviews.addeditproduct.AddOrEditProductFragment
import com.example.shoppinggroceryapp.views.sharedviews.authenticationviews.signup.SignUpFragment
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.CustomerRequestListFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterPrice
import com.example.shoppinggroceryapp.views.sharedviews.search.SearchViewModel
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.OrderHistoryFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.profileviews.AccountFragment
import com.example.shoppinggroceryapp.views.sharedviews.search.adapter.SearchListAdapter
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.example.shoppinggroceryapp.views.userviews.home.HomeFragment
import com.example.shoppinggroceryapp.views.userviews.offer.OfferFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale


class InitialFragment : Fragment() {

    private lateinit var bottomNav:BottomNavigationView
    private lateinit var searchView:SearchView
    private lateinit var searchBar:SearchBar
    private lateinit var homeFragment: Fragment
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var searchViewModel:SearchViewModel
    private lateinit var permissionHandler: com.example.shoppinggroceryapp.helpers.permissionhandler.interfaces.MicPermissionHandler
    companion object{
        private var searchString =""
        var category = ""
        var searchedQuery:MutableLiveData<String> = MutableLiveData()
        var openSearchView:MutableLiveData<Boolean> = MutableLiveData()
        var openMicSearch:MutableLiveData<Boolean> = MutableLiveData()
        var searchHint:MutableLiveData<String> =MutableLiveData()
        var searchQueryList = mutableListOf<String>()
        var hideSearchBar:MutableLiveData<Boolean> = MutableLiveData()
        var hideBottomNav:MutableLiveData<Boolean> = MutableLiveData()
        var closeSearchView:MutableLiveData<Boolean> = MutableLiveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchString = ""
        openMicSearch.value = false
        openSearchView.value =false
        searchListAdapter = SearchListAdapter(this)
        permissionHandler = MicPermissionHandler(
            this
        )
        permissionHandler.initMicResults()
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val retailerDao = db1.getRetailerDao()
        val productDataSource = ProductDataSourceImpl(retailerDao)
        val productRepository = ProductRepository(productDataSource,productDataSource)
        SetInitialDataForUser().loadImages(this, GetOfferedProducts(productRepository),
            GetParentAndChildCategories(productRepository), File(requireContext().filesDir,"AppImages"))
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_initial, container, false)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        lifecycleScope.launch (Dispatchers.IO){
            FilterPrice.MAX_PRICE_VALUE =userDao.getMaxPrice().price
            FilterPrice.priceEndTo = FilterPrice.MAX_PRICE_VALUE
        }
        searchViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[SearchViewModel::class.java]
        bottomNav = view.findViewById(R.id.bottomNav)
        searchBar = view.findViewById(R.id.searchBar)
        searchView = view.findViewById(R.id.searchView)
        searchView.setupWithSearchBar(searchBar)

        searchHint.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                searchBar.hint = it
            }
            else{
                searchBar.hint = "Search Products"
            }
        }


        val launchMicResults = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val micResult = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val textOutput = micResult?.get(0).toString()
                searchView.show()
                searchView.editText.setText(textOutput)
                searchView.editText.setSelection(textOutput.length)
            }
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Product Name")
        }
        openSearchView.observe(viewLifecycleOwner){
            if(it) {
                searchView.editText.setText("")
                searchView.show()
            }
            else{
                searchView.hide()
            }
        }
        searchBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.searchBarMic ->{
                    if(permissionHandler.checkMicPermission(launchMicResults,intent)==true){
                        launchMicResults.launch(intent)
                    }
                }
            }
            true
        }
        openMicSearch.observe(viewLifecycleOwner){
            if(it){
                if(permissionHandler.checkMicPermission(launchMicResults,intent)==true){
                    launchMicResults.launch(intent)
                }
            }
        }
        homeFragment = HomeFragment()
        val customerRequestListFragment = CustomerRequestListFragment()

        val pref = requireActivity().getSharedPreferences("freshCart", Context.MODE_PRIVATE)
        SetInitialDataForUser().invoke(pref)


        if(isRetailer){
            bottomNav.menu.clear()
            bottomNav.inflateMenu(R.menu.admin_menu)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentMainLayout,customerRequestListFragment)
                .commit()
            parentFragmentManager.registerFragmentLifecycleCallbacks(object :FragmentManager.FragmentLifecycleCallbacks(){
                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    when (f){
                        is ProductListFragment ->{
                            bottomNav.menu.findItem(R.id.inventory).isChecked = true
                        }
                        is SignUpFragment -> bottomNav.menu.findItem(R.id.addOtherAdmin).isChecked = true
                        is OrderHistoryFragment -> {
                            bottomNav.menu.findItem(R.id.ordersReceived).isChecked = true
                        }
                        is CustomerRequestListFragment -> {
                            bottomNav.menu.findItem(R.id.customerRequest).isChecked = true
                        }
                        is AccountFragment ->{
                            bottomNav.menu.findItem(R.id.account).isChecked = true
                        }
                    }
                }
            },true)
            bottomNav.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.inventory -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            ProductListFragment(),"Product List Fragment")
                    }
                    R.id.customerRequest -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,customerRequestListFragment,"Customer Request List Fragment")
                    }
                    R.id.addOtherAdmin->{
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            SignUpFragment(),"Adding Other Admin Fragment")
                    }
                    R.id.account-> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            AccountFragment(),"Account Fragment")
                    }
                    R.id.ordersReceived -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            OrderHistoryFragment(),"Orders History Fragment")
                    }
                }
                true
            }
        }
        else{
            if(savedInstanceState==null) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentMainLayout, homeFragment,"Home Fragment")
                    .commit()
            }
            parentFragmentManager.registerFragmentLifecycleCallbacks(object :FragmentManager.FragmentLifecycleCallbacks(){
                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    when (f){
                        is AccountFragment -> {
                            bottomNav.menu.findItem(R.id.account).isChecked = true
                        }
                        is CategoryFragment -> {
                            bottomNav.menu.findItem(R.id.category).isChecked = true
                        }
                        is HomeFragment -> {
                            bottomNav.menu.findItem(R.id.homeMenu).isChecked = true
                        }
                        is CartFragment -> {
                            bottomNav.menu.findItem(R.id.cart).isChecked = true
                        }
                        is OfferFragment -> {
                            bottomNav.menu.findItem(R.id.offer).isChecked = true
                        }
                    }
                }
            },true)

            bottomNav.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.account -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            AccountFragment(),"Account Fragment")
                    }
                    R.id.cart -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            CartFragment(),"Cart Fragment")
                    }
                    R.id.homeMenu -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,homeFragment,"Home Fragment")
                    }
                    R.id.offer -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            OfferFragment(),"Offer Fragment")
                    }
                    R.id.category -> {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            CategoryFragment(),"Category Fragment")
                    }
                }
                true
            }
        }

        parentFragmentManager.registerFragmentLifecycleCallbacks(object :FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentPreCreated(
                fm: FragmentManager,
                f: Fragment,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentPreCreated(fm, f, savedInstanceState)
                println("#232 tag set on create: ${f.tag}")
            }
        },true)

        parentFragmentManager.registerFragmentLifecycleCallbacks(object :FragmentManager.FragmentLifecycleCallbacks(){

            override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentDestroyed(fm, f)
                println("#232 tag set on destroy: tag name ${f.tag}")
            }
        },true)

        searchViewModel.getSearchedList()
        searchedQuery.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                searchViewModel.addItemInDb(it)
            }
        }

        var backPressedCallback = object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(searchView.isShowing){
                    searchView.hide()
                }
                else{
                    println("8769283 REMOVING THE FRAGMENTS ON ELSE")
                    println("ON ORDER SUCCESS FRAGMENT BACKUP in initial")
                    isEnabled = false

                    requireActivity().onBackPressed()
//                    if(homeFragment.isVisible){
//                        isEnabled = false
//                        requireActivity().onBackPressed()
//                    }
//                    else{
//                        println("REMOVING THE FRAGMENTS ON IF")
//                        parentFragmentManager.popBackStack("SubFragment From Home",FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,backPressedCallback)
        val cartListViewModel = ViewModelProvider(this,GroceryAppSharedVMFactory(retailerDao, userDao))[ProductListViewModel::class.java]
        if(!isRetailer){
            cartListViewModel.getCartItems(MainActivity.cartId)
        }
        cartListViewModel.cartEntityList.observe(viewLifecycleOwner){
            if(!isRetailer){
                FindNumberOfCartItems.productCount.value = it.size
            }
        }
        FindNumberOfCartItems.productCount.observe(viewLifecycleOwner){
            if(!isRetailer){
                if(it!=0) {
                    bottomNav.getOrCreateBadge(R.id.cart).isVisible = true
                    bottomNav.getOrCreateBadge(R.id.cart).text = it.toString()
                }
                else{
                    bottomNav.getOrCreateBadge(R.id.cart).isVisible = false
                }
            }
        }
        val searchRecyclerView = view.findViewById<RecyclerView>(R.id.searchRecyclerView)
        searchView.editText.addTextChangedListener {
            searchString = it.toString()
            if (it?.isNotEmpty() == true) {
                searchViewModel.performSearch(it.toString())
            }
            else if(it.toString().isEmpty()){
                searchViewModel.getSearchedList()
            }
            else{
                searchViewModel.performSearch("-1")
            }
        }


        searchViewModel.searchedList.observe(viewLifecycleOwner){ searchList ->

            if(searchString.isEmpty() && searchList.isEmpty()){
                SearchListAdapter.searchList = mutableListOf("Frozen Pizza","Cake Mixes","Chocolate Cake","Almond Milk","Frozen Veggie Burgers")
            }
            else if(searchList.isEmpty() && searchString.isNotEmpty()){
                SearchListAdapter.searchList = mutableListOf("No Products Found")
            }
            else {
                SearchListAdapter.searchList = searchList.toMutableList()
            }
            searchRecyclerView.adapter = SearchListAdapter(this)
            searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

//        val searchBarTop = view.findViewById<LinearLayout>(R.id.searchBarTop)

        closeSearchView.observe(viewLifecycleOwner){
            if(it){
                searchView.hide()

            }
        }

        searchView.addTransitionListener { searchView, previousState, newState ->

            if(newState==SearchView.TransitionState.SHOWING){
                backPressedCallback.isEnabled = true
            }
            if(newState==SearchView.TransitionState.HIDDEN){
                if(category.isNotEmpty()) {
                    var productListFragment = ProductListFragment()
                    productListFragment.arguments = Bundle().apply {
                        putBoolean("searchViewOpened", true)
                        putString("category", category)
                    }
                    FragmentTransaction.navigateWithBackstack(
                        parentFragmentManager,
                        productListFragment,
                        category
                    )
                    category =""
                }
            }
        }

        hideSearchBar.observe(viewLifecycleOwner){
            println("321432 INITIAL FRAGMENT Hide search bar $it")
            if(it){
                view.findViewById<AppBarLayout>(R.id.appbarLayout).visibility = View.GONE
//                searchBarTop.visibility = View.GONE
            }
            else{

                view.findViewById<AppBarLayout>(R.id.appbarLayout).visibility = View.VISIBLE
//                searchBarTop.visibility = View.VISIBLE
            }
        }
        hideBottomNav.observe(viewLifecycleOwner){
            println("321432 INITIAL FRAGMENT Hide bottom nav bar $it")
            if(it){
                bottomNav.visibility = View.GONE
            }
            else{
                bottomNav.visibility =View.VISIBLE
            }
        }

        bottomNav.setOnItemReselectedListener {
        }
        if(arguments?.getBoolean("isEdit")==true){
            println("INTENT PRODUCT: ON TRUE")
            arguments?.let {
                ProductListFragment.selectedProductEntity.value = PutExtras.getProductFromExtras(it)
            }
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,AddOrEditProductFragment().apply { arguments = Bundle().apply { putBoolean("isEdit",true) } },"Add Or Edit Fragment")
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        searchedQuery = MutableLiveData()
    }

    override fun onResume() {
        super.onResume()
        println("321432 INITIAL FRAGMENT ON RESUME ")
    }
    override fun onStop() {
        super.onStop()
        println("321432 INITIAL FRAGMENT ON STOP ")
    }

    override fun onPause() {
        super.onPause()
        println("321432 INITIAL FRAGMENT ON Pause ")
    }

}