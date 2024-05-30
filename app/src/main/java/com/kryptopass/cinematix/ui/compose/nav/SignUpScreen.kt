package com.kryptopass.cinematix.ui.compose.nav

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kryptopass.cinematix.R
import com.kryptopass.common.nav.NavRoutes

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var checkedState by remember { mutableStateOf(true) }

    val signUpState by viewModel.signUpState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.sign_up), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(stringResource(R.string.full_name_entry)) },
            isError = fullName.isBlank()
        )
        if (fullName.isBlank()) {
            Text(stringResource(R.string.full_name_error), color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email_text_entry)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password_text_entry)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedTextField(
            value = birthday,
            onValueChange = { birthday = it },
            label = { Text(stringResource(R.string.birthday_text_entry)) }
        )
        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text(stringResource(R.string.gender_text_entry)) }
        )
        OutlinedTextField(
            value = zipCode,
            onValueChange = { zipCode = it },
            label = { Text(stringResource(R.string.zip_code_text_entry)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedState,
                onCheckedChange = { checkedState = it },
                modifier = Modifier
                    .height(48.dp)
                    .padding(vertical = 1.5.dp),
            )
            Text(
                stringResource(R.string.subscribe_confirmation),
                modifier = Modifier.padding(end = 16.dp),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(R.string.privacy_policy),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            color = Color.Gray, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                viewModel.signUp(email, password)
            },
            modifier = Modifier
                .width(400.dp)
                .height(48.dp)
                .padding(vertical = 1.5.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black,
                containerColor = Color.Transparent
            )
        ) {
            Text(stringResource(R.string.continue_button))
        }

        if (signUpState) {
            LaunchedEffect(signUpState) {
                navController.navigate(NavRoutes.Movies.route)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}