package com.example.inzynierka_app.api

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        sessionManager.fetchAuthToken()?.let {
            requestBuilder
                .addHeader("X-Auth-Token", it)
            Log.i("LoginAuth", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}