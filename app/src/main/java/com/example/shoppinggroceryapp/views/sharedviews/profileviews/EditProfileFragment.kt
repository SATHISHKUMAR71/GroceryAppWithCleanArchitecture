package com.example.shoppinggroceryapp.views.sharedviews.profileviews

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.core.data.repository.CustomerRepository
import com.core.data.repository.RetailerRepository
import com.core.data.repository.UserRepository
import com.example.shoppinggroceryapp.MainActivity
import com.example.shoppinggroceryapp.R
import com.example.shoppinggroceryapp.framework.data.CustomerDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.RetailerDataSourceImpl
import com.example.shoppinggroceryapp.framework.data.UserDataSourceImpl
import com.example.shoppinggroceryapp.framework.db.database.AppDatabase
import com.example.shoppinggroceryapp.helpers.permissionhandler.CameraPermissionHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageHandler
import com.example.shoppinggroceryapp.helpers.imagehandlers.ImageLoaderAndGetter
import com.example.shoppinggroceryapp.helpers.permissionhandler.interfaces.ImagePermissionHandler
import com.example.shoppinggroceryapp.helpers.inputvalidators.interfaces.InputChecker
import com.example.shoppinggroceryapp.helpers.inputvalidators.TextLayoutInputChecker
import com.example.shoppinggroceryapp.views.GroceryAppViewModelFactory
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
    private lateinit var db: AppDatabase
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var imageLoaderAndGetter: ImageLoaderAndGetter
    private lateinit var imageHandler: ImageHandler
    private lateinit var imagePermissionHandler: ImagePermissionHandler
    var deleteImage = false
    var deleteImgFile = ""
    private var image = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileInputChecker = TextLayoutInputChecker()
        db = AppDatabase.getAppDatabase(requireContext())
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
            view.findViewById<ImageView>(R.id.editPictureImg).apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.add_photo_alternate_24px))
                setPadding(30)
            }
            view.findViewById<MaterialButton>(R.id.editPictureBtn).text = "Add Profile Picture"
            view.findViewById<MaterialButton>(R.id.deleteProfileButton).visibility = View.GONE
            Toast.makeText(context,"Profile Picture Deleted Successfully",Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.editPictureImg).setOnClickListener {
            imagePermissionHandler.checkPermission(false)
        }


        imageHandler.gotImage.observe(viewLifecycleOwner){
            val imageTmp = System.currentTimeMillis().toString()
            imageLoaderAndGetter.storeImageInApp(requireContext(),it,imageTmp)
            view.findViewById<ImageView>(R.id.editPictureImg).apply {
                setImageBitmap(it)
                setPadding(0)
            }
            MainActivity.userImage = imageTmp
            view.findViewById<MaterialButton>(R.id.editPictureBtn).text = "Change Profile Picture"
            view.findViewById<MaterialButton>(R.id.deleteProfileButton).visibility = View.VISIBLE
        }

        view.findViewById<MaterialButton>(R.id.editPictureBtn).setOnClickListener {
            imagePermissionHandler.checkPermission(false)
//            imageHandler.showAlertDialog()
        }
        val db1 = AppDatabase.getAppDatabase(requireContext())
        val retailerRepository = RetailerRepository(RetailerDataSourceImpl(db1.getRetailerDao()))
        val customerRepository = CustomerRepository(CustomerDataSourceImpl(db1.getUserDao()))
        val userRepository = UserRepository(UserDataSourceImpl(db1.getUserDao(),db1.getRetailerDao()))
        editProfileViewModel = ViewModelProvider(this,
            GroceryAppViewModelFactory(userRepository, retailerRepository, customerRepository)
        )[EditProfileViewModel::class.java]
        editProfileTopbar = view.findViewById(R.id.editProfileAppBar)
        firstName = view.findViewById(R.id.editFirstName)
        lastName = view.findViewById(R.id.editLastName)
        email = view.findViewById(R.id.editEmail)
        phone = view.findViewById(R.id.editPhoneNumber)
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
            parentFragmentManager.popBackStack()
        }

        phone.filters = arrayOf(InputFilter.LengthFilter(15))
        saveDetails.setOnClickListener {
            firstNameLayout.error = editProfileInputChecker.nameCheck(firstName)
            emailLayout.error = editProfileInputChecker.lengthAndEmailCheck(email)
            phoneLayout.error = editProfileInputChecker.lengthAndEmptyCheck("Phone Number",phone,10)
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
                Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(context, "Please Provide Valid Details", Toast.LENGTH_SHORT).show()
            }
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
        InitialFragment.hideSearchBar.value = false
        InitialFragment.hideBottomNav.value = false
    }

    fun addFocusChangeListeners(){

    }

}