package com.sandeveloper.infopulse

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.sandeveloper.infopulse.ScreenNavigation.Navigation
import com.sandeveloper.infopulse.ui.Home
import com.sandeveloper.infopulse.ui.theme.InfoPulseTheme
import com.sandeveloper.models.Article
import com.sandeveloper.models.Source
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private val newsViewModel by viewModels<NewsViewModel>()


    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if(Networkstatus.isNetworkAvailable(this)){
            newsViewModel.getNews()
        }else{
            Toast.makeText(this,"Internet unavilable!!", Toast.LENGTH_SHORT).show()
        }

        setContent {
            InfoPulseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navHostController = rememberNavController()
                    var newsArticles   = remember {
                        mutableStateOf( mutableListOf( Article(Source("a","a"),"a","a","a","a","a","a","a")))
                    }


                    newsViewModel.newsResponseLiveData.observe(this, Observer { itt ->
                        when(itt){
                            is NetworkResult.Success->{
                                newsArticles.value.clear()
                                itt.data!!.articles.forEach {
                                    newsArticles.value.add(it)
                                }



                            }
                            is NetworkResult.Error->{

                            }
                            is NetworkResult.Loading->{


                            }
                        }
                    })
                    val auth = FirebaseAuth.getInstance()
                    val currentUser = auth.currentUser
                    Navigation(navHostController,newsViewModel,newsArticles,this,currentUser)


                }
            }
        }
    }

}

