package com.hubwallet.customer_checkin.ui.checkinscreen


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.github.alexzhirkevich.customqrgenerator.style.Color
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.DeviceInfo
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.base.Keys
import com.hubwallet.customer_checkin.common.base.LogoutPop
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.CheckinDialogBoxLayoutBinding
import com.hubwallet.customer_checkin.databinding.FragmentCheckInBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CheckInFragment : BaseFragment<FragmentCheckInBinding>(R.layout.fragment_check_in) {
    private val viewModel: CheckInFragmentViewModel by viewModels()
    private var checkinDialog: AlertDialog? = null

    @Inject
    lateinit var preferenceManager: PreferenceManager
    private var isTimerFinished = false
    val options = createQrVectorOptions {

        padding = .325f
//        background {
//            drawable = DrawableSource
//                .Resource(R.drawable.frame)
//        }

        //show logo in center in barcode
//        logo {
//            drawable = DrawableSource
//                .Resource(R.drawable.hubwallet_logo)
//            size = .25f
//            padding = QrVectorLogoPadding.Natural(.2f)
//            shape = QrVectorLogoShape
//                .Circle
//        }
        colors {
            dark = QrVectorColor
                .Solid(Color(0xff345288))
        }
        shapes {
            darkPixel = QrVectorPixelShape
                .RoundCorners(.5f)
            ball = QrVectorBallShape
                .RoundCorners(.25f)
            frame = QrVectorFrameShape
                .RoundCorners(.25f)
        }
    }

    override fun onInitDataBinding() {
        if (isTimerFinished) {
            findNavController().navigate(R.id.homeScreen)
            return
        }
        (activity as MainActivity).backButtonVisiable(true)


        viewBinding.viewModel = viewModel
        (activity as MainActivity).bottomPowerByIsShow(true)
        (activity as MainActivity).topPowerByIsShow(true)
        (activity as MainActivity).backButtonShow(true)
        Log.e( "onInitDataDEVICE_ID", DeviceInfo().getDeviceSerial(requireContext()).toString())
//        Toast.makeText(requireContext(),preferenceManager.getValueString(PreferenceManager.vendorKey).toString() , Toast.LENGTH_SHORT).show()

//        var deviceID = DeviceInfo().getDeviceSerial(requireContext())
        val deviceID  = preferenceManager.getValueString(PreferenceManager.Device_id)

        Log.e( "DEVICE_ID_PREFERRENCE", deviceID.toString())

        // Check if the device_id is null or empty
//        if (deviceID.isNullOrEmpty()) {
//            // Regenerate device_id here, you can use an appropriate method to do that
//            // For example:
//             deviceID = DeviceInfo().getDeviceSerial(requireContext())
//        }
        val url = "https://www.hubwallet.com/customer-app-redirect/?app_type=android&vendor_id=${preferenceManager.getValueString(PreferenceManager.vendorKey)}&device_id=$deviceID"

        viewBinding.ivBarCodeImg.setImageDrawable(
            QrCodeDrawable(
                requireContext(),
                {
                url
                },
                options
            ),
        )


//        customView(viewBinding.tvEnterNumberLabel,Color.RED,Color.WHITE)
        viewBinding.apply {
            keyboard.btOkay.setOnClickListener {
                if (isValidate()) {
                    viewModel?.customerCheckInNumber(
                        keyboard.keyInput.text.toString(),
                        preferenceManager.getValueString(PreferenceManager.vendorKey),
                        "1"
                    )
                }
            }
            keyboard.submit1.setOnClickListener {
                if (isValidate()) {
                    viewModel?.customerCheckInNumber(
                        keyboard.keyInput.text.toString(),
                        preferenceManager.getValueString(PreferenceManager.vendorKey),
                        "1"
                    )
                }
            }


//            loginButton.setOnClickListener{
//                Toast.makeText(requireContext(), "Check-In Successfully", Toast.LENGTH_SHORT).show()
//                findNavController().popBackStack()
//            }
        }

        observe(viewModel.pinLiveData, ::observePin)
        observe(viewModel.customerMobileNoLiveData, ::customerMobileNoObserver)


    }


    private fun customerMobileNoObserver(response: NetworkResponse<BaseResponse>) {
        when (response) {
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)
            }
            is NetworkResponse.Success -> {
                Log.e("existingCObs000 ", response.toString())
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                if (response.data.status == 1) {
//                    Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_SHORT)
//                        .show()
                    LogoutPop(findNavController(),response.data).show(
                        childFragmentManager, ""
                    )

//                    checkInDialogBox()
                    viewBinding.keyboard.keyInput.setText("")
                    viewModel.builder.clear()

                } else {

                    LogoutPop(findNavController(),response.data).show(
                        childFragmentManager, ""
                    )
                    viewBinding.keyboard.keyInput.setText("")
                    viewModel.builder.clear()
//                    Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_SHORT)
//                        .show()
                }
            }
            is NetworkResponse.Failed -> {
                viewBinding.keyboard.keyInput.setText("")
                viewModel.builder.clear()
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()


            }
        }

    }

    fun observePin(al: String) {

        viewBinding.keyboard.keyInput.setText(al)
    }

    fun customView(v: View, backgroundColor: Int, borderColor: Int) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(100f, 50f, 0f, 0f, 100f, 50f, 0f, 0f)
        shape.setColor(backgroundColor)
        shape.setStroke(3, borderColor)
        v.setBackground(shape)
    }

    fun checkInDialogBox() {
        val checkinDialogBinding = DataBindingUtil.inflate<CheckinDialogBoxLayoutBinding>(
            layoutInflater,
            R.layout.checkin_dialog_box_layout,
            null,
            false
        )
        checkinDialog =
            MaterialAlertDialogBuilder(requireContext()).setView(checkinDialogBinding.root)
                .setCancelable(false).show()

        checkinDialogBinding.apply {
            checkInText.text = "Phone number is not registerd"
            checkInText.text = "You're Checked in!"
            okButton.setOnClickListener {
                checkinDialog?.dismiss()
                findNavController().navigate(R.id.homeScreen)

            }

        }
        checkinDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        checkinDialog?.show()


    }

    private fun isValidate(): Boolean {
        if (viewBinding.keyboard.keyInput.text.toString().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewBinding.keyboard.keyInput.text?.length!! < 14) {
            Toast.makeText(requireContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    val TimerCountDown = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            viewBinding.tvCountDown.text = "Screen will refresh in ${millisUntilFinished / 1000}S"
        }

        override fun onFinish() {
            findNavController().navigate(R.id.homeScreen)
            isTimerFinished = true
        }
    }

    override fun onResume() {
        super.onResume()
        TimerCountDown.start()
    }

    override fun onPause() {
        super.onPause()
        TimerCountDown.cancel()
        isTimerFinished = false
    }

}