package com.israelaguilar.prohiringandroid.data.db.model

import java.io.Serializable

data class Company(
    val companyName: String,
    val services: String,
    val logoURL: String,
    val name: String,
    val contact: String,
    val socialMedia: String,
) : Serializable