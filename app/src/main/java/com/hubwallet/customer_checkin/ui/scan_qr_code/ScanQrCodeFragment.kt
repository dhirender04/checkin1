package com.hubwallet.customer_checkin.ui.scan_qr_code

import android.util.Log
import android.view.View
import android.widget.Toast
import com.hubwallet.customer_checkin.R
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.utils.BaseResponse
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.FragmentScanQrCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScanQrCodeFragment : BaseFragment<FragmentScanQrCodeBinding>(R.layout.fragment_scan_qr_code) {
    private lateinit var codeScanner: CodeScanner
    private val viewModel: ScanQrCodeViewModel by viewModels()
    @Inject
    lateinit var preferenceManager: PreferenceManager
    override fun onInitDataBinding() {
        viewBinding.llBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
        setScanBar()
//        viewBinding.scannerView.setOnClickListener {
//            codeScanner.startPreview()
//        }
        codeScanner.startPreview()
        observe(viewModel.qrCodeScanLiveData, ::observerQrCodeScan)
    }

    private fun observerQrCodeScan(data: NetworkResponse<BaseResponse>) {
        when (data) {
            is NetworkResponse.Loading -> {
                viewBinding.frameLayoutProgress.visibility = View.VISIBLE
            }
            is NetworkResponse.Success -> {
                Toast.makeText(requireContext(), data.data.message, Toast.LENGTH_SHORT).show()
                viewBinding.frameLayoutProgress.visibility = View.GONE
                findNavController().navigate(R.id.action_scanQrCodeFragment_to_successFullCheckInFragment)
            }
            is NetworkResponse.Failed -> {
                viewBinding.frameLayoutProgress.visibility = View.GONE
            }
        }

    }

    private fun setScanBar() {
        codeScanner = CodeScanner(requireContext(), viewBinding.scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            viewModel.qrCodeCheckIn(customerId = preferenceManager.getValueString(PreferenceManager.customerId), it.toString())

//            activity?.runOnUiThread {
//                findNavController().navigate(R.id.action_scanQrCodeFragment_to_successFullCheckInFragment)
//            }
            Log.e("onInitDataBinding4444 ", it.text.toString())
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            Log.e("onInitDataBinding4444 ", it.toString())
        }

    }
}