package uz.gita.mycontactb7.domain.impl

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.gita.mycontactb7.data.mapper.ContactMapper.toUIData
import uz.gita.mycontactb7.data.model.ContactUIData
import uz.gita.mycontactb7.data.model.StatusEnum
import uz.gita.mycontactb7.data.source.local.AppDatabase
import uz.gita.mycontactb7.data.source.local.entity.ContactEntity
import uz.gita.mycontactb7.data.source.remote.ApiClient
import uz.gita.mycontactb7.data.source.remote.request.CreateContactRequest
import uz.gita.mycontactb7.data.source.remote.response.ContactResponse
import uz.gita.mycontactb7.data.source.remote.response.ErrorResponse
import uz.gita.mycontactb7.domain.ContactRepository
import uz.gita.mycontactb7.utils.NetworkStatusValidator
import uz.gita.mycontactb7.utils.logger

class ContactRepositoryImpl private constructor() : ContactRepository {
    companion object {
        private lateinit var repository: ContactRepository

        fun init() {
            if (!(::repository.isInitialized)) repository = ContactRepositoryImpl()
        }

        fun getInstance() = repository
    }

    private val api = ApiClient.api
    private val gson = Gson()
    private val contractDao = AppDatabase.getInstance().getContactDao()

    override fun getAllContact(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit, ) {
        api.getAppContact().enqueue(object : Callback<List<ContactResponse>> {
            override fun onResponse(call: Call<List<ContactResponse>>, response: Response<List<ContactResponse>>, ) {
                if (response.isSuccessful && response.body() != null) {
                    val remoteData = response.body()!!.map { it.toUIData() }
                    val localData = contractDao.getAllContactFromLocal().map { it.toUIData() }
                    val result = ArrayList<ContactUIData>()
                    result.addAll(remoteData)
                    result.addAll(localData)
                    successBlock.invoke(result)
                } else if (response.errorBody() != null) {
                    val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    errorBlock.invoke(data.message)
                } else errorBlock.invoke("Unknown error!")
            }

            override fun onFailure(call: Call<List<ContactResponse>>, t: Throwable) {
                logger(t.message.toString())
                logger(t.localizedMessage.toString())
                t.message?.let { errorBlock.invoke(it) }
            }
        })
    }

    override fun addContact(firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
        if (NetworkStatusValidator.hasNetwork) {
            val request = CreateContactRequest(firstName, lastName, phone)
            api.addContact(request).enqueue(object : Callback<ContactResponse> {
                override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>, ) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.errorBody() != null) {
                        val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                        errorBlock.invoke(data.message)
                    } else errorBlock.invoke("Unknown error!!")
                }

                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    t.message?.let { errorBlock.invoke(it) }
                }
            })
        } else {
            contractDao.insertContact(ContactEntity(0, firstName, lastName, phone, StatusEnum.ADD.statusCode))
            successBlock.invoke()
        }
    }
}