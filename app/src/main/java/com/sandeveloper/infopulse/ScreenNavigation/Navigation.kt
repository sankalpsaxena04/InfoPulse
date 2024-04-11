package com.sandeveloper.infopulse.ScreenNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.firebase.auth.FirebaseUser
import com.sandeveloper.infopulse.MainActivity
import com.sandeveloper.infopulse.NewsViewModel
import com.sandeveloper.infopulse.ui.Home
import com.sandeveloper.infopulse.ui.NewsDetail
import com.sandeveloper.infopulse.ui.Profile
import com.sandeveloper.infopulse.ui.auth.SignIn
import com.sandeveloper.models.Article


@Composable
fun Navigation(
    navHostController: NavHostController,
    newsViewModel: NewsViewModel,
    news: MutableState<MutableList<Article>>,
    mainActivity: MainActivity,
    currentUser: FirebaseUser?
){

    NavHost(navController = navHostController, startDestination = "auth" ) {
        navigation(startDestination = if(currentUser==null) {
            Screen.SignIn.route
        }else{
             Screen.Home.route
             }, route="auth"){
            composable(route = Screen.SignIn.route){
                SignIn(navHostController,mainActivity)
            }

            composable(route = Screen.Home.route){
                Home(navHostController,newsViewModel, news,mainActivity)
            }
            composable(route = Screen.NewsDetail.route){
                NewsDetail(navHostController)
            }
            composable(route = Screen.Profile.route){
                Profile(navHostController)
            }



        }

    }
}