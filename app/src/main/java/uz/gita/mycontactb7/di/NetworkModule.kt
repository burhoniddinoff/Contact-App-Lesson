package uz.gita.mycontactb7.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.mycontactb7.data.source.remote.api.ContactApi
import uz.gita.mycontactb7.utils.NetworkStatusValidator
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @[Provides Singleton]
    fun provideGson(): Gson = Gson()

    @[Provides Singleton]
    fun provideOkHttp(@ApplicationContext context: Context, networkStatusValidator: NetworkStatusValidator) : OkHttpClient {
        val cacheSize = (50 * 1024 * 1024).toLong()  // 50 MB
        val cache = Cache(context.cacheDir, cacheSize)
        val maxStale = 60 * 60 * 24 * 30

        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context))
            .addInterceptor { chain ->
                if (!networkStatusValidator.hasNetwork) {
                    val newRequest = chain.request()
                        .newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma").build()
                    chain.proceed(newRequest)
                } else chain.proceed(chain.request())
            }
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .cache(cache).build()
    }

    @[Provides Singleton]
    fun provideRetrofit(okHttpClient : OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://db96-195-158-16-140.ngrok-free.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @[Provides Singleton]
    fun provideContactApi(retrofit: Retrofit): ContactApi = retrofit.create(ContactApi::class.java)
}
