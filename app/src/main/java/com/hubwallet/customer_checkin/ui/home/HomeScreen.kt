package com.hubwallet.customer_checkin.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.CustomerSelectionDialogLayoutBinding
import com.hubwallet.customer_checkin.databinding.FragmentHomeScreenBinding
import com.hubwallet.customer_checkin.databinding.LayoutMenuItemBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@AndroidEntryPoint
class HomeScreen : BaseFragment<FragmentHomeScreenBinding>(R.layout.fragment_home_screen) {

    @Inject
    lateinit var preferenceManager: PreferenceManager
    var barCodeFlag = true
    private var selectCustomerDialog: AlertDialog? = null
    override fun onInitDataBinding() {
        (activity as MainActivity).bottomPowerByIsShow(false)
        (activity as MainActivity).topPowerByIsShow(false)
        viewBinding.apply {


            val salonName = preferenceManager.getValueString(PreferenceManager.salonName)
            toolbarTop.tvSalonName.text = salonName
            toolbarTop.ivMenuDot.setVisibility(false)
            try {
                val profilePhoto =
                    preferenceManager.getValueString(PreferenceManager.profilePhoto)

                Glide.with(requireContext()).load(profilePhoto).centerCrop()
                    .placeholder(R.drawable.no_image).into(toolbarTop.ivLogo)

                val wallPhoto =
                    preferenceManager.getValueString(PreferenceManager.profilePhoto.toString())
                if (clLeftContainerImg != null) {
                    Glide.with(requireContext())
                        .load(wallPhoto)
                        .centerCrop()
                        .placeholder(R.drawable.no_image)
                        .into(clLeftContainerImg)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            ivMenuDot.setOnClickListener {
//                val popupwindow_obj = (activity as MainActivity).popupDisplay()
//                popupwindow_obj?.showAsDropDown(
//                    ivMenuDot,
//                    -40,
//                    18
//                ); // where u want show on view click event popupwindow.showAsDropDown(view, x, y);
//
//
//            }


//check if toogle is on / off from POS app
//            if (preferenceManager.getValueString(PreferenceManager.CHECK_IN_ONLINE_BOOKING).equals("1")) {
//                btnRegister.isEnabled = true
//                btnRegister.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
//            } else {
//                btnRegister.isEnabled = false
//                btnRegister.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey));
//            }

            btnRegister.setOnClickListener {
                newAppointmentDialogBox()
            }

            llExistingUser.setOnClickListener {
                findNavController().navigate(R.id.checkInFragment)
            }

        }


    }

    private fun newAppointmentDialogBox() {
        val customerSelectionBiding = DataBindingUtil.inflate<CustomerSelectionDialogLayoutBinding>(
            layoutInflater,
            R.layout.customer_selection_dialog_layout,
            null,
            false
        )
        selectCustomerDialog =
            MaterialAlertDialogBuilder(requireContext()).setView(customerSelectionBiding.root)
                .setCancelable(false).show()

        customerSelectionBiding.apply {
            existingCustomerButton.setOnClickListener {
                Log.e("newAppo55555555gBox: ", "322222222222")
                findNavController().navigate(R.id.existingCustomerCheckInFragment)
                selectCustomerDialog?.dismiss()
            }
            newCustomerButton.setOnClickListener {
                Log.e("newAppo511111=15gBox: ", "322222222222")
                findNavController().navigate(R.id.customerRegistration)
                selectCustomerDialog?.dismiss()
            }
            ivClose.setOnClickListener {
                selectCustomerDialog?.dismiss()
            }

        }
        selectCustomerDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        selectCustomerDialog?.show()

    }

    private fun menuDialogBox() {
        val binding = DataBindingUtil.inflate<LayoutMenuItemBinding>(
            layoutInflater,
            R.layout.layout_menu_item,
            null,
            false
        )

        val dialog =
            MaterialAlertDialogBuilder(requireContext()).setView(binding.root).setCancelable(false)
                .show()

        dialog.show()
    }


    fun getBitmapFromURL(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


}