package com.hubwallet.customer_checkin.ui.login

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.DeviceInfo
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.FragmentLoginBinding
import com.hubwallet.customer_checkin.ui.MainActivity
import com.hubwallet.customer_checkin.ui.login.network.model.LoginResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onInitDataBinding() {
        (activity as MainActivity).bottomPowerByIsShow(false)
        (activity as MainActivity).topPowerByIsShow(false)
        viewBinding.viewModel = viewModel
        if (!DeviceInfo().getDeviceSerial(requireContext()).isNullOrEmpty()) {
            preferenceManager.putValueString(
                PreferenceManager.Device_id,
                DeviceInfo().getDeviceSerial(requireContext()).toString()
            )

        } else {
            Toast.makeText(
                requireContext(),
                "Device ID not get, Please Close the app and try again!",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewBinding.apply {
            submit.setOnClickListener {
                val deviceId = DeviceInfo().getDeviceSerial(requireContext())
                Log.e("onInitDataBinding000", deviceId.toString())
                if (isValidate()) {
                    if (deviceId != null) {
                        viewModel!!.loginApi(viewBinding.keyInput.text.toString(), deviceId)
                    }
                }
//                findNavController().navigate(R.id.homeScreen)
            }

            observe(viewModel!!.loginLiveData, ::observePinLogin)
            observe(viewModel!!.loginPinLiveData, ::observePin)
        }

    }

    private fun isValidate(): Boolean {
        viewBinding.apply {
            if (keyInput.text.toString().isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please enter pin code", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun observePin(al: String) {

        viewBinding.keyInput.setText(al)
    }

    private fun observePinLogin(data: NetworkResponse<LoginResponse>) {
        when (data) {
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)
            }

            is NetworkResponse.Success -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                if (data.data.status == 1) {

//                     MainActivity.vendorId = data.data.data.vendor_id ?: ""
                    preferenceManager.putValueString(
                        PreferenceManager.token,
                        "Bearer " + data.data.token.toString()
                    )
                    preferenceManager.putValueString(
                        PreferenceManager.profilePhoto,
                        data.data.data.photo ?: ""
                    )
                    preferenceManager.putValueString(
                        PreferenceManager.salonName,
                        data.data.data.username ?: ""
                    )
                    preferenceManager.putValueString(
                        PreferenceManager.is_card_booking_available,
                        data.data.apt_rules?.cc_online_ch_app ?:""
                    )
                    preferenceManager.putValueString(
                        PreferenceManager.CHECK_IN_ONLINE_BOOKING,
                        data.data.apt_rules?.checkin_app_allow ?:""
                    )

                    Log.e("observePinLoginUserName ", data.data.data.username.toString())
                    preferenceManager.putValueString(
                        PreferenceManager.vendorKey,
                        data.data.data.vendor_id ?: ""
                    )
                    preferenceManager.putValueString(
                        PreferenceManager.tokenRefresh,
                        "Bearer " + data.data.refresh_token
                    )
                    findNavController().navigate(R.id.homeScreen)
                    (activity as MainActivity).apply {
                        setData()
                    }
                    viewBinding.keyInput.setText("")
                    viewModel.builder.clear()
                } else {
                    Toast.makeText(requireContext(), data.data.message, Toast.LENGTH_SHORT).show()
                    viewBinding.keyInput.setText("")
                    viewModel.builder.clear()
                }
                Log.e("observePinLoginRes ", data.toString())

            }

            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)
                Toast.makeText(requireContext(), data.error, Toast.LENGTH_SHORT).show()

            }
        }
    }
}