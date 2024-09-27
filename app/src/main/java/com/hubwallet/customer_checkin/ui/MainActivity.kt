package com.hubwallet.customer_checkin.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.dialogUtils.DialogUtils
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.ActivityMainBinding
import com.hubwallet.customer_checkin.ui.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException
import javax.inject.Inject
import com.hubwallet.customer_checkin.BuildConfig
import com.hubwallet.customer_checkin.common.DeviceInfo
import com.hubwallet.customer_checkin.common.extensions.setVisibility


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mWebSocketClient: WebSocketClient
    private lateinit var viewBinding: ActivityMainBinding

    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (resources.getBoolean(R.bool.isTablet)) {
            isTableModeOn = true
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
            Log.e("onCreateISTAB", "The app is running on a tablet")
            // The app is running on a tablet
            // Add your tablet-specific code here
        } else {
            isTableModeOn = false
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            Log.e("onCreateISTAB", "The app is running on a mobile device")

            // The app is running on a mobile device
            // Add your mobile-specific code here
        }

        viewBinding.apply {
//            ivMenuDot.setOnClickListener {
//                val popupwindow_obj = popupDisplay()
//                popupwindow_obj?.showAsDropDown(
//                    ivMenuDot,
//                    -40,
//                    18
//                ); // where u want show on view click event popupwindow.showAsDropDown(view, x, y);
//            }


           toolbarTop.ivMenuDot.setOnClickListener {
                findNavController(viewBinding.navHostContainer.id).popBackStack()
            }


//            ivMenuDot.setOnClickListener {
//                val popup = PopupMenu(this@MainActivity, it)
//                val inflater: MenuInflater = popup.menuInflater
//                inflater.inflate(R.menu.hubwallet_menu, popup.menu)
//                try {
//                    val method: Method = popup.menu.javaClass.getDeclaredMethod(
//                        "setOptionalIconsVisible",
//                        Boolean::class.javaPrimitiveType
//                    )
//                    method.setAccessible(true)
//                    method.invoke(popup.menu, true)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                    when (item.itemId) {
//                        R.id.changeTheme -> {
////                            changeTheme(item)
//
//                        }
//
//                        R.id.signout -> {
//                            findNavController(R.id.nav_host_container).popBackStack()
//                            Toast.makeText(
//                                this@MainActivity,
//                                "You Clicked : " + item.title,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                    true
//                })
//                popup.show()
//
//            }
        }
        connectWebSocket()
    }
    private fun connectWebSocket() {


        val uri: URI
        val url = BuildConfig.SOCKET_URL
        Log.e("connectWebSocket: ", url)
        try {
            uri = URI(url)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        mWebSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                Log.e("Websocket", "Opened")

            }

            override fun onMessage(s: String) {
                try {
                    Log.e("onMessage: ", s)
                    val json = JSONObject(s)
                    Log.e("onMessage:APPTYPE ",json.getString("app_type").toString() )
//                    Log.e("onMes:DEVICEID_backend",json.getString("device_id").toString() )
//                    Log.e("onMesDEVICEID_itself ",DeviceInfo().getDeviceSerial(this@MainActivity).toString() )
//                    Log.e("onMessage:RELOAD ",json.getString("reload").toString() )

                 if (json.getString("app_type") == "checkin_app"
                     && json.getString("device_id").equals(DeviceInfo().getDeviceSerial(this@MainActivity)) && json.getString("reload").equals("yes")
                     ){
                     when (json.getString("module_name")) {
                         "get_card" -> {
                             val jsonData = json.getJSONObject("data")
                             preferenceManager.putValueString(PreferenceManager.is_card_booking_available,jsonData.getString("cc_online_ch_app"))
                             preferenceManager.putValueString(PreferenceManager.CHECK_IN_ONLINE_BOOKING,jsonData.getString("checkin_app_allow"))
                             Log.e(TAG, "onMes_cc_online_JsonObject --- " +    jsonData)
                             Log.e(TAG, "onMes_cc_online_cu_app --- " +    jsonData.getString("cc_online_ch_app"))
                         }
                     }
                    findNavController(R.id.nav_host_container).navigate(R.id.homeScreen)
                 }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.e("Websocket", "Closed $s")

            }

            override fun onError(e: Exception) {
                Log.e("Websocket", "Error " + e.message)

            }
        }
        mWebSocketClient.connect()
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!

                try {


                } catch (e: Exception) {
                    e.printStackTrace()
                }

                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show()
                // Use the uri to load the image
            }
        }

    fun backButtonVisiable(flag: Boolean){
        viewBinding.toolbarTop.ivMenuDot.setVisibility(flag)
    }

    fun bottomPowerByIsShow(flag: Boolean) {
        if (flag) {
            viewBinding.llPowerByContainer.llPowerByContainer.visibility = View.VISIBLE
        } else {
            viewBinding.llPowerByContainer.llPowerByContainer.visibility = View.GONE
        }
    }

    fun topPowerByIsShow(flag: Boolean) {
        if (flag) {
            viewBinding.toolbarTop.commonToolbarTop.visibility = View.VISIBLE
        } else {
            viewBinding.toolbarTop.commonToolbarTop.visibility = View.GONE
        }
    }

    fun backButtonShow(flag: Boolean) {
        if (flag) {
            viewBinding.toolbarTop.ivMenuDot.visibility = View.VISIBLE
        } else {
            viewBinding.toolbarTop.ivMenuDot.visibility = View.GONE
        }
    }

    private fun changeTheme(menuItem: View) {
        ImagePicker.with(this@MainActivity)
            //...
            .provider(ImageProvider.BOTH) //Or bothCameraGallery()
            .createIntentFromDialog { launcher.launch(it) }
        Toast.makeText(
            this@MainActivity,
            "You Clicked : " + menuItem,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.hubwallet_menu, popup.menu)
        popup.show()
    }

    fun popupDisplay(): PopupWindow {
        val popupWindow: PopupWindow = PopupWindow(this)

        // inflate your layout or dynamically add view
        val inflater =
            baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.custom_menu_bar, null)
        val changeThemeButton = view.findViewById<TextView>(R.id.tvChangeTheme)
        val logoutButton = view.findViewById<TextView>(R.id.tvLogout)
        changeThemeButton.setOnClickListener {
            changeTheme(it)
        }
        logoutButton.setOnClickListener {
            findNavController(R.id.nav_host_container).popBackStack()
            Toast.makeText(
                this@MainActivity,
                "You Clicked : " + "dfsdfdsf",
                Toast.LENGTH_SHORT
            ).show()
        }
        popupWindow.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = view
        return popupWindow
    }

//    override fun onBackPressed() {
//        val navHostFragment: NavHostFragment? =
//            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
//        navHostFragment!!.childFragmentManager.fragments.forEach {
//            it.childFragmentManager.popBackStack()
//        }
//    }


    override fun onDestroy() {
        preferenceManager.clean()
        super.onDestroy()
    }

    companion object {
        //        var vendorId = ""
        var customerId = ""
        var isTableModeOn = false
    }

    fun setData() {
//        vendorId = preferenceManager.getValueString(PreferenceManager.vendorKey)
        val salonName = preferenceManager.getValueString(PreferenceManager.salonName)
        Log.e("onCreateSalonName ", salonName.toString())
        viewBinding.apply {
            try {

                val profilePhoto =
                    preferenceManager.getValueString(PreferenceManager.profilePhoto)
                Glide.with(this@MainActivity)
                    .load(profilePhoto)
                    .centerCrop()
                    .placeholder(R.drawable.no_image)
                    .into(toolbarTop.ivLogo)

            } catch (e: Exception) {
                e.printStackTrace()
            }

           toolbarTop.tvSalonName.text = salonName
            Log.e("onCreate:222222", salonName.toString())
        }
    }

    fun checkIfCheckoutContainerActive(isChe: Boolean = false) {
        val navController = findNavController(R.id.nav_host_container)
        navController.popBackStack(navController.graph.startDestinationId, true)

//        for (frag in supportFragmentManager.fragments) {
//            if (frag is CheckoutFragment) {
////                findNavController(R.id.nav_host_container).clearBackStack(R.id.checkoutFragment)
////                supportFragmentManager.beginTransaction().remove(frag).commit()
//            }
//            if (CheckoutFragment.isOpen) {
//                CheckoutFragment.appointmentDialog.dismiss()
//            }

//        }

    }

    override fun onBackPressed() {

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navHostFragment.childFragmentManager.fragments.forEach {
            if (it is HomeScreen) {
                DialogUtils.openPopup( findNavController(R.id.nav_host_container),this, layoutInflater)
            } else {
                super.onBackPressed()
            }
        }
    }

}

