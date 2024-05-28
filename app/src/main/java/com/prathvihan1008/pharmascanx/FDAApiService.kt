package com.prathvihan1008.pharmascanx

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FDAApiService {
    @GET("drug/label.json")
    fun getDrugLabel(@Query("search") query: String?): Call<Any>
}