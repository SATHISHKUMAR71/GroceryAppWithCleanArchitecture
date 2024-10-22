package com.example.shoppinggroceryapp.views.sharedviews.profileviews

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.permissionhandler.CameraPermissionHandler
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.helpers.permissionhandler.interfaces.ImagePermissionHandler
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.userviews.cartview.cart.CartFragment
import com.example.shoppinggroceryapp.views.userviews.offer.OfferFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.FilterFragment
import com.example.shoppinggroceryapp.views.sharedviews.sort.BottomSheetDialogFragment
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.retailerviews.customerrequestlist.CustomerRequestListFragment
import com.example.shoppinggroceryapp.views.sharedviews.filter.ResetFilterValues
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.OrderHistoryFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.adapter.ProductListAdapter
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListFragment
import com.example.shoppinggroceryapp.views.sharedviews.productviews.productlist.ProductListViewModel
import com.example.shoppinggroceryapp.views.userviews.addressview.savedaddress.SavedAddressList
import com.google.android.material.button.MaterialButton
import java.io.File

class AccountFragment : Fragment() {

    private lateinit var editProfile:MaterialButton
    private lateinit var orderHistory:MaterialButton
    private lateinit var help:MaterialButton
    private lateinit var savedAddress:MaterialButton
    private lateinit var logoutUser:MaterialButton
    private lateinit var userName:TextView
    private lateinit var imagePermissionHandler: ImagePermissionHandler
    private lateinit var imageHandler: ImageHandler
    private lateinit var imageLoader: ImageLoaderAndGetter
    private lateinit var recentlyPurchasedItems:RecyclerView
    private lateinit var editUser:EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BottomSheetDialogFragment.selectedOption.value = null
        imageHandler = ImageHandler(this)
        imageHandler.initActivityResults()
        imagePermissionHandler = CameraPermissionHandler(this,imageHandler)
        imagePermissionHandler.initPermissionResult()
        imageLoader = ImageLoaderAndGetter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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
        val view =  inflater.inflate(R.layout.fragment_account, container, false)
        val recent = view.findViewById<LinearLayout>(R.id.recentlyPurchasedItems)
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val customerReqHistory = view.findViewById<MaterialButton>(R.id.customerReqHistory)

        customerReqHistory.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,CustomerRequestListFragment(),"customer request fragment")
        }
        val retailerDao = db1.getRetailerDao()
        editUser = ViewModelProvider(this,
            GroceryAppSharedVMFactory(
            retailerDao, userDao)
        )[EditProfileViewModel::class.java]
        val name = MainActivity.userFirstName + " "+ MainActivity.userLastName
        val profileView = view.findViewById<ImageView>(R.id.accountImage)
        val image = imageLoader.getImageInApp(requireContext(),MainActivity.userImage)
        if(MainActivity.isRetailer){
            recent.visibility =View.GONE
        }
        else{
            recent.visibility =View.VISIBLE
        }
        if(image!=null){
            profileView.setImageBitmap(imageLoader.getImageInApp(requireContext(),MainActivity.userImage))
            profileView.setPadding(0)
        }

        view.findViewById<MaterialButton>(R.id.wishlist).setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, WishListFragment(),"Wish List Fragment")
        }
        recentlyPurchasedItems = view.findViewById(R.id.recentlyPurchasedItemsList)
        editUser.getPurchasedProducts(MainActivity.userId.toInt())
        val adapter = ProductListAdapter(this,
            File(requireContext().filesDir,"AppImages"),"P",true,productListViewModel = ViewModelProvider(this,
                GroceryAppSharedVMFactory(retailerDao, userDao)
            )[ProductListViewModel::class.java],null)
        editUser.recentlyBoughtList.observe(viewLifecycleOwner){
            if((it!=null)&&(it.isNotEmpty())){
                if(recentlyPurchasedItems.adapter == null) {
                    recentlyPurchasedItems.adapter = adapter
                    recentlyPurchasedItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    adapter.setProducts(it)
                }
            }
            else{
                recent.visibility =View.GONE
                view.findViewById<TextView>(R.id.recentlyPurchasedText).visibility = View.GONE
            }
        }
        imageHandler.gotImage.observe(viewLifecycleOwner){
            if(it!=null){
                val userImageUri = System.currentTimeMillis().toString()
                var imageDate = imageLoader.storeImageInApp(requireContext(),it,userImageUri)
                editUser.saveUserImage(MainActivity.userEmail,userImageUri)
                with(requireActivity().getSharedPreferences("freshCart",Context.MODE_PRIVATE).edit()){
                    putString("userProfile",userImageUri)
                    apply()
                }
                MainActivity.userImage = userImageUri
                profileView.setImageBitmap(imageLoader.getImageInApp(requireContext(),MainActivity.userImage))
                profileView.setPadding(0)
            }
        }

        userName = view.findViewById(R.id.userName)
        userName.text =name
        editProfile = view.findViewById(R.id.editProfile)
        orderHistory = view.findViewById(R.id.orderHistory)
        help = view.findViewById(R.id.help)
        savedAddress = view.findViewById(R.id.savedAddress)
        logoutUser = view.findViewById(R.id.logout)
        editProfile.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, EditProfileFragment(),"Edit Profile")
        }
        orderHistory.setOnClickListener {
            var orderHistoryFragment = OrderHistoryFragment()
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,orderHistoryFragment,"Order List Fragment")
        }
        help.setOnClickListener {
            val orderListFragment = OrderHistoryFragment()
            orderListFragment.arguments = Bundle().apply {
                putBoolean("isClickable",true)
            }
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,orderListFragment,"Help")
        }
        savedAddress.setOnClickListener {
            FragmentTransaction.navigateWithBackstack(parentFragmentManager, SavedAddressList(),"Saved Address")
        }

        logoutUser.setOnClickListener {
            showAlertDialog()
        }
        if(MainActivity.isRetailer){
            view.findViewById<LinearLayout>(R.id.linearLayout3).visibility = View.GONE
            help.visibility =View.GONE
            savedAddress.visibility =View.GONE
        }
        return view
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout Confirmation")
            .setMessage("Are you sure to Logout?")
            .setPositiveButton("Yes"){_,_ ->
                restartApp()
            }
            .setNegativeButton("No"){dialog,_ ->
                dialog.dismiss()
            }
            .show()
    }



    fun restartApp() {
        val intent = Intent(context,MainActivity::class.java)
        CartFragment.selectedAddressEntity = null
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val sharedPreferences = requireActivity().getSharedPreferences("freshCart",Context.MODE_PRIVATE)
        editUser.resetDetails(sharedPreferences)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        InitialFragment.hideSearchBar.value = true
    }


    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value = false
    }


    override fun onDestroyView() {
        imageHandler.gotImage.value = null
        super.onDestroyView()
    }
}