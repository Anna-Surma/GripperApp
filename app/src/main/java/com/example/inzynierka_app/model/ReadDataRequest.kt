package com.example.inzynierka_app.model


data class ReadDataRequest(
    val id: Int,
    val jsonrpc: String,
    val method: String,
    val params: Params
)

data class Params(
    val `var`: String
)
