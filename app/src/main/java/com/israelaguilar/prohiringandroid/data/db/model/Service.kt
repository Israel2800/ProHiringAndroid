package com.israelaguilar.prohiringandroid.data.db.model

data class Service(
    var id: String = "",
    var name: String = "",
    var status: String = "",
    var color: String = ""
) {
    // Constructor sin argumentos
    constructor() : this("", "", "", "")
}