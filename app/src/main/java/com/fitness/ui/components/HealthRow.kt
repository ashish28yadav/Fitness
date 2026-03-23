package com.fitness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitness.ui.theme.*

@Composable
fun HealthRow(
    text: String,
    iconBlue: Boolean = false
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(CardSecondary, RoundedCornerShape(18.dp))
            .height(52.dp),
        contentAlignment = androidx.compose.ui.Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = TextPrimary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
