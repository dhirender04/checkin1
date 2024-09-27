package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hubwallet.customer_checkin.R
import com.hubwallet.customer_checkin.common.base.BaseFragment
import com.hubwallet.customer_checkin.common.extensions.observe
import com.hubwallet.customer_checkin.common.extensions.setVisibility
import com.hubwallet.customer_checkin.common.utils.DateTimeUtils
import com.hubwallet.customer_checkin.common.utils.NetworkResponse
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.databinding.FragmentDateAndTimeBinding
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.adapter.EmployeeTimeAvailabilityAdapter
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.model.StylistTime
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.model.TimeAvailable
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.vishal.calendarsingle.singlerowcalendar.calendar.CalendarChangesObserver
import com.vishal.calendarsingle.singlerowcalendar.calendar.CalendarViewManager
import com.vishal.calendarsingle.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.vishal.calendarsingle.singlerowcalendar.selection.CalendarSelectionManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DateAndTimeFragment :
    BaseFragment<FragmentDateAndTimeBinding>(R.layout.fragment_date_and_time) {
    var durationList = ArrayList<String>()
    private val employeeAvailabilityAdapter by lazy {
        EmployeeTimeAvailabilityAdapter(
            callback,
            requireArguments().getString("durations")?.split(",") ?: emptyList()
        )
    }
    private var currentMonth = 0
    private val calendar = Calendar.getInstance()
    private val viewModel by viewModels<DateAndTimeViewModel>()
    private var items = -13
    var currentDate = ""
    private var endTime: String = ""
    var duration = 0
    val timeList = mutableListOf<String>()


    val allTimeAvaList = ArrayList<TimeAvailable>()
    val callback: (str: String, pos: Int) -> Unit = { str, pos ->

        employeeAvailabilityAdapter.update(str, pos)
    }

    private val listStartTime = mutableListOf<String>()
    private val listEndTime = mutableListOf<String>()

    private val myCalendarViewManager = object : CalendarViewManager {
        override fun setCalendarViewResourceId(
            position: Int,
            date: Date,
            isSelected: Boolean
        ): Int {
            return R.layout.calendar_day_item_selected

        }

        override fun bindDataToCalendarView(
            holder: SingleRowCalendarAdapter.CalendarViewHolder,
            date: Date,
            position: Int,
            isSelected: Boolean
        ) {
            holder.view.findViewById<TextView>(R.id.tv_day_calendar_item).text =
                DateUtils.getDay3LettersName(date)
            holder.view.findViewById<TextView>(R.id.tv_date_calendar_item).text =
                DateUtils.getDayNumber(date)
            holder.view.findViewById<TextView>(R.id.view).setVisibility(isSelected)


        }
    }
    private val myCalendarChangesObserver = object : CalendarChangesObserver {
        override fun whenWeekMonthYearChanged(
            weekNumber: String,
            monthNumber: String,
            monthName: String,
            year: String,
            date: Date
        ) {

            viewBinding.dateTextView.text =
                "${DateUtils.getMonthName(date)} ${DateUtils.getYear(date)} "
            // tvDay.text = DateUtils.getDayName(date)

            super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
        }

        override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
            val tempDate = DateTimeUtils.normalDateFormatter.format(date)
            if (currentDate.isEmpty()) {
                currentDate = tempDate
                viewModel.availableTimeByStylist(
                    preferenceManager.getValueString(PreferenceManager.vendorKey),
                    arguments?.getString("service_provider").toString(),
                    currentDate
                )
            } else {
                if (currentDate != tempDate) {
                    currentDate = tempDate
                    viewModel.availableTimeByStylist(
                        preferenceManager.getValueString(PreferenceManager.vendorKey),
                        arguments?.getString("service_provider").toString(),
                        currentDate
                    )
                }
            }



            super.whenSelectionChanged(isSelected, position, date)
        }

        override fun whenCalendarScrolled(dx: Int, dy: Int) {
            super.whenCalendarScrolled(dx, dy)
        }

        override fun whenSelectionRestored() {
            super.whenSelectionRestored()
        }

        override fun whenSelectionRefreshed() {
            super.whenSelectionRefreshed()
        }


    }
    private val mySelectionManager = object : CalendarSelectionManager {
        override fun canBeItemSelected(position: Int, date: Date): Boolean {
            return true
        }
    }

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onInitDataBinding() {
        viewBinding.btNext.setOnClickListener {

            Log.e("onInitDataBinding: ", employeeAvailabilityAdapter.timeSelection.toString())

            val timeList = mutableListOf<String>()
//            for (mutableEntry in employeeAvailabilityAdapter.timeSelection) {
//                if (mutableEntry.value.isEmpty()) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Please select time of ${mutableEntry.key}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }
//                timeList.add(mutableEntry.value)
//            }
            for (mutableEntry in employeeAvailabilityAdapter.timeSelection) {
//                if (mutableEntry.value.isEmpty()) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Please select time of ${employeeAvailabilityAdapter.nameList[key]}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }
                Log.e( "onInitDataBinding: ",employeeAvailabilityAdapter.timeSelection.toString() )
                if (employeeAvailabilityAdapter.nameList.size == employeeAvailabilityAdapter.timeSelection.size) {
                    Log.e("onInitDataBindingMutableVAlue ", mutableEntry.value.toString())
                    timeList.add(mutableEntry.value)
                } else {
                    Toast.makeText(
                        requireContext(),
//                        "Please select time of ${mutableEntry.key}",
                        "Please select time ",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            Log.e("onInitDataBinding789456 ", timeList.toString())
            gotToPayment(
                time = timeList.joinToString(","),
                date = currentDate,
                services = arguments?.getString("services").toString(),
            )
        }
        viewBinding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }

        Log.e("onInitDataBinding: ", employeeAvailabilityAdapter.timeSelection.toString())


        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        viewBinding.mainSingleRowCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            includeCurrentDate = true
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }
        viewBinding.mainSingleRowCalendar.select(0)
        viewBinding.calendarView.minDate = Calendar.getInstance().timeInMillis
        viewBinding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            viewModel.availableTimeByStylist(
                preferenceManager.getValueString(PreferenceManager.vendorKey),
                arguments?.getString("service_provider").toString(),
                "$i-${i2 + 1}-$i3"
            )
            currentDate = "$i-${i2 + 1}-$i3"
        }
        duration = arguments?.getString("duration")?.toInt() ?: 0
        currentDate = DateTimeUtils.normalDateFormatter.format(Calendar.getInstance().time)
        viewBinding.recyclerView.adapter = employeeAvailabilityAdapter

        observe(viewModel.getAvailableStylistLiveData, ::observeDateTime)

    }

    private fun observeDateTime(data: NetworkResponse<StylistTime>) {
        when (data) {
            is NetworkResponse.Failed -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)
            }
            is NetworkResponse.Loading -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(true)
            }
            is NetworkResponse.Success -> {
                viewBinding.progressBar.mainProgressBarContainer.setVisibility(false)

                items = data.data.result.size
                val services = requireArguments().getString("services_name", "").split(",")
                val map = LinkedHashMap<String, List<String>>()
                Log.e("observeDateTime: ", "${services.size} ${data.data.result.size}")

                val nameList = mutableListOf<String>()
                requireArguments().getString("provider_name", "").split(",").forEach {

                    nameList.add(it)
                    if (!data.data.result.containsKey(it)) {
                        map[it] = emptyList()
                    } else {
                        map.put(it, data.data.result[it] ?: emptyList())
                    }
                }

                employeeAvailabilityAdapter.updateMap(
                    map,
                    data.data.stylist_appointment,
                    data.data.customer_appointment,
                    currentDate,
                    services,nameList
                )
                if (data.data.status == 0) {
                    Toast.makeText(requireContext(), data.data.message, Toast.LENGTH_SHORT)
                        .show()
                }


            }
        }
    }

    private fun gotToPayment(time: String, date: String, services: String = "") {
        Log.e("gotToPayment: ", time)
        if (time.isEmpty()) {
            Toast.makeText(requireContext(), "Please select time", Toast.LENGTH_SHORT).show()
            return
        }
        val list = time.split(",")
        list.forEach {
            if (it.isEmpty()) {
                Toast.makeText(requireContext(), "Please select time", Toast.LENGTH_SHORT)
                    .show()
                return
            }
        }


        val calen = Calendar.getInstance()
        calen.time = DateTimeUtils.timeFormatter24.parse(time)
        calen.add(Calendar.MINUTE, duration.toInt())
        endTime = DateTimeUtils.timeFormatterHM.format(calen.time)

        Log.e("gotToPaymentTimeSplit ", time.split(",").toString())
        val listOfTime = time.split(",")
        val calendar = Calendar.getInstance()
        listOfTime.forEachIndexed { i, time ->
            listStartTime.add(time)
            durationList.addAll(arguments?.getString("durations").toString().split(","))
            Log.e("gotToPayment1234", durationList.toString())
            val date = DateTimeUtils.timeFormatter24.parse(time)
            calendar.time = date
            calendar.add(Calendar.MINUTE, durationList[i].toInt())
            listEndTime.add(DateTimeUtils.timeFormatter24.format(calendar.time))
        }

        Log.e("gotToPaymentStartTime ", listStartTime.toString())
        Log.e("gotToPaymentStartTime11 ", listStartTime.joinToString(","))
        Log.e("gotToPaymentEndTime ", listEndTime.toString())


        val bundle = Bundle()
//        bundle.putString(
//            "time",
//            time
//        )
        bundle.putString(
            "time",
            listStartTime.joinToString(",")
        )
        bundle.putString(
            "end_time",
            listEndTime.joinToString(",")
        )
        bundle.putString("date", date)
        bundle.putString(
            "duration",
            requireArguments().getString("duration", "0")
        )
        bundle.putString(
            "durations",
            requireArguments().getString("durations", "")
        )
        bundle.putString(
            "services", arguments?.getString("services", "") ?: ""
        )


        bundle.putString(
            "service_provider",
            arguments?.getString("service_provider", "")
        )
        bundle.putString(
            "prices",
            requireArguments().getString("prices", "")
        )
        bundle.putString(
            "services_name",
            requireArguments().getString("services_name", "")
        )
        bundle.putString(
            "flow",
            "by_stylist"
        )
        bundle.putString(
            "provider_name",
            requireArguments().getString("provider_name", "")
        )


        findNavController().navigate(R.id.checkoutFragment, bundle)

//                    viewModel.addAppointment(
//                        preferenceManager.getValueString(PreferenceManager.vendorKey),
//                        preferenceManager.getValueString(PreferenceManager.customerId),
//                        arguments?.getString("service_provider", "") ?: "",
//                        services,
//                        currentDate,
//                        time,
//                        endTime,
//                        "by_stylist"
//
//                    )


    }


    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return  getDates(mutableListOf())
    }


    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)

        list.add(calendar.time)
        var i = 0
        while (i < 365) {
            calendar.add(Calendar.DATE, +1)
            list.add(calendar.time)
            i++
        }

        return list
    }
}