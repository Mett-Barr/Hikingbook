package com.example.hikingbook.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hikingbook.data.task.Task
import com.example.hikingbook.data.task.TaskObj

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(task: Task) {
    Card(onClick = {  }) {
        Column(modifier = Modifier.padding(16.dp)) {
            task.apply {
                Text(text = title, fontSize = 28.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text(text = description)
                Row() {
                    Text(text = createdDate, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    Text(text = dueDate, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                }
                Text(text = locationCoordinate, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun TaskTest() {
    TaskCard(TaskObj.obj)
}