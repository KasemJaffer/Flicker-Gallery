package com.tigerspike.flickergallery.api

import com.tigerspike.flickergallery.entities.Feeds
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickerService {
    @GET("feeds/photos_public.gne")
    fun feeds(@Query("format") format: String): Single<Response<Feeds?>>
}
