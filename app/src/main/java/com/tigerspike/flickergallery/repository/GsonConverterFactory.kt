package com.tigerspike.flickergallery.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.Charset

class GsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type!!))
        return GsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type?,
                                      parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type!!))
        return GsonRequestBodyConverter(gson, adapter)
    }

    private inner class GsonResponseBodyConverter<T>
    internal constructor(private val gson: Gson, private val adapter: TypeAdapter<T>)
        : Converter<ResponseBody, T> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T? {
            val json = value.string()
            return try {
                val replaceFirst = json.replaceFirst("jsonFlickrFeed(", "")
                adapter.fromJson(replaceFirst.substring(0, replaceFirst.length - 1))
            } catch (e: Exception) {
                Log.e(TAG, "Response body is malformed : " + json, e)
                null
            } finally {
                value.close()
            }
        }
    }

    private inner class GsonRequestBodyConverter<T>
    internal constructor(private val gson: Gson, private val adapter: TypeAdapter<T>)
        : Converter<T, RequestBody> {

        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")

        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            val buffer = Buffer()
            val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
        }
    }

    companion object {

        private val TAG = "GsonConverter"

        /**
         * Create an instance using a default [Gson] instance for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        fun create(): GsonConverterFactory {
            return create(Gson())
        }

        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        // Guarding public API nullability.
        fun create(gson: Gson?): GsonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return GsonConverterFactory(gson)
        }
    }
}