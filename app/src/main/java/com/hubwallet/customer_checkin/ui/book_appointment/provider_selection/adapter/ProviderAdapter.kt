package com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.utils.DecimalFormatterAmount
import com.hubwallet.customer_checkin.databinding.LayoutProviderItemBinding
import com.hubwallet.customer_checkin.ui.book_appointment.provider_selection.model.ProviderData

class ProviderAdapter : RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder>() {
    val providerList = ArrayList<ProviderData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewHolder {

        val binding = DataBindingUtil.inflate<LayoutProviderItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_provider_item, parent, false
        )
        return ProviderViewHolder((binding))
    }

    override fun onBindViewHolder(holder: ProviderViewHolder, position: Int) {

        holder.binding.apply {
            val model = providerList[position]
            name.text = providerList[position].providerName

            if (model.price?.isNotEmpty() == true) {
                holder.binding.price.text =
                    "$${DecimalFormatterAmount.formatToTwoDecimalPlaces((model.price?.toFloat()))}"
            } else {
                holder.binding.price.text = ""
            }

            if (model.providerName.equals("Any Available")) {
                holder.binding.info.visibility = View.INVISIBLE
                holder.binding.price.text = ""
            }

            Glide.with(holder.binding.root.context).load(providerList[position].image)
                .into(holder.binding.image)


            cvContainer.setOnClickListener {
                model.isSelected = !model.isSelected
                if (providerList[position].isSelected) {
                    providerList.forEachIndexed { index, data ->
                        if (data.isSelected && index != position) {
                            data.isSelected = false
                        }
                    }
                }
              notifyDataSetChanged()
            }
            if (model.isSelected) {
                holder.binding.card.setCardBackgroundColor(Color.parseColor("#24BCFD"))
                holder.binding.name.setTextColor(Color.parseColor("#FFFFFF"))
                holder.binding.price.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                holder.binding.card.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                holder.binding.name.setTextColor(Color.parseColor("#000000"))
                holder.binding.price.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    override fun getItemCount(): Int {
        return providerList.size
    }

    fun updateList(providerListResult: List<ProviderData>) {
        this.providerList.clear()
        this.providerList.addAll(providerListResult)
        notifyDataSetChanged()
    }


    inner class ProviderViewHolder(val binding: LayoutProviderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}