package com.hubwallet.customer_checkin.ui.book_appointment.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.extensions.roundTwoDecimalNo
import com.hubwallet.customer_checkin.databinding.LayoutCheckoutItemBinding

class CheckoutAdapter : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewModel>() {

    private var serviceList = ArrayList<String>()
    private var nameList = ArrayList<String>()
    private var priceList = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewModel {
        val binding = DataBindingUtil.inflate<LayoutCheckoutItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_checkout_item, parent, false
        )
        return CheckoutViewModel(binding)

    }

    override fun onBindViewHolder(holder: CheckoutViewModel, position: Int) {
        holder.binding.apply {
            tvServiceName.text = "${serviceList[position]} with ${nameList[position]}"
            tvServiceAmount.text = "$${priceList[position].toFloat().roundTwoDecimalNo().toString()}"
        }

    }

    override fun getItemCount(): Int {
        return if (serviceList.size > 0) serviceList.size else 0

    }

    fun updateList(servicslist: List<String>, namesList: List<String>, pricesList: List<String>) {
        this.serviceList.clear()
        this.nameList.clear()
        this.priceList.clear()
        this.serviceList.addAll(servicslist)
        this.nameList.addAll(namesList)
        this.priceList.addAll(pricesList)
        notifyDataSetChanged()
    }

    inner class CheckoutViewModel(val binding: LayoutCheckoutItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}