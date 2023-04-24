package com.example.hikingbook.ui.component

import android.R
import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun OperationButton(clickOK: () -> Unit = {}, clickCancel: () -> Unit = {}) {
    val focusManager = LocalFocusManager.current

    Row(modifier = Modifier.fillMaxWidth()) {

        // cancel Button
        CustomButton(
            text = Resources.getSystem().getString(R.string.cancel),
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            textColor = Color.White
        ) {
            focusManager.clearFocus()
            clickCancel.invoke()
        }

        Spacer(modifier = Modifier.size(32.dp))

        // OK Button
        CustomButton(
            text = Resources.getSystem().getString(R.string.ok),
            modifier = Modifier.weight(1f),
//            color = MaterialTheme.colors.primary
            color = MaterialTheme.colorScheme.primaryContainer,
//            color = if (MaterialTheme.colors.isLight) MaterialTheme.colors.primary else OkButtonColorLight,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            focusManager.clearFocus()
            clickOK.invoke()
        }
    }
}