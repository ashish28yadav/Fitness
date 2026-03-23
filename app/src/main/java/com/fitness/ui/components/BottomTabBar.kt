package com.fitness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitness.ui.theme.CardDark
import com.fitness.ui.theme.IOSBlue
import com.fitness.ui.theme.TextSecondary

@Composable
fun BottomTabBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                CardDark.copy(alpha = 0.85f),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Summary",
                    tint = IOSBlue
                )
                Text("Summary", color = IOSBlue)
            }

            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = "Sharing",
                    tint = TextSecondary
                )
                Text("Sharing", color = TextSecondary)
            }
        }
    }
}
