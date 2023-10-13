package com.example.restaurantapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import com.example.restaurantapp.retrofit.Resource
import com.example.restaurantapp.user.LoginResponse

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T) : Resource<T> {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(apiCall.invoke())
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
        } as Resource<T>
    }
}
