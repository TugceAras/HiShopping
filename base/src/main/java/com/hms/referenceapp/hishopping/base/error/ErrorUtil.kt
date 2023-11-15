import com.hms.referenceapp.hishopping.base.error.ErrorModel
import com.hms.referenceapp.hishopping.base.error.ErrorType

object ErrorUtil {
    fun mapError(exception: Throwable): ErrorModel {
        val error: ErrorModel? = when (parseMessage(exception.message!!)) {
            401 -> ErrorModel("Unauthorized", 401, ErrorType.UNAUTHORIZED)
            else -> ErrorModel("Error not defined", -1, ErrorType.NOT_DEFINED)
        }
        return error!!
    }

    private fun parseMessage(message: String): Int {
        return if (!message.isNullOrEmpty()) {
            when (message) {
                "HTTP 401 Unauthorized" -> 401
                else -> -1
            }
        } else {
            -1
        }
    }
}