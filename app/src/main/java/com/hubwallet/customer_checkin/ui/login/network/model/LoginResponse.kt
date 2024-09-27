package com.hubwallet.customer_checkin.ui.login.network.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val api_hit: String,
    val business_info: BusinessInfo,
    val calendar_start_time: CalendarStartTime,
    val checkAttendenceButton: Int,
    val checkLongButton: Int,
    val checkShortButton: Int,
    val color_setting: ColorSetting,
    val `data`: Data,
    val employee_title: List<EmployeeTitle>,
    val employee_type: List<EmployeeType>,
    val getMachineIp: GetMachineIp,
    val is_tmc_show: String,
    val message: String,
    val permission: List<Permission>,
    val refresh_token: String,
    val role_id: String,
    val screen_lock_time: String,
    val screen_view: String,
    val services: List<Service>,
    val states: List<State>,
    val status: Int,
    val stylist_id: String,
    val stylist_id_product: String,
    val tmc: String,
    val token: String,
    val username: String,

    @SerializedName("apt_rules")
    val apt_rules: AptRules?
)