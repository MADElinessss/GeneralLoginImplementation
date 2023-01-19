package com.example.generallogin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {
    //@Headers("app/json")
    @POST("/app/login")
    fun userLogin(
        @Body jsonParams : UserModel,
    ): Call<LoginBackendResponse>

    //2 -> idx
    @POST("/app/users/2")
    fun PostUserToken(
        @Header("Authorization") accessToken : UserToken,
    ):Call<LoginBackendResponse>

    @GET("/app/users/2")
    fun GetUserInfo(
        @Header("Authorization") accessToken: String,
    ):Call<UserDetailResponse>

    companion object {
        private const val BASE_URL = "https://1d52-211-106-114-186.jp.ngrok.io/"
        val gson : Gson =   GsonBuilder().setLenient().create();

        //intercepter 고민..
        fun create() : Api{
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            /*
            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(headerInterceptor)
                .build()
            */
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(Api::class.java)
        }
    }
}