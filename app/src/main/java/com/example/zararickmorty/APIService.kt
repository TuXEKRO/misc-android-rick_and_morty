package com.example.zararickmorty

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface APIService {
    @GET("character")
    suspend fun getCharacters(@QueryMap filters: Map<String, String>): Response<ApiResponse>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<Character>
}