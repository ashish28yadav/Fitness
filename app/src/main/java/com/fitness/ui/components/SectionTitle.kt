package com.fitness.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import com.fitness.ui.theme.TextPrimary

@Composable
fun SectionTitle(
    title: String,
    large: Boolean = false
) {
    Spacer(modifier = Modifier.height(if (large) 24.dp else 12.dp))

    Text(
        text = title,
        color = TextPrimary,
        style = if (large)
            MaterialTheme.typography.headlineMedium
        else
            MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 20.dp)
    )
}
