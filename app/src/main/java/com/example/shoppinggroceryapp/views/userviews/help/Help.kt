package com.example.shoppinggroceryapp.views.userviews.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.core.domain.help.CustomerRequest
import com.core.domain.order.OrderDetails
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.helpers.dategenerator.DateGenerator
import com.example.shoppinggroceryapp.helpers.fragmenttransaction.FragmentTransaction
import com.example.shoppinggroceryapp.views.GroceryAppUserVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.example.shoppinggroceryapp.views.sharedviews.orderviews.orderlist.OrderListFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class Help : Fragment() {


    private lateinit var helpViewModel: HelpViewModel
    companion object{
        var selectedOrder: OrderDetails? = null
        var backPressed = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_help, container, false)
        view.findViewById<MaterialToolbar>(R.id.helpReqTool).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        val requestTextView = view.findViewById<TextView>(R.id.customerRequestHelpFrag)
        val orderLayout = view.findViewById<LinearLayout>(R.id.orderViewLayout)
        setUpViewModel()
        if(selectedOrder == null){
            val orderListFragment = OrderListFragment()
            orderListFragment.arguments = Bundle().apply {
                putBoolean("isClickable",true)
            }
            FragmentTransaction.navigateWithBackstack(parentFragmentManager,orderListFragment,"Order List Fragment")
        }
        else{
            val selectOrderFrag = parentFragmentManager.findFragmentByTag("SelectOrder")
            selectOrderFrag?.let{
                parentFragmentManager.beginTransaction()
                    .remove(selectOrderFrag)
                    .commit()
            }
            val selectedOrderView = LayoutInflater.from(context).inflate(R.layout.order_layout,
                container,false)
            setDeliveryStatus(selectedOrderView)
            helpViewModel.assignProductList(selectedOrder!!.cartId)
            helpViewModel.productList.observe(viewLifecycleOwner){
                selectedOrderView.findViewById<TextView>(R.id.orderedProductsList).text = it
            }

            selectedOrderView.findViewById<ImageView>(R.id.imageView).visibility = View.GONE
            orderLayout.addView(selectedOrderView)
            setOnClickListeners(view,requestTextView)
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)

        return view
    }

    private fun setDeliveryStatus(selectedOrderView: View) {
        if(selectedOrder!!.deliveryStatus=="Pending"){
            val screen = "Expected On: ${DateGenerator.getDayAndMonth(selectedOrder!!.deliveryDate)}"
            selectedOrderView.findViewById<TextView>(R.id.deliveryDate).text = screen
        }
        else{
            val screen = "Delivered On: ${DateGenerator.getDayAndMonth(selectedOrder!!.deliveryDate)}"
            selectedOrderView.findViewById<TextView>(R.id.deliveryDate).text = screen
        }
        if(selectedOrder?.deliveryFrequency!="Once"){
            when(selectedOrder?.deliveryFrequency){
                "Weekly Once" -> {
                    selectedOrderView.findViewById<TextView>(R.id.deliveryDate).text =
                        "Orders will be delivered on a weekly basis"
                }
                "Monthly Once" -> {
                    selectedOrderView.findViewById<TextView>(R.id.deliveryDate).text =
                        "Orders will be delivered on a monthly basis"
                }
                "Daily" -> {
                    selectedOrderView.findViewById<TextView>(R.id.deliveryDate).text =
                        "Orders will be delivered on a daily basis"
                }
            }
        }
        val orderDate = "Ordered On: ${DateGenerator.getDayAndMonth(selectedOrder!!.orderedDate)}"
        selectedOrderView.findViewById<TextView>(R.id.orderedDate).text = orderDate
    }

    private fun setOnClickListeners(view: View,req:TextView) {
        view.findViewById<MaterialButton>(R.id.sendReqBtn).setOnClickListener {
            if(req.text.toString().isNotEmpty()){
                ShowShortToast.show("Request Sent Successfully",requireContext())
                val orderId = selectedOrder!!.orderId
                helpViewModel.sendReq(
                    CustomerRequest(0,MainActivity.userId.toInt(),
                        DateGenerator.getCurrentDate(),
                        orderId,req.text.toString())
                )
                parentFragmentManager.popBackStack()
            }
            else{
                ShowShortToast.show("Please Write the Request",requireContext())
            }
        }
    }

    private fun setUpViewModel() {
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        helpViewModel = ViewModelProvider(this,
            GroceryAppUserVMFactory(userDao, retailerDao)
        )[HelpViewModel::class.java]
    }

    override fun onPause() {
        super.onPause()
        backPressed = true
        selectedOrder = null
    }
    override fun onResume() {
        super.onResume()
        InitialFragment.hideSearchBar.value = true
        InitialFragment.hideBottomNav.value = true
    }
    override fun onStop() {
        super.onStop()
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }

}