package com.sandeveloper.infopulse.ScreenNavigation

sealed class Screen(val route: String){
    object SignIn:Screen("SignIn_Screen")
    object Home:Screen("Home_Screen")
    object Profile:Screen("Profile_Screen")
    object NewsDetail:Screen("NewsDetail_Screen")

}