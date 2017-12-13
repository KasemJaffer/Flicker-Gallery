package com.tigerspike.flickergallery.api

import com.google.gson.GsonBuilder
import com.tigerspike.flickergallery.utils.ObserverTestUtil.Companion.getValue
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class FlickerServiceTest {
    private lateinit var service: FlickerService
    private lateinit var mockWebServer: MockWebServer

    @Before
    @Throws(IOException::class)
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(com.tigerspike.flickergallery.repository.GsonConverterFactory.create(
                        GsonBuilder().setDateFormat("yyyy-mm-dd'T'HH:mm:ssZ").create())
                )
                .client(OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .build())
                .build()
                .create(FlickerService::class.java)
    }

    @After
    @Throws(IOException::class)
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun getFeeds() {
        enqueueResponse("feeds.json")
        val feeds = getValue(service.feeds("json")).body()

        val request = mockWebServer.takeRequest()
        assertThat<String>(request.path, `is`<String>("/feeds/photos_public.gne?format=json"))

        assertThat(feeds, notNullValue())
        assertThat(feeds?.title, `is`<String>("Uploads from everyone"))
        assertThat(feeds?.items?.get(0)?.title, `is`<String>("Stop the Stigma....."))
        assertThat(feeds?.items?.get(0)?.media?.url, `is`<String>("https://farm5.staticflickr.com/4646/24164726327_0fc19dfab9_m.jpg"))
    }

    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String) {
        enqueueResponse(fileName, emptyMap<String, String>())
    }

    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String, headers: Map<String, String>) {
        val inputStream = javaClass.classLoader.getResourceAsStream("api-response/" + fileName)
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }
}