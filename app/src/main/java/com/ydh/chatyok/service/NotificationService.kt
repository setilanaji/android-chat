package com.ydh.chatyok.service

import com.ydh.chatyok.ConstantUtil
import com.ydh.chatyok.PayloadModel
import com.ydh.chatyok.ResponseModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {
    @POST("send")
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=${ConstantUtil.API_KEY}"
    )
    suspend fun sendNotification(@Body body: PayloadModel): ResponseModel
}