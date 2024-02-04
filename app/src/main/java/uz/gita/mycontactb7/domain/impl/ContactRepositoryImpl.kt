package uz.gita.mycontactb7.domain.impl

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.gita.mycontactb7.data.mapper.ContactMapper.toUIData
import uz.gita.mycontactb7.data.model.ContactUIData
import uz.gita.mycontactb7.data.model.StatusEnum
import uz.gita.mycontactb7.data.model.toStatusEnum
import uz.gita.mycontactb7.data.source.local.dao.ContactDao
import uz.gita.mycontactb7.data.source.local.entity.ContactEntity
import uz.gita.mycontactb7.data.source.remote.api.ContactApi
import uz.gita.mycontactb7.data.source.remote.request.CreateContactRequest
import uz.gita.mycontactb7.data.source.remote.response.ContactResponse
import uz.gita.mycontactb7.data.source.remote.response.ErrorResponse
import uz.gita.mycontactb7.domain.ContactRepository
import uz.gita.mycontactb7.utils.NetworkStatusValidator
import java.lang.Exception
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao,
    private val gson: Gson,
    private val api: ContactApi,
    private val networkStatusValidator: NetworkStatusValidator,
) : ContactRepository {

    override fun getAllContact(
        successBlock: (List<ContactUIData>) -> Unit,
        errorBlock: (String) -> Unit,
    ) {
        api.getAppContact().enqueue(object : Callback<List<ContactResponse>> {
            override fun onResponse(
                call: Call<List<ContactResponse>>,
                response: Response<List<ContactResponse>>,
            ) {
                if (response.isSuccessful && response.body() != null) {
                    successBlock.invoke(
                        mergeData(
                            response.body()!!, contactDao.getAllContactFromLocal()
                        )
                    )
                } else if (response.errorBody() != null) {
                    val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    if (data != null) {
                        errorBlock.invoke(data.message)
                    } else {
                        errorBlock.invoke("Unknown error!")
                    }
                } else errorBlock.invoke("Unknown error!")
            }

            override fun onFailure(call: Call<List<ContactResponse>>, t: Throwable) {
                t.message?.let { errorBlock.invoke(it) }
            }
        })
    }

    override fun addContact(firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
        if (networkStatusValidator.hasNetwork) {
            val request = CreateContactRequest(firstName, lastName, phone)
            api.addContact(request).enqueue(object : Callback<ContactResponse> {
                override fun onResponse(
                    call: Call<ContactResponse>,
                    response: Response<ContactResponse>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.errorBody() != null) {
                        val data = gson.fromJson(
                            response.errorBody()!!.string(), ErrorResponse::class.java
                        )
                        if (data != null) {
                            errorBlock.invoke(data.message)
                        } else {
                            errorBlock.invoke("Unknown error!")
                        }
                    } else errorBlock.invoke("Unknown error!!")
                }

                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    t.message?.let { errorBlock.invoke(it) }
                }
            })
        } else {
            contactDao.insertContact(
                ContactEntity(
                    0, 0, firstName, lastName, phone, StatusEnum.ADD.statusCode
                )
            )
            successBlock.invoke()
        }
    }

    override fun syncWithServer(finishBlock: () -> Unit, errorBlock: (String) -> Unit) {
        val list = contactDao.getAllContactFromLocal()
        list.forEach {
            try {
                val response = api.addContact(CreateContactRequest(it.firstName, it.lastName, it.phone)).execute()
                if (response.isSuccessful && response.body() != null) {
                    finishBlock.invoke()
                } else if (response.errorBody() != null) {
                    val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    if (data != null) { errorBlock.invoke(data.message) }
                    else { errorBlock.invoke("Unknown error!") }
                } else errorBlock.invoke("Unknown error!!")
                contactDao.deleteContact(it)
            } catch (e: Exception) {
                e.message?.let { it1 -> errorBlock.invoke(it1) }
            }
        }

    }

    private fun mergeData(remoteList: List<ContactResponse>, localList: List<ContactEntity>): List<ContactUIData> {
        val result = ArrayList<ContactUIData>()
        result.addAll(remoteList.map { it.toUIData() })

        var index = remoteList.lastOrNull()?.id ?: 0      // face
        localList.forEach { entity ->
            when (entity.statusCode.toStatusEnum()) {
                StatusEnum.ADD -> {
                    result.add(entity.toUIData(++index))
                }

                StatusEnum.EDIT -> {
                    val findData = result.find { it.id == entity.remoteID }
                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        result[findIndex] = newData
                    }
                }

                StatusEnum.DELETE -> {
                    val findData = result.find { it.id == entity.remoteID }
                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        result[findIndex] = newData
                    }
                }

                StatusEnum.SYNC -> {}
            }
        }

        return result
    }
}