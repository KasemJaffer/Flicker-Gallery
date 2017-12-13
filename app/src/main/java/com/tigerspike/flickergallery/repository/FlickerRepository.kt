package com.tigerspike.flickergallery.repository

import com.google.gson.GsonBuilder
import com.tigerspike.flickergallery.api.FlickerService
import com.tigerspike.flickergallery.entities.Feeds
import com.tigerspike.flickergallery.utils.Constants
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit


class FlickerRepository : FlickerContract {

    private val service: FlickerService

    constructor(service: FlickerService) {
        this.service = service
    }

    constructor() {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.URLs.BACKEND)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        GsonBuilder().setDateFormat("yyyy-mm-dd'T'HH:mm:ssZ").create())
                ).client(OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build())
                .build()
        this.service = retrofit.create(FlickerService::class.java)
    }

    override val feeds: Single<Response<Feeds?>>
        get() {
            return service.feeds("json")
        }

}
