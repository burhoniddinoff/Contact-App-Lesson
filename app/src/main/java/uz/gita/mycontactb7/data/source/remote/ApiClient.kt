package uz.gita.mycontactb7.data.source.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.mycontactb7.data.source.remote.api.ContactApi
import uz.gita.mycontactb7.utils.NetworkStatusValidator
import uz.gita.mycontactb7.utils.logger
import java.sql.Time
import java.util.concurrent.TimeUnit

object ApiClient {
    lateinit var api: ContactApi

    fun init(context: Context) {
        val cacheSize = (50 * 1024 * 1024).toLong()  // 50 MB
        val cache = Cache(context.cacheDir, cacheSize)
        val maxStale = 60 * 60 * 24 * 30

        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(context))
                .addInterceptor { chain ->
                    if (!NetworkStatusValidator.hasNetwork) {
                        val newRequest = chain.request()
                            .newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build()

                        chain.proceed(newRequest)
                    } else chain.proceed(chain.request())
                }
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cache(cache)
                .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://a5a0-195-158-16-140.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        api = retrofit.create(ContactApi::class.java)
    }
}

// auth2