package com.sandeveloper.infopulse.ui.auth

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.sandeveloper.infopulse.MainActivity
import com.sandeveloper.infopulse.R
import com.sandeveloper.infopulse.ScreenNavigation.Screen
import java.util.concurrent.TimeUnit

@Composable
fun SignIn(navController: NavController,mainActivity:MainActivity) {
    var number by remember { mutableStateOf("+91") }

    var verified = remember {
        mutableStateOf(false)
    }
    var verificationInProgress = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val verificationCodeState = remember { mutableStateOf("") }
    val resendToken = remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background),

        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Image(
            painter = painterResource(id = R.drawable.news1),
            contentDescription = "image description",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(10.dp, 40.dp, 10.dp, 0.dp)
                .fillMaxSize()
        )
        Text(
            text = "InfoPulse",
            style = TextStyle(fontSize = 55.sp, fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 3.dp)
        )
        Text(
            text = "Stay informed, news at fingertips.",
            style = TextStyle(fontSize = 15.8.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 55.dp)
        )
        InputNumberOtp(
            verificationInProgress,
            number,
            mainActivity,
            navController,
            verificationCodeState,
            auth,
            resendToken,
            verified
        )
        FilledTonalButton(
            onClick = {
               navController.navigate(Screen.Home.route)
            },
            modifier = Modifier
                .padding(45.dp, 20.dp, 52.dp, 55.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)


        ) {
            Text(
                text = "Read News if otp not received",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            )
        }

    }
}

@Composable
fun InputNumberOtp(
    verificationInProgress: MutableState<Boolean>,
    number: String,
    mainActivity: MainActivity,
    navController: NavController,
    verificationCodeState: MutableState<String>,
    auth: FirebaseAuth,
    resendToken: MutableState<String>,
    verified: MutableState<Boolean>,


    ) {
    var num by remember {
        mutableStateOf(number)
    }

    if (!verificationInProgress.value) {
        Text(
            text = "Enter Phone Number to SignIn",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.W900,
                textAlign = TextAlign.Left
            ),

            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(0.dp, 70.dp, 0.dp, 5.dp)
        )
        OutlinedTextField(value = num,
            onValueChange = { num = it },
            label = {
                Text(text = "Enter your Number here", color = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 10.dp, 30.dp, 10.dp),
            placeholder = {
                Text(
                    text = "+91xxxxxxxxxx"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),

            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Phone No.",
                    tint = MaterialTheme.colorScheme.secondary
                )
            })

        FilledTonalButton(
            onClick = {
                if (num.length == 13) {

                    val callbacks =
                        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                                Log.d("checkAuth", "onVerificationCompleted:$credential")
                                navController.navigate(Screen.Home.route)
                                signInWithPhoneAuthCredential(credential,mainActivity,verified)
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                // This callback is invoked in an invalid request for verification is made,
                                // for instance if the the phone number format is not valid.
                                Log.w("checkAuth", "onVerificationFailed", e)

                                if (e is FirebaseAuthInvalidCredentialsException) {
                                    // Invalid request
                                } else if (e is FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                                    // reCAPTCHA verification attempted with null Activity
                                }

                                // Show a message and update the UI
                            }

                            override fun onCodeSent(
                                verificationId: String,
                                token: PhoneAuthProvider.ForceResendingToken,
                            ) {
                                verificationInProgress.value=true
                                Log.d("checkAuth", "onCodeSent:$verificationId")

                                verificationCodeState.value = verificationId
                                resendToken.value = token.toString()
                            }
                        }


                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(num) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(mainActivity) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)





                } else {
                    Toast.makeText(mainActivity, "Enter a valid number!!", Toast.LENGTH_SHORT)
                        .show()
                }

//                    navController.navigate(route = Screen.OTP.route)
            },
            modifier = Modifier
                .padding(30.dp, 10.dp, 30.dp, 25.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)


        ) {
            Text(
                text = "Send OTP",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            )
        }

    } else {

        OtpScreen(
            verificationCodeState,
            mainActivity,
            verified
        )

    }

}

private fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    context: MainActivity,
    verified: MutableState<Boolean>
) {

    val auth = FirebaseAuth.getInstance()
    auth.signInWithCredential(credential)
        .addOnCompleteListener(context) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                verified.value=true
                Toast.makeText(context,"Login Successful",Toast.LENGTH_SHORT).show()

                val user = task.result?.user
            } else {
                // Sign in failed, display a message and update the UI
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                }
                // Update UI
            }
        }
}


@Composable
fun OtpScreen(
    verificationId: MutableState<String>,
    context: MainActivity,
    verified: MutableState<Boolean>
) {

    Text(
        text = "Enter the OTP:",
        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.ExtraBold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(0.dp, 90.dp, 0.dp, 35.dp)
    )
    val code = OtpInput()
    FilledTonalButton(
        onClick = {
            if(code.length>6) {
                val credential = PhoneAuthProvider.getCredential(verificationId.value, code)
                signInWithPhoneAuthCredential(credential, context, verified)
            }
            else{
                Toast.makeText(context,"enter valid code",Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier
            .padding(45.dp, 20.dp, 52.dp, 55.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)


    ) {
        Text(
            text = "Login",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        )
    }
}



@Composable
fun OtpInput():String {
    var otpValue by remember {
        mutableStateOf("")
    }
    BasicTextField(value = otpValue, onValueChange ={
        if(it.length<=6){
            otpValue = it
        }
    },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                repeat(6){index ->
                    val char = when{
                        index>=otpValue.length ->""
                        else -> otpValue[index].toString()
                    }

                    Text(
                        text = char,
                        modifier = Modifier
                            .width(40.dp)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.onSecondaryContainer,
                                RoundedCornerShape(8.dp)
                            )
                            .height(45.dp)
                            .padding(2.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center,


                        )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
    return otpValue

}