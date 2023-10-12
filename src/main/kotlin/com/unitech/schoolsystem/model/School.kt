package com.unitech.schoolsystem.model

data class School(
    var schoolRowNumber: Int = 0,
    val schoolName: String,
    val schoolType: String,
    val city: String,
    val country: String,
    val schoolAddress: String,
    val contactNumber: String,
    val fax: String,
    val emailAddress: String,
    val schoolWebsite: String
)
