package com.example.inzynierka_app.api

import com.example.inzynierka_app.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body

interface ApiHelper {
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    suspend fun readData(@Body request: ReadDataRequest): Response<DataResponse>
    suspend fun writeData(@Body request: WriteDataRequest): Response<DataResponse>
    suspend fun readArray(@Body request: ArrayList<ReadDataRequest>): Response<ArrayResponse>
    suspend fun writeArray(@Body request: ArrayList<WriteDataRequest>): Response<ArrayResponse>
    suspend fun readCPUMode(@Body request: CPUModeRequest): Response<DataResponse>
    suspend fun writeCPUMode(@Body request: WriteCPUModeRequest): Response<DataResponse>
}