package com.sandeveloper.infopulse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandeveloper.infopulse.repository.NewsRepository
import com.sandeveloper.models.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel(){

    val newsResponseLiveData: LiveData<NetworkResult<NewsResponse>>
        get() = newsRepository.newsResponseLiveData

    fun getNews(category:String="general"){
        viewModelScope.launch {
            newsRepository.getNews(category = category)
        }
    }

}