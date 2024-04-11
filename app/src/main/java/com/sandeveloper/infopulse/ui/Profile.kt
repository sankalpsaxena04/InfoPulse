package com.sandeveloper.infopulse.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Profile(
    navController: NavController
) {

    var name: String = "Yolo"
    var phone: String = "+91xxxxxxxxxx"
    var age = 23
    val scrollState = rememberScrollState()
    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()
            }, modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
                .offset(10.dp, 10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew, contentDescription = "back",
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = "Profile",
            style = TextStyle(fontSize = 55.sp, fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(0.dp, 60.dp, 0.dp, 60.dp)
                .align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)

            )
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(10.dp, 0.dp)


            ) {


                Text(
                    text = "Name        : $name",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(0.dp, 30.dp, 0.dp, 3.dp)
                )
                Text(
                    text = "Phone No.: $phone",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(0.dp, 6.dp, 0.dp, 3.dp)
                )
                Text(
                    text = "Age            : $age",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(0.dp, 6.dp, 0.dp, 30.dp)
                )
            }
        }
    }

}
