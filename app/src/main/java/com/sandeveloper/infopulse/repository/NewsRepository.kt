package com.sandeveloper.infopulse.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sandeveloper.infopulse.NetworkResult
import com.sandeveloper.infopulse.api.NewsAPI
import com.sandeveloper.models.NewsResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsAPI: NewsAPI){

    private val _newsResponseLiveData = MutableLiveData<NetworkResult<NewsResponse>>()
    val newsResponseLiveData: LiveData<NetworkResult<NewsResponse>>
        get() = _newsResponseLiveData
    var isDataLoaded  = mutableStateOf(false)
    suspend fun getNews(category:String){
        _newsResponseLiveData.postValue(NetworkResult.Loading())
        val response = newsAPI.news(category,"in","21c529a6be4b493a952e1b330afe82cd")

        handleResponse(response)
    }

    private fun handleResponse(response: Response<NewsResponse>) {
        if (response.isSuccessful && response.body() != null) {
            //uid = response.body()!!.user._id
            _newsResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            Log.d("newsCheck",response.body().toString())
            isDataLoaded= mutableStateOf(true)
        }
        else if(response.errorBody()!=null){
            val errorObj = response.errorBody()!!.charStream().readText()
            Log.d("newsCheck",errorObj)
            _newsResponseLiveData.postValue(NetworkResult.Error(errorObj))
        }
        else{
            _newsResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            Log.d("newsCheck","something went wrong")
        }
    }
}