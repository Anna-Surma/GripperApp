package com.example.inzynierka_app.model

data class DataResponse(
    var jsonrpc: String,
    var id: Int,
    var result: Any
    //var error: Error
)

data class Error(
    var code: Int,
    var message: String
)