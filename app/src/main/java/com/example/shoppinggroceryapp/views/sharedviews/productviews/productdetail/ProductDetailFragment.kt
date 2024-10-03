package com.example.shoppinggroceryapp.views.sharedviews.productviews.productdetail

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
import com.core.domain.order.Cart
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
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.views.userviews.cartview.FindNumberOfCartItems
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.example.shoppinggroceryapp.views.retailerviews.addeditproduct.AddOrEditProductFragment
import com.example.shoppinggroceryapp.views.GroceryAppViewModelFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductImageAdapter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File


class ProductDetailFragment : Fragment() {


    private var countOfOneProduct = 0
    private lateinit var imageLoader: ImageLoaderAndGetter
//    private lateinit var cartViewModel: CartViewModel
    private lateinit var recyclerView:RecyclerView
    private lateinit var productDetailToolBar:MaterialToolbar
    private lateinit var mrpTextView:TextView
    private lateinit var discountedPrice:TextView
    private lateinit var badgeDrawable:BadgeDrawable
    private lateinit var addProductButton:MaterialButton
    private lateinit var totalItemsAddedProductDetail:TextView
    private lateinit var removeProductImgButton:ImageButton
    private lateinit var addProductImgButton:ImageButton
    private lateinit var addRemoveLayout:LinearLayout
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    var once = 0
    var oneTimeFragmentIn = -1
    var backNavigated = false
    var selectedProductEntityInClass: Product? = null
    var productObserved = 0
    companion object{
        var brandData:MutableLiveData<String> = MutableLiveData()
        var selectedProductEntityList = mutableListOf<Product>()
        var deletePosition:Int? = null
//        var productCount:MutableLiveData<Int> = MutableLiveData(0)
    }
    private lateinit var productDetailViewModel: ProductDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLoader = ImageLoaderAndGetter()
        oneTimeFragmentIn = 0
    }

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_product_detail, container, false)
        println("ON PRODUCT DETAIL VALUE: ${ProductListFragment.selectedProductEntity.value}")
        val viewPager = view.findViewById<ViewPager2>(R.id.productImageViewer)
        productDetailToolBar = view.findViewById<MaterialToolbar>(R.id.productDetailToolbar)
//        cartViewModel = CartViewModel(AppDatabase.getAppDatabase(requireContext()).getUserDao())
        mrpTextView = view.findViewById<TextView>(R.id.productPriceProductDetail)
        discountedPrice = view.findViewById<TextView>(R.id.discountedPrice)
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

        productDetailViewModel = ViewModelProvider(this,
            GroceryAppViewModelFactory(userRepository, authenticationRepository, cartRepository, helpRepository, orderRepository, productRepository, searchRepository, subscriptionRepository, addressRepository)
        )[ProductDetailViewModel::class.java]
        var productListViewModel = ViewModelProvider(this,
            GroceryAppViewModelFactory(userRepository, authenticationRepository, cartRepository, helpRepository, orderRepository, productRepository, searchRepository, subscriptionRepository, addressRepository)
        )[ProductListViewModel::class.java]
        productDetailViewModel.getImagesForProducts(ProductListFragment.selectedProductEntity.value?.productId?:0)
        addProductButton = view.findViewById(R.id.addProductButtonProductDetail)
        totalItemsAddedProductDetail = view.findViewById(R.id.totalItemsAddedProductDetail)
        removeProductImgButton = view.findViewById(R.id.productRemoveSymbolButtonProductDetail)
        addProductImgButton = view.findViewById(R.id.productAddSymbolButtonProductDetail)
        addRemoveLayout = view.findViewById(R.id.productAddRemoveLayoutProductDetail)
        productDetailViewModel.imageList.observe(viewLifecycleOwner){
            for(i in it){
            }

        }

        if(MainActivity.isRetailer){
            productDetailToolBar.menu.findItem(R.id.edit).setVisible(true)
            productDetailToolBar.menu.findItem(R.id.delete).setVisible(true)
            productDetailToolBar.menu.findItem(R.id.cart).setVisible(false)
//            view.findViewById<LinearLayout>(R.id.similarProductsLayout).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.exploreBottomLayout).visibility = View.GONE
        }

        else{
            productDetailToolBar.menu.findItem(R.id.edit).setVisible(false)
            productDetailToolBar.menu.findItem(R.id.cart).setVisible(true)
            productDetailToolBar.menu.findItem(R.id.delete).setVisible(false)
//            view.findViewById<LinearLayout>(R.id.similarProductsLayout).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.exploreBottomLayout).visibility = View.VISIBLE
        }


        productDetailToolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.cart -> {
                    if (!MainActivity.isRetailer) {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            CartFragment(),"Cart In Product Detail")
                    }
                }
                R.id.edit -> {
                    if (MainActivity.isRetailer) {
                        FragmentTransaction.navigateWithBackstack(parentFragmentManager,
                            AddOrEditProductFragment(),"Edit in Product Detail")
                    }
                }
                R.id.delete -> {
                    if(MainActivity.isRetailer){
                        var alertDialog =AlertDialog.Builder(context)
                            .setTitle("Delete Product!")
                            .setMessage("Are you Sure to delete this product in Inventory?")
                            .setNegativeButton("No"){dialog,which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("Yes"){dialog,which ->
                                ProductListFragment.selectedProductEntity.value?.let {
                                    productDetailViewModel.removeProduct(it)
                                }
                                deletePosition = ProductListFragment.selectedPos
                                dialog.dismiss()
                                parentFragmentManager.popBackStack()
                            }
                            .create()

                        alertDialog.show()
                    }
                }
            }
            true
        }
        badgeDrawable = BadgeDrawable.create(requireContext())



        view.findViewById<MaterialButton>(R.id.categoryButton).setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, CategoryFragment(),"Category Opened From Product Detail")
        }

        productDetailViewModel.getProductsByCartId(MainActivity.cartId)
        productDetailViewModel.cartProducts.observe(viewLifecycleOwner){
            FindNumberOfCartItems.productCount.value = it.size
            var noOfItemsInt = FindNumberOfCartItems.productCount.value
            if(noOfItemsInt==0){
                badgeDrawable.isVisible = false
            }
            else{
                badgeDrawable.isVisible = true
                badgeDrawable.text = noOfItemsInt.toString()
            }
            BadgeUtils.attachBadgeDrawable(badgeDrawable,productDetailToolBar,R.id.cart)
        }
        ProductListFragment.selectedProductEntity.value?.brandId?.let{
            productDetailViewModel.getBrandName(it)
        }

        productDetailViewModel.brandName.observe(viewLifecycleOwner){
            view.findViewById<TextView>(R.id.brandNameProductDetail).text = it
        }


        productDetailToolBar.setNavigationOnClickListener {
            setProductValue()
            parentFragmentManager.popBackStack()
        }

        ProductListFragment.selectedProductEntity.value?.let {
            productDetailViewModel.addInRecentlyViewedItems(it.productId)
//            productDetailViewModel.getImagesForProducts(it.productId)

        }


        recyclerView = view.findViewById(R.id.productListInProductDetailFragment)

        productDetailViewModel.similarProductsLiveData.observe(viewLifecycleOwner){
            if(it.size ==1) {
                view.findViewById<LinearLayout>(R.id.similarProductsLayout).visibility = View.GONE
            }
            else {
                view.findViewById<LinearLayout>(R.id.similarProductsLayout).visibility = View.VISIBLE
                val adapter = ProductListAdapter(
                    this,
                    File(requireContext().filesDir, "AppImages"),
                    "P",
                    true,productListViewModel
                )
                recyclerView.adapter = adapter
                val tmpList = mutableListOf<Product>()
                for (i in it.toMutableList()) {
                    if (i.productId == ProductListFragment.selectedProductEntity.value?.productId) {
                        continue
                    }
                    tmpList.add(i)
                }
                adapter.setProducts(tmpList)
                recyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }
        addProductButton.setOnClickListener {
            countOfOneProduct++
            productDetailViewModel.addProductInCart(
                Cart(
                    MainActivity.cartId,
                    ProductListFragment.selectedProductEntity.value!!.productId.toInt(),
                    countOfOneProduct,
                    calculateDiscountPrice(
                        ProductListFragment.selectedProductEntity.value!!.price,
                        ProductListFragment.selectedProductEntity.value!!.offer
                    )
                )
            )
            totalItemsAddedProductDetail.text = countOfOneProduct.toString()
            addProductButton.visibility = View.GONE
            FindNumberOfCartItems.productCount.value =
                FindNumberOfCartItems.productCount.value!! + 1
            resetBadge(badgeDrawable, productDetailToolBar)
            addRemoveLayout.visibility = View.VISIBLE
        }
        productDetailViewModel.imageList.observe(viewLifecycleOwner){
            var imageList:MutableList<String> = mutableListOf()
            imageList.add(ProductListFragment.selectedProductEntity.value?.mainImage?:"")
            for(i in it){
                imageList.add(i.images)
            }
            viewPager.adapter  = ProductImageAdapter(this,imageList,imageLoader)
            TabLayoutMediator(view.findViewById(R.id.imageTabLayout),viewPager){tab,pos ->

            }.attach()
        }
        addProductImgButton.setOnClickListener {
            countOfOneProduct++
            productDetailViewModel.updateProductInCart(
                Cart(
                    MainActivity.cartId,
                    ProductListFragment.selectedProductEntity.value!!.productId.toInt(),
                    countOfOneProduct,
                    calculateDiscountPrice(
                        ProductListFragment.selectedProductEntity.value!!.price,
                        ProductListFragment.selectedProductEntity.value!!.offer
                    )
                )
            )
            totalItemsAddedProductDetail.text = countOfOneProduct.toString()
        }
        removeProductImgButton.setOnClickListener {
            if (countOfOneProduct > 1) {
                countOfOneProduct--
                totalItemsAddedProductDetail.text = countOfOneProduct.toString()
                productDetailViewModel.updateProductInCart(
                    Cart(
                        MainActivity.cartId,
                        ProductListFragment.selectedProductEntity.value!!.productId.toInt(),
                        countOfOneProduct,
                        calculateDiscountPrice(
                            ProductListFragment.selectedProductEntity.value!!.price,
                            ProductListFragment.selectedProductEntity.value!!.offer
                        )
                    )
                )
            } else if (countOfOneProduct == 1) {
                countOfOneProduct--
                productDetailViewModel.removeProductInCart(
                    Cart(
                        MainActivity.cartId,
                        ProductListFragment.selectedProductEntity.value!!.productId.toInt(),
                        countOfOneProduct,
                        calculateDiscountPrice(
                            ProductListFragment.selectedProductEntity.value!!.price,
                            ProductListFragment.selectedProductEntity.value!!.offer
                        )
                    )
                )
                FindNumberOfCartItems.productCount.value =
                    FindNumberOfCartItems.productCount.value!! - 1
                resetBadge(badgeDrawable, productDetailToolBar)
                addRemoveLayout.visibility = View.GONE
                addProductButton.visibility = View.VISIBLE
            }
        }
        FindNumberOfCartItems.productCount.observe(viewLifecycleOwner){
            if(FindNumberOfCartItems.productCount.value==0){
                badgeDrawable.isVisible = false
            }
            else{
                badgeDrawable.isVisible = true
                badgeDrawable.text = FindNumberOfCartItems.productCount.value.toString()
            }
            BadgeUtils.attachBadgeDrawable(badgeDrawable,productDetailToolBar,R.id.cart)
        }
        return view
    }

    @OptIn(ExperimentalBadgeUtils::class)
    private fun resetBadge(badgeDrawable: BadgeDrawable,productDetailToolBar:MaterialToolbar) {

        if(FindNumberOfCartItems.productCount.value==0){
            badgeDrawable.isVisible = false
        }
        else{
            badgeDrawable.isVisible = true
            badgeDrawable.text = FindNumberOfCartItems.productCount.value.toString()
        }
        BadgeUtils.attachBadgeDrawable(badgeDrawable,productDetailToolBar,R.id.cart)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(isAdded) {
                    setProductValue()
                    parentFragmentManager.popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)
    }
    override fun onResume() {
        super.onResume()
        ProductListFragment.selectedProductEntity.observe(viewLifecycleOwner) { selectedProduct ->
            if(selectedProductEntityInClass==null){
                selectedProductEntityInClass = selectedProduct
            }

            if(once==0) {
                selectedProductEntityList.add(selectedProduct)
            }
            if((oneTimeFragmentIn==0) || (backNavigated) || (MainActivity.isRetailer)) {
                println("ON PRODUCT DETAIL VALUE Observer value: one time: $oneTimeFragmentIn  back: $backNavigated ${this.hashCode()} ${ProductListFragment.selectedProductEntity.value?.productName} ${selectedProduct.productName}")
                productDetailToolBar.title = selectedProduct.productName
                view?.findViewById<TextView>(R.id.productDescriptionProductDetail)?.text =
                    selectedProduct.productDescription
                productDetailViewModel.getImagesForProducts(selectedProduct.productId)
                val productNameWithQuantity =
                    "${ProductListFragment.selectedProductEntity.value?.productName} (${ProductListFragment.selectedProductEntity.value?.productQuantity})"
                view?.findViewById<TextView>(R.id.productNameProductDetail)?.text =
                    productNameWithQuantity
                var price = ""
                if ((ProductListFragment.selectedProductEntity.value?.offer ?: -1f) > 0f) {
                    mrpTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    val discountedPriceStr = " MRP ₹${
                        calculateDiscountPrice(
                            ProductListFragment.selectedProductEntity.value!!.price,
                            ProductListFragment.selectedProductEntity.value!!.offer
                        )
                    }"
                    discountedPrice.visibility = View.VISIBLE
                    discountedPrice.text = discountedPriceStr
                } else {
                    mrpTextView.paintFlags = 0
                    discountedPrice.visibility = View.GONE
                }
                price = "MRP ₹${ProductListFragment.selectedProductEntity.value?.price}"
                ProductListFragment.selectedProductEntity.value?.brandId?.let {
                    productDetailViewModel.getBrandName(it)
                }
                mrpTextView.text = price
                val offerView = view?.findViewById<TextView>(R.id.productOffer)
                if ((ProductListFragment.selectedProductEntity.value?.offer ?: -1f) < 1f) {
                    offerView?.visibility = View.GONE
                } else {
                    offerView?.visibility = View.VISIBLE
                }
                var offerStr =
                    ProductListFragment.selectedProductEntity.value?.offer?.toInt().toString() + "% Off"
                offerView?.text = offerStr
                view?.findViewById<TextView>(R.id.expiryDateProductDetail)?.text =
                    DateGenerator.getDayAndMonth(ProductListFragment.selectedProductEntity.value?.expiryDate!!)
                view?.findViewById<TextView>(R.id.manufactureDateProductDetail)?.text =
                    DateGenerator.getDayAndMonth(ProductListFragment.selectedProductEntity.value?.manufactureDate!!)

                if (ProductListFragment.selectedProductEntity.value != null) {
                    productDetailViewModel.getCartForSpecificProduct(
                        MainActivity.cartId,
                        ProductListFragment.selectedProductEntity.value!!.productId.toInt()
                    )
                    productDetailViewModel.isCartEntityAvailable.observe(viewLifecycleOwner) {
                        if (it == null) {
                            countOfOneProduct = 0
                            addRemoveLayout.visibility = View.GONE
                            addProductButton.visibility = View.VISIBLE
                        } else {
                            addRemoveLayout.visibility = View.VISIBLE
                            addProductButton.visibility = View.GONE
                            countOfOneProduct = it.totalItems
                            totalItemsAddedProductDetail.text = countOfOneProduct.toString()
                        }
                    }
                }
                if(backNavigated){
                    backNavigated = false
                }
                else{
                    oneTimeFragmentIn = 1
                }
            }
            once = 1
            productDetailViewModel.getSimilarProduct(selectedProduct.categoryName)
        }

        InitialFragment.hideBottomNav.value = true
        InitialFragment.hideSearchBar.value = true
    }

    override fun onStop() {
        super.onStop()
        backNavigated = true
        recyclerView.stopScroll()
        InitialFragment.hideBottomNav.value = false
        InitialFragment.hideSearchBar.value = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
        productDetailViewModel.brandName.value = null
        productDetailViewModel.brandName.removeObservers(viewLifecycleOwner)
        productDetailViewModel.isCartEntityAvailable.removeObservers(viewLifecycleOwner)
    }

    override fun onPause() {
        super.onPause()
        productObserved = 0
        recyclerView.adapter?.let {
            it.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

    }
    private fun calculateDiscountPrice(price:Float, offer:Float):Float{
        if(offer>0f) {
            return price - (price * (offer / 100))
        }
        else{
            return price
        }
    }


    fun setProductValue(){
        val size = selectedProductEntityList.size
        try{
            selectedProductEntityList.removeAt(size-1)
            ProductListFragment.selectedProductEntity.value = selectedProductEntityList[size-2]
        }
        catch (e:Exception){
        }
    }
}