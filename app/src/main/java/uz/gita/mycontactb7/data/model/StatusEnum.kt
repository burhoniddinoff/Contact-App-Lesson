package uz.gita.mycontactb7.data.model

enum class StatusEnum(val statusCode: Int) {
    SYNC(0), ADD(1), DELETE(2), EDIT(3)
}


fun Int.toStatusEnum(): StatusEnum = when (this) {
    1 -> StatusEnum.ADD
    2 -> StatusEnum.DELETE
    3 -> StatusEnum.EDIT
    else -> StatusEnum.SYNC
}