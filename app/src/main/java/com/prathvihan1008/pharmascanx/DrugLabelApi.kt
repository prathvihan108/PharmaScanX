package com.prathvihan1008.pharmascanx
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query




import retrofit2.Call


interface OpenFdaApiService {
    @GET("drug/label.json")
    fun searchDrugs(@Query("search") searchQuery: String): Call<ResponseBody>
}


