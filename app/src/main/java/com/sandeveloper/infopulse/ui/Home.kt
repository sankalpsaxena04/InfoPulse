package com.sandeveloper.infopulse.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.sandeveloper.infopulse.MainActivity
import com.sandeveloper.infopulse.Networkstatus
import com.sandeveloper.infopulse.NewsViewModel
import com.sandeveloper.models.Article

@SuppressLint("RestrictedApi")
@Composable
fun Home(
    navController: NavController,
    newsViewModel: NewsViewModel,
    news: MutableState<MutableList<Article>>,
    mainActivity: MainActivity
) {



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            GreetingSection("READER", navController,mainActivity)
            ChipSection(
                chips = listOf(
                    "General",
                    "Health",
                    "Business",
                    "Entertainment",
                    "Science",
                    "Sports"
                ), newsViewModel, mainActivity
            )
            NewsTags(news)

        }
    }
}

@Composable
fun NewsTags(articles: MutableState<MutableList<Article>>){
    var size = articles.value.size
    if(size>10){
        size=10
    }
    LazyColumn {
        items(size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)

                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(15.dp)
            ) {
                Column {


                    Text(text = articles.value[it].title+":", color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if(articles.value[it].description.length>50){articles.value[it].description.substring(0,50)}else{articles.value[it].description},
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
    }
}
}

@Composable
fun GreetingSection(
    name: String = "Reader",
    navController: NavController,
    mainActivity: MainActivity
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hi, $name",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary

            )
            Text(
                text = "We wish you have a good day!",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        IconButton(onClick = {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            Toast.makeText(mainActivity,"Logged out Successfully!!",Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        },modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .offset(10.dp, 10.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(30.dp)
            )
        }


    }
}
@Composable
fun ChipSection(
    chips: List<String>,
    newsViewModel: NewsViewModel,
    mainActivity: MainActivity
) {
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow {
        items(chips.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                    .clickable {
                        if (Networkstatus.isNetworkAvailable(mainActivity)){
                            newsViewModel.getNews(chips[it].lowercase())
                        }else{
                            Toast.makeText(mainActivity,"Internet unavilable!!",Toast.LENGTH_SHORT).show()
                        }

                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    .padding(15.dp)
            ) {
                Text(text = chips[it], color = MaterialTheme.colorScheme.onTertiary)
            }
        }
    }
}