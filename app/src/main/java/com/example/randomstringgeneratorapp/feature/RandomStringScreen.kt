package com.example.randomstringgeneratorapp.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randomstringgeneratorapp.R
import com.example.randomstringgeneratorapp.models.RandomStringData

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RandomStringScreen() {
    val viewModel = hiltViewModel<RandomStringViewModel>()
    val state: RandomStringScreenState by viewModel.state.collectAsState()

    var inputLength by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = inputLength,
            onValueChange = { inputLength = it },
            label = { Text("Enter max length") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                inputLength.text.toIntOrNull()?.let { viewModel.generateRandomString(it) }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.generate_random_string_title))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // shows delete all when list is not empty
        if (state.randomStrings.isNotEmpty()) {
            Button(
                onClick = { viewModel.deleteAllStrings() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text(text = stringResource(R.string.delete_all_title))
            }
        }

        // Generated list
        LazyColumn {
            itemsIndexed(state.randomStrings) { index, stringData ->
                StringItem(stringData, onDelete = { viewModel.deleteString(index) })
            }
        }
    }
}

@Composable
fun StringItem(data: RandomStringData, onDelete: (RandomStringData) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            Column(
                modifier = Modifier.weight(1f) // Takes up available space
            ) {
                Text(text = stringResource(R.string.generated_string, data.value))
                Text(text = stringResource(R.string.length_string, data.length))
                Text(text = stringResource(R.string.created_string, data.created))
            }

            Image(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "delete",
                modifier = Modifier
                    .size(24.dp) // Set size
                    .clickable { onDelete(data) }
            )
        }
    }
}