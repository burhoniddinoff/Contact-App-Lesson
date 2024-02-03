package uz.gita.mycontactb7.data.mapper

import uz.gita.mycontactb7.data.model.ContactUIData
import uz.gita.mycontactb7.data.model.StatusEnum
import uz.gita.mycontactb7.data.model.toStatusEnum
import uz.gita.mycontactb7.data.source.local.entity.ContactEntity
import uz.gita.mycontactb7.data.source.remote.response.ContactResponse

object ContactMapper {

    fun ContactResponse.toUIData() : ContactUIData =
        ContactUIData(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = StatusEnum.SYNC
        )

    fun ContactEntity.toUIData() : ContactUIData =
        ContactUIData(
            id = -1,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = this.statusCode.toStatusEnum()
        )
}

