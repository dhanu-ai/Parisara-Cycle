package com.example.frontend.network

import com.example.frontend.model.Buddy
import com.example.frontend.model.DangerZone
import retrofit2.http.GET

interface ApiService {

    @GET("buddy/")
    suspend fun getBuddies(): List<Buddy>

    @GET("danger/")
    suspend fun getDangerZones(): List<DangerZone>
}