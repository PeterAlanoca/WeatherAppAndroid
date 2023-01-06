package peter.alanoca.weatherapp.model.repository.datasource

import android.content.Context
import peter.alanoca.weatherapp.R
import retrofit2.HttpException
import java.lang.Exception
import java.net.HttpURLConnection

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {

        fun <T> success(data: T, message: String): Resource<T> {
            return Resource(Status.SUCCESS, data, message)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> error(e: Exception, context: Context, data: T? = null): Resource<T> {
            var message = context.resources.getString(R.string.app_http_unknown_error)
            if (e is HttpException) {
                if (!e.response()?.isSuccessful!!) {
                    message = when (e.response()!!.raw().code) {
                        HttpURLConnection.HTTP_BAD_REQUEST -> context.resources.getString(R.string.app_http_rest_error_in_the_request)
                        HttpURLConnection.HTTP_INTERNAL_ERROR -> context.resources.getString(R.string.app_http_rest_internal_error_in_the_server)
                        HttpURLConnection.HTTP_NOT_FOUND -> context.resources.getString(R.string.app_http_rest_server_could_not_be_found)
                        HttpURLConnection.HTTP_UNAUTHORIZED -> context.resources.getString(R.string.app_http_session_expired)
                        else -> context.resources.getString(R.string.app_http_unknown_error)
                    }
                }
            }
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

}