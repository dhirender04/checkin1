package com.hubwallet.customer_checkin.ui.book_appointment.service_selection.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.databinding.LayoutServiceCategoryItemBinding

class ServiceCategoryAdapter(val serviceCategoryClick: (list: List<String>) -> Unit) :
    RecyclerView.Adapter<ServiceCategoryAdapter.ServiceCategoryViewHolder>() {

    var serviceCategoryList =
        ArrayList<com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.Result>()
    val listSelected = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceCategoryViewHolder {
        val binding = DataBindingUtil.inflate<LayoutServiceCategoryItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_service_category_item, parent, false
        )
        return ServiceCategoryViewHolder(binding)

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ServiceCategoryViewHolder, position: Int) {
        val model = serviceCategoryList[position]
        holder.binding.apply {

            tvServiceCategoryName.text = serviceCategoryList[position].category_name
            tvServiceCategoryName.setOnClickListener {
                Log.e("onBindViewHold33333", model.isSelected.toString())
                model.isSelected = !model.isSelected
                notifyItemChanged(position)
                listSelected.clear()
                serviceCategoryList.filter { it.isSelected }.map { it.category_id }.let { it1 ->
                    serviceCategoryClick(it1 as List<String>)
                }
            }

            if (model.isSelected) {
//                tvServiceCategoryName.setBackgroundColor(Color.parseColor("#57cdff"))
                tvServiceCategoryName.setBackgroundDrawable(holder.itemView.resources.getDrawable(R.drawable.rectangle_border_blue_fill))


                tvServiceCategoryName.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
//                tvServiceCategoryName.setBackgroundColor(Color.parseColor("#FFFFFF"))
                tvServiceCategoryName.setBackgroundDrawable(holder.itemView.resources.getDrawable(R.drawable.rectangle_border))
                tvServiceCategoryName.setTextColor(Color.parseColor("#000000"))
            }

        }

    }

    override fun getItemCount(): Int {
        return serviceCategoryList.size
    }

    fun updateList(list: List<com.hubwallet.customer_checkin.ui.book_appointment.service_selection.network.model.Result>?) {
        serviceCategoryList.clear()
        if (list != null) {
            this.serviceCategoryList.addAll(list)
            notifyDataSetChanged()
        }

    }

    inner class ServiceCategoryViewHolder(val binding: LayoutServiceCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}