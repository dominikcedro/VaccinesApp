package com.example.vaccineapp.vaccines_list_recycler

data class VaccineModel(
    val id: Long,
    val name: String,
    val recommendedAge: Int?,
    val doses: Int,
    val remainingDoses: List<Any>
)