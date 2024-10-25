package com.example.shoppinggroceryapp.views.sharedviews.profileviews

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.AddressRepository
import com.core.data.repository.AuthenticationRepository
import com.core.data.repository.CartRepository
import com.core.data.repository.HelpRepository
import com.core.data.repository.OrderRepository
import com.core.data.repository.ProductRepository
import com.core.data.repository.SearchRepository
import com.core.data.repository.SubscriptionRepository
import com.core.data.repository.UserRepository
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
import com.example.shoppinggroceryapp.helpers.alertdialog.DataLossAlertDialog
import com.example.shoppinggroceryapp.helpers.permissionhandler.CameraPermissionHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.helpers.permissionhandler.interfaces.ImagePermissionHandler
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.helpers.toast.ShowShortToast
import com.example.shoppinggroceryapp.views.GroceryAppSharedVMFactory
import com.example.shoppinggroceryapp.views.initialview.InitialFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditProfileFragment : Fragment() {

    private lateinit var editProfileTopbar:MaterialToolbar
    private lateinit var firstName:TextInputEditText
    private lateinit var lastName:TextInputEditText
    private lateinit var email:TextInputEditText
    private lateinit var phone:TextInputEditText
    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var editProfileInputChecker: InputChecker
    private lateinit var saveDetails:MaterialButton
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var imageLoaderAndGetter: ImageLoaderAndGetter
    private lateinit var imageHandler: ImageHandler
    private var profileBitmap:Bitmap? = null
    private var fileName = ""
    private lateinit var imagePermissionHandler: ImagePermissionHandler
    var deleteImage = false
    var deleteImgFile = ""
    private var image = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileInputChecker = TextLayoutInputChecker()
        imageLoaderAndGetter = ImageLoaderAndGetter()
        imageHandler = ImageHandler(this)
        imageHandler.initActivityResults()
        imagePermissionHandler = CameraPermissionHandler(this,imageHandler)
        imagePermissionHandler.initPermissionResult()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_edit_profile, container, false)
        view.findViewById<ImageView>(R.id.editPictureImg).apply {
            val imageBitMap = imageLoaderAndGetter.getImageInApp(requireContext(),MainActivity.userImage)
            if(imageBitMap!=null){
                setImageBitmap(imageBitMap)
                view.findViewById<MaterialButton>(R.id.editPictureBtn).text = "Change Profile Picture"
                view.findViewById<MaterialButton>(R.id.deleteProfileButton).visibility = View.VISIBLE
                setPadding(0)
            }
            else{
                view.findViewById<MaterialButton>(R.id.deleteProfileButton).visibility = View.GONE
            }
        }
        view.findViewById<MaterialButton>(R.id.deleteProfileButton).setOnClickListener {
            deleteImage = true
            deleteImgFile = MainActivity.userImage
            fileName = ""
            view.findViewById<ImageView>(R.id.editPictureImg).apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.add_photo_alternate_24px))
                setPadding(30)
            }
            view.findViewById<MaterialButton>(R.id.editPictureBtn).text = "Add Profile Picture"
            view.findViewById<MaterialButton>(R.id.deleteProfileButton).visibility = View.GONE
        }
        view.findViewById<ImageView>(R.id.editPictureImg).setOnClickListener {
            imagePermissionHandler.checkPermission(false)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                backPropagate()
            }

        })
        imageHandler.gotImage.observe(viewLifecycleOwner){
            val imageTmp = System.currentTimeMillis().toString()
            fileName = imageTmp
            profileBitmap = it
            deleteImage = false
            view.findViewById<ImageView>(R.id.editPictureImg).apply {
                setImageBitmap(it)
                setPadding(0)
            }
            view.findViewById<MaterialButton>(R.id.editPictureBtn).text = "Change Profile Picture"
            view.findViewById<MaterialButton>(R.id.deleteProfileButton).visibility = View.VISIBLE
        }

        view.findViewById<MaterialButton>(R.id.editPictureBtn).setOnClickListener {
            imagePermissionHandler.checkPermission(false)
        }

        val db1 = AppDatabase.getAppDatabase(requireContext())
        val userDao = db1.getUserDao()
        val retailerDao = db1.getRetailerDao()
        editProfileViewModel = ViewModelProvider(this,
            GroceryAppSharedVMFactory(retailerDao, userDao)
        )[EditProfileViewModel::class.java]
        editProfileTopbar = view.findViewById(R.id.editProfileAppBar)
        firstName = view.findViewById(R.id.editFirstName)
        lastName = view.findViewById(R.id.editLastName)
        email = view.findViewById(R.id.editEmail)
        phone = view.findViewById(R.id.editPhoneNumber)
        fileName = MainActivity.userImage
        saveDetails = view.findViewById(R.id.saveDetailsButton)
        phoneLayout = view.findViewById(R.id.editPhoneNumberLayout)
        emailLayout = view.findViewById(R.id.editEmailLayout)
        firstNameLayout = view.findViewById(R.id.editFirstNameLayout)
        firstName.setText(MainActivity.userFirstName)
        lastName.setText(MainActivity.userLastName)
        email.setText(MainActivity.userEmail)
        phone.setText(MainActivity.userPhone)

        val pref = requireActivity().getSharedPreferences("freshCart",Context.MODE_PRIVATE)
        val editor =pref.edit()

        editProfileTopbar.setNavigationOnClickListener {
            backPropagate()
        }

        phone.filters = arrayOf(InputFilter.LengthFilter(15))
        saveDetails.setOnClickListener {
            profileBitmap?.let {
                imageLoaderAndGetter.storeImageInApp(requireContext(),it,fileName)
                MainActivity.userImage = fileName
            }
            firstNameLayout.error = editProfileInputChecker.nameCheck(firstName)
            emailLayout.error = editProfileInputChecker.lengthAndEmailCheck(email)
            phoneLayout.error = editProfileInputChecker.lengthAndEmptyCheckForPhone("Phone Number",phone,10)
            if(firstNameLayout.error==null && emailLayout.error==null && phoneLayout.error == null) {
                val oldEmail = MainActivity.userEmail
                MainActivity.userEmail = email.text.toString()
                MainActivity.userPhone = phone.text.toString()
                MainActivity.userFirstName = firstName.text.toString()
                MainActivity.userLastName = lastName.text.toString()
                editor.putString("userFirstName", MainActivity.userFirstName)
                editor.putString("userLastName", MainActivity.userLastName)
                editor.putString("userEmail", MainActivity.userEmail)
                editor.putString("userPhone", MainActivity.userPhone)
                editor.putString("userProfile", MainActivity.userImage)
                editor.apply()
                if(deleteImage){
                    imageLoaderAndGetter.deleteImageInApp(requireContext(),deleteImgFile)
                    MainActivity.userImage = ""
                }

                editProfileViewModel.saveDetails(
                    oldEmail = oldEmail,
                    firstName = firstName.text.toString(),
                    lastName = lastName.text.toString(),
                    email = email.text.toString(),
                    phone = phone.text.toString(), image = MainActivity.userImage
                )
                ShowShortToast.show("Profile Updated Successfully",requireContext())
                parentFragmentManager.popBackStack()
            }
            else{
                ShowShortToast.show("Please Provide Valid Details",requireContext())
            }
        }
        return view
    }

    private fun backPropagate() {
        if((MainActivity.userEmail==email.text.toString() && (MainActivity.userImage==fileName)
                    && MainActivity.userPhone==phone.text.toString() && MainActivity.userFirstName == firstName.text.toString()
                    && MainActivity.userLastName==lastName.text.toString())){
            parentFragmentManager.popBackStack()
        }
        else{
            DataLossAlertDialog().showDataLossAlertDialog(requireContext(),parentFragmentManager)
        }
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