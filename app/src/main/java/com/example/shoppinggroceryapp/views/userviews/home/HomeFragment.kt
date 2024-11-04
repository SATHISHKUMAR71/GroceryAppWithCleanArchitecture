package com.example.shoppinggroceryapp.views.userviews.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.example.shoppinggroceryapp.framework.db.entity.products.ProductEntity
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterPrice
import com.example.shoppinggroceryapp.views.sharedviews.filter.ResetFilterValues
import com.example.shoppinggroceryapp.views.sharedviews.sort.BottomSheetDialogFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.userviews.category.CategoryFragment
import com.example.shoppinggroceryapp.views.userviews.offer.OfferFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class HomeFragment : Fragment() {

    var essentialItems = listOf("Butter & Ghee","Fresh Fruits","Fresh Vegetables","Milk & Cream","Mixed Nuts","Rice","Spices","Soft Drinks"
        ,"Energy Drinks","Tea & Coffee","Wheat & Flour")

    var imagesList = listOf(R.drawable.butter,R.drawable.fresh_fruits,R.drawable.fresh_veg,
        R.drawable.milk_cream,R.drawable.mixed_nuts,R.drawable.rice,R.drawable.spices,R.drawable.soft_drinks,
        R.drawable.energy_drinks,R.drawable.tea_coffee,R.drawable.wheat_flour)

    var essentialSize = essentialItems.size -1
    private lateinit var homeFragNestedScroll: ScrollView
    private lateinit var recentItems:RecyclerView
    var recentlyViewedList = mutableListOf<ProductEntity>()
    private lateinit var homeViewModel : HomeViewModel
    var lastSelectedPosition =0
    companion object{
        var position = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BottomSheetDialogFragment.selectedOption.value = null
        if(savedInstanceState==null){
            super.onCreate(savedInstanceState)
        }
        else{
            onViewStateRestored(savedInstanceState)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        OfferFragment.offerFilterCount = 0
        FilterFragment.badgeNumber = 0
        ResetFilterValues.resetFilterValues()
        OfferFragment.dis10Val = false
        OfferFragment.dis20Val = false
        OfferFragment.dis30Val = false
        OfferFragment.dis40Val = false
        OfferFragment.dis50Val =false
        ProductListFragment.dis10Val = false
        ProductListFragment.dis20Val = false
        ProductListFragment.dis30Val = false
        ProductListFragment.dis40Val = false
        ProductListFragment.dis50Val = false
        FilterFragment.list = null
        val view =inflater.inflate(R.layout.fragment_home, container, false)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        homeViewModel = ViewModelProvider(this,
            GroceryAppUserVMFactory(userDao, retailerDao)
        )[HomeViewModel::class.java]

        recentItems = view.findViewById(R.id.recentlyViewedItemsHomeFrag)
        homeFragNestedScroll =  view.findViewById(R.id.nestedScrollViewHomeFrag)

        val adapter = ProductListAdapter(this,File(requireContext().filesDir,"AppImages"),"P",true,productListViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java],null)

        homeViewModel.getRecentlyViewedItems()

//        adapter.setProducts(mutableListOf())
        if(recentItems.adapter==null) {
            recentItems.adapter = adapter
            recentItems.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        homeViewModel.recentlyViewedList.observe(viewLifecycleOwner){
            if((it!=null) &&( it.isNotEmpty())){
                view.findViewById<TextView>(R.id.recentlyViewedItemsText).visibility = View.VISIBLE
                adapter.setProducts(it)
            }
            else{
//                adapter.setProducts(listOf())
                view.findViewById<TextView>(R.id.recentlyViewedItemsText).visibility = View.GONE
            }
        }

        view.findViewById<MaterialButton>(R.id.viewAllCategoriesBtn).setOnClickListener {
//            parentFragmentManager.popBackStack("Category Fragment",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, CategoryFragment(),"Category Fragment")
        }
        val categoryContainer = view.findViewById<LinearLayout>(R.id.categoryLayoutRow)

        var i=0
        while (i<essentialSize){
            addViewToLayout(categoryContainer,i)
            i+=3
        }


        return view
    }

    private fun addViewToLayout(container: ViewGroup, index:Int){
        val newView = LayoutInflater.from(requireContext()).inflate(R.layout.category_layout,container,false)
        val card0 = newView.findViewById<CardView>(R.id.card1)
        val card1 = newView.findViewById<CardView>(R.id.card2)
        val card2 = newView.findViewById<CardView>(R.id.card3)
        val imageView0 = newView.findViewById<ImageView>(R.id.categoryImage0)
        val imageView1 = newView.findViewById<ImageView>(R.id.categoryImage1)
        val imageView2 = newView.findViewById<ImageView>(R.id.categoryImage2)
        val categoryType0 = newView.findViewById<TextView>(R.id.categoryType0)
        val categoryType1 = newView.findViewById<TextView>(R.id.categoryType1)
        val categoryType2 = newView.findViewById<TextView>(R.id.categoryType2)

        if ((index+2) <= essentialSize) {
            categoryType0.text = essentialItems[index]
            categoryType1.text = essentialItems[index+1]
            categoryType2.text = essentialItems[index+2]
            imageView0.setImageDrawable(ContextCompat.getDrawable(requireContext(),imagesList[index]))
            imageView1.setImageDrawable(ContextCompat.getDrawable(requireContext(),imagesList[index+1]))
            imageView2.setImageDrawable(ContextCompat.getDrawable(requireContext(),imagesList[index+2]))
            setImageAndTextListener(imageView0,categoryType0,card0)
            setImageAndTextListener(imageView1,categoryType1,card1)
            setImageAndTextListener(imageView2,categoryType2,card2)
        }
        else if ((index+1) <= essentialSize) {
            categoryType0.text = essentialItems[index]
            categoryType1.text = essentialItems[index+1]
            imageView0.setImageDrawable(ContextCompat.getDrawable(requireContext(),imagesList[index]))
            imageView1.setImageDrawable(ContextCompat.getDrawable(requireContext(),imagesList[index+1]))
            setImageAndTextListener(imageView0,categoryType0,card0)
            setImageAndTextListener(imageView1,categoryType1,card1)
            card2.visibility = View.INVISIBLE
            imageView2.visibility = View.INVISIBLE
            categoryType2.visibility = View.INVISIBLE
        }
        else{
            categoryType0.text = essentialItems[index]
            imageView0.setImageDrawable(ContextCompat.getDrawable(requireContext(),imagesList[index]))
            imageView2.visibility = View.INVISIBLE
            imageView1.visibility = View.INVISIBLE
            setImageAndTextListener(imageView0,categoryType0,card0)
            categoryType1.visibility = View.INVISIBLE
            categoryType2.visibility = View.INVISIBLE
            card2.visibility = View.INVISIBLE
            card1.visibility = View.INVISIBLE
        }
        container.addView(newView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setImageAndTextListener(image:ImageView, text:TextView, cardView: CardView){
        val productListFrag = ProductListFragment()
        productListFrag.arguments = Bundle().apply {
            putString("category",text.text.toString())
        }
//        image.setOnClickListener {
//            FragmentTransaction.navigateWithBackstack(parentFragmentManager,productListFrag,
//                "Product List Opened")
        text.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,productListFrag
            ,text.text.toString())
        }
        text.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scaleView(cardView, 0.9f)
                    scaleView(text,0.9f)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    scaleView(cardView, 1f)
                    scaleView(text,1f)
                    v.performClick()
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    scaleView(cardView, 1f)
                    scaleView(text,1f)
                    true
                }

                else -> false
            }
        }
        cardView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    scaleView(cardView, 0.9f)
                    scaleView(text,0.9f)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    scaleView(cardView, 1f)
                    scaleView(text,1f)
                    v.performClick()
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    scaleView(cardView, 1f)
                    scaleView(text,1f)
                    true
                }

                else -> false
            }
        }
        cardView.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,productListFrag
                ,text.text.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        lastSelectedPosition = homeFragNestedScroll.scrollY

    }


    override fun onStop() {
        super.onStop()
        recentItems.stopScroll()
        homeViewModel.recentlyViewedList.removeObservers(viewLifecycleOwner)
    }
    private fun scaleView(view: View, scale: Float) {
        view.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(100)
            .start()
    }


}