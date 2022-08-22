package com.example.inzynierka_app.model

import com.google.gson.annotations.SerializedName

data class WriteDataRequest(var id: Int,
                            var jsonrpc: String,
                            var method: String,
                            var params: ParamsWriteVar
                            )

data class ParamsWriteVar(@SerializedName("var")
                          var params_var:String,
                          var value: Int
                          )