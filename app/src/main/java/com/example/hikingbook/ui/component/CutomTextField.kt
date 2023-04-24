package com.example.hikingbook.ui.component

import android.widget.NumberPicker.OnValueChangeListener
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.CustomTextField(
    modifier: Modifier = Modifier,

    value: String,
    onValueChange: (String) -> Unit,

    label: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,

    clickForOperate: (() -> Unit)? = null

) {

    if (clickForOperate == null) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),

            label = { label?.let { Text(text = it) } },
            trailingIcon = trailingIcon
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        clickForOperate()
                    }
                },
            readOnly = false,

            label = { label?.let { Text(text = it) } },
            trailingIcon = trailingIcon
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.CustomTextField(
    modifier: Modifier = Modifier,

    value: String,
    onValueChange: (String) -> Unit,

    label: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,

    clickForOperate: (() -> Unit)? = null

) {

    if (clickForOperate == null) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.weight(1f),

            label = { label?.let { Text(text = it) } },
            trailingIcon = trailingIcon
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged {
                    if (it.isFocused) {
                        clickForOperate()
                    }
                },
            readOnly = true,

            label = { label?.let { Text(text = it) } },
            trailingIcon = trailingIcon
        )
    }
}