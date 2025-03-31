package com.example.media.ui.screen.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.media.R
import com.example.media.ui.constant.FunctionConstants.Companion.ToastMessage
import com.example.media.ui.constant.FunctionConstants.Companion.gradientBrush
import com.example.media.ui.constant.FunctionConstants.Companion.isValidEmail
import com.example.media.ui.screen.login.LoginObject
import com.example.media.ui.viewModel.AuthViewModel

@Composable
fun SignUpScreen(navController: NavController) {

    fun navigateToLoginScreen(){
        navController.navigate(LoginObject)
    }

    SignUpScreenPreview(::navigateToLoginScreen)
}


@Composable
fun SignUpScreenPreview(navigateToLoginScreen : () -> Unit) {



    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()
            .background(brush = gradientBrush)
            .padding(innerPadding),
            contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.fillMaxWidth()) {

                SignUpCardView(navigateToLoginScreen)
            }

        }
    }
    
}

@Composable
fun SignUpCardView(navigateToLoginScreen : () -> Unit,
                   viewModel: AuthViewModel = hiltViewModel()){


    val context = LocalContext.current

    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }

    val focusManger = LocalFocusManager.current
    val focusRequestUserName = remember { FocusRequester() }
    val focusRequestEmail = remember { FocusRequester() }
    val focusRequestPassword = remember { FocusRequester() }
    val focusRequestConfirmPassword = remember { FocusRequester() }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isLoading by viewModel::isLoading


    fun signupValidation(){
        when{
            userName.text.isEmpty() -> {
                focusRequestUserName.requestFocus()
                ToastMessage(context = context, content = "User Name is required")
                return
            }

            email.text.isEmpty() -> {
                focusRequestEmail.requestFocus()
                ToastMessage(context, "Email  is Required")
                return
            }

            !isValidEmail(email.text.trim()) -> {
                focusRequestEmail.requestFocus()
                ToastMessage(context, "Invalid email format")
                return
            }


            password.text.isEmpty() -> {
                focusRequestPassword.requestFocus()
                ToastMessage(context = context, content = "Password is required")
                return
            }

            password.text.length < 6 -> {
                focusRequestPassword.requestFocus()
                ToastMessage(context, "Password must be at least 6 characters long")
                return
            }

            confirmPassword.text.isEmpty() -> {
                focusRequestConfirmPassword.requestFocus()
                ToastMessage(context = context, content = "Confirm Password is required")
                return
            }

            password.text != confirmPassword.text ->{
                focusRequestPassword.requestFocus()
                ToastMessage(context = context, content = "Passwords do not match")
                return
            }
        }

        focusManger.clearFocus()

        viewModel.signUp(userName.text.trim(), email.text.trim(), password.text.trim()) { success ->
            if (success) {
                Toast.makeText(context, "Verify your email before logging in!", Toast.LENGTH_LONG).show()
                navigateToLoginScreen()
            } else {
                Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }


    }


    Card(modifier = Modifier.fillMaxWidth().padding(15.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.fillMaxWidth().padding(5.dp)) {

            Text(stringResource(id = R.string.signup),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().padding(5.dp)
                    .padding(top = 5.dp),
                textAlign = TextAlign.Center)

            OutlinedTextField(value = userName, onValueChange = { text ->
                userName = text },
                label = {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append("User Name ")
                        }
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                    })
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManger.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequestUserName)
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp),
                shape = RoundedCornerShape(10.dp), singleLine = true)


            OutlinedTextField(
                value = email,
                onValueChange = { newText ->
                    email = newText
                },
                label = {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append("Email ")
                        }
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                    })
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequestEmail)
                    .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 10.dp),
                shape = RoundedCornerShape(10.dp),
                textStyle = TextStyle(color = Color.Black),

                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManger.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true)


            OutlinedTextField(value = password,
                onValueChange = { text -> password = text },
                label = {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append("Password ")
                        }
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                    })
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManger.moveFocus(FocusDirection.Down) }
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Default.Visibility
                    else Icons.Default.VisibilityOff

                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        }.focusRequester(focusRequestPassword)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { text -> confirmPassword = text },
                label = {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append("Confirm Password ")
                        }
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append("*")
                        }
                    })
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        Icons.Default.Visibility
                    else Icons.Default.VisibilityOff

                    Icon(
                        imageVector = image,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        tint = Color.Black,
                        modifier = Modifier.clickable {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManger.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequestConfirmPassword)
                    .padding(start = 15.dp, end = 15.dp, top = 10.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    if (!isLoading) signupValidation()

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp)
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.color_primary)),
                shape = RoundedCornerShape(10.dp),
                enabled = !isLoading ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Sign Up",
                        color = Color.White,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp), contentAlignment = Alignment.Center
            ) {

                Text(
                    stringResource(id = R.string.already_have_an_account_login),
                    fontSize = 14.sp,
                    color = colorResource(R.color.color_primary),
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null) {
                        navigateToLoginScreen()
                    }
                )


            }



        }
    }

}

@PreviewLightDark
@Composable
fun SignUpPreviewScreen() {

    SignUpScreenPreview(navigateToLoginScreen = {})

}