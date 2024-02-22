package com.example.cryptoudemy.service

import com.example.cryptoudemy.model.CryptoModel
import com.example.cryptoudemy.utils.Constants.ENDPOINT
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {
    @GET(ENDPOINT)
    fun getData() : Observable<List<CryptoModel>>

}