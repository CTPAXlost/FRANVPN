package com.franvpn.app.data.api

import com.franvpn.app.data.model.VpnConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * API for fetching subscription content
 */
interface SubscriptionApi {
    
    @GET
    suspend fun getSubscription(@Url url: String): ResponseBody
}
