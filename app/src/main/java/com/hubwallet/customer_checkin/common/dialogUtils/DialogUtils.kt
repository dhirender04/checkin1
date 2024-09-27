package com.hubwallet.customer_checkin.common.dialogUtils

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.databinding.LogoutPopupBinding

object DialogUtils {
    fun openPopup(navController: NavController, context: Context, layoutInflater: LayoutInflater) {
        val bind = DataBindingUtil.inflate<LogoutPopupBinding>(
            layoutInflater,
            R.layout.logout_popup,
            null,
            false
        )
        val dialog = MaterialAlertDialogBuilder(context).setView(bind.root).create()
        // Set the width of the dialog
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT // Set your desired width here
        dialog.window?.attributes = layoutParams
        dialog.show()
        bind.btnContinue.setOnClickListener {
            dialog.dismiss()
            navController.popBackStack(navController.graph.startDestinationId, true)
            navController.navigate(R.id.loginFragment)
        }
        bind.btnCancel.setOnClickListener { dialog.dismiss() }


    }
}