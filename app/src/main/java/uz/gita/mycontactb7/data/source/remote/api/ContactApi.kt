package uz.gita.mycontactb7.data.source.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.gita.mycontactb7.data.source.remote.request.CreateContactRequest
import uz.gita.mycontactb7.data.source.remote.response.ContactResponse

interface ContactApi {

    @GET("api/v1/contact")
    fun getAppContact(): Call<List<ContactResponse>>

    @POST("api/v1/contact")
    fun addContact(@Body data: CreateContactRequest): Call<ContactResponse>
}



