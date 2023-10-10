package com.example.restaurantapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import com.example.restaurantapp.retrofit.Resource
abstract class BaseRepository {

    suspend fun <T> safeApiCall(ApiCall: suspend () -> T) : Any {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(ApiCall.invoke())
            }catch (throwable : Throwable){
                when(throwable){
                    is HttpException -> {
                        Resource.Failure(true,throwable.code(),throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(false,null,null)
                    }
                }
            }
        }
    }
}
