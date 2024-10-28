package com.example.shoppinggroceryapp.views.sharedviews.profileviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.userviews.cartview.FindNumberOfCartItems
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import java.io.File


class WishListFragment : Fragment() {

    private lateinit var adapter: ProductListAdapter

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wish_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.wishedList)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        val viewModel = ViewModelProvider(
            this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[ProductListViewModel::class.java]
        if(recyclerView.adapter==null) {
            adapter = ProductListAdapter(
                this, File(requireContext().filesDir, "AppImages"), "P", false,viewModel,null)
            recyclerView.adapter =adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        val toolbar = view.findViewById<MaterialToolbar>(R.id.wishListListToolBar)
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
                        CartFragment(),"Cart Fragment")
                }
            }
            true
        }
        val badgeDrawableListFragment = BadgeDrawable.create(requireContext())
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
        viewModel.getWishedProducts(MainActivity.userId.toInt())
        viewModel.productEntityList.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                recyclerView.visibility = View.GONE
                view.findViewById<TextView>(R.id.noItemsAvailableText).visibility = View.VISIBLE
            }
            else{
                recyclerView.visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.noItemsAvailableText).visibility = View.GONE
                adapter.setProducts(it)
            }
        }
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        InitialFragment.hideSearchBar.value = true
        InitialFragment.hideBottomNav.value = true
    }

    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value =false
        InitialFragment.hideBottomNav.value = false
    }
}