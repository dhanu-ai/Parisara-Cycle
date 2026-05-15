package com.parisaracycle.app.api

import com.parisaracycle.app.models.DangerZone
import retrofit2.Call
import retrofit2.http.GET

interface ParisaraApi {

    @GET("danger")
    fun getDangerZones(): Call<List<DangerZone>>
}