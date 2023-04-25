package com.example.hikingbook.data.quote

import retrofit2.Response
import retrofit2.http.GET

interface QuotableApiService {
    @GET("random")
    suspend fun getRandomQuote(): Response<QuoteResponse>
}