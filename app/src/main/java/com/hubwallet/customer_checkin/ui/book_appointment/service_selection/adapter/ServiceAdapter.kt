package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.databinding.LayoutServiceItemBinding
import com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.servicelist.ServiceListResult

class ServiceAdapter : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
      var serviceList = ArrayList<ServiceListResult>()
   private val filterList = ArrayList<ServiceListResult>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = DataBindingUtil.inflate<LayoutServiceItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_service_item, parent, false
        )
        return ServiceViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val model = filterList[position]
        holder.binding.apply {
            tvServiceName.text = model.service_name
            tvServiceName.setOnClickListener {
                model.isSelected = !model.isSelected
                notifyItemChanged(position)
            }

            if (model.isSelected) {
                holder.binding.cardContainer.setCardBackgroundColor(Color.parseColor("#24BCFD"))
                holder.binding.tvServiceName.setTextColor(Color.parseColor("#FFFFFF"))
             } else {
                holder.binding.cardContainer.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                holder.binding.tvServiceName.setTextColor(Color.parseColor("#000000"))
             }

        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    fun updateList(list: List<ServiceListResult>) {
        serviceList.clear()
        filterList.clear()
        if (list != null) {
            serviceList.addAll(list)
            filterList.addAll(list)
            notifyDataSetChanged()
        }

    }

    fun filterList(listSelected: List<String>) {
        filterList.clear()

        if (listSelected.isEmpty()) {
            filterList.addAll(serviceList)
        } else {
            filterList.clear()
            serviceList.forEach { result ->
                listSelected.forEach {
                    if (it.equals(result.category_id, false)) {
                        filterList.add(result)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun filterListBySearch(toString: String) {
        filterList.clear()
        if (toString.isEmpty()) {
            filterList.addAll(serviceList)
        } else {
            filterList.clear()
            serviceList.forEach {
                if (it.service_name?.lowercase()?.contains(toString.lowercase()) == true) {
                    filterList.add(it)
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class ServiceViewHolder(val binding: LayoutServiceItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}