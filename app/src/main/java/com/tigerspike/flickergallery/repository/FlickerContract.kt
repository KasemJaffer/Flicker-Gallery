package com.tigerspike.flickergallery.repository

import com.tigerspike.flickergallery.entities.Feeds
import io.reactivex.Single
import retrofit2.Response

/**
 * Contract to be used by the views
 */
interface FlickerContract {
    val feeds: Single<Response<Feeds?>>
}