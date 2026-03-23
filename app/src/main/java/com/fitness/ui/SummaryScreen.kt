package com.fitness.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fitness.ui.theme.*
import com.fitness.ui.components.BottomTabBar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sin

@Composable
fun SummaryScreen(viewModel: StepViewModel = hiltViewModel()) {
    var showStepsDetail by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AnimatedContent(
            targetState = showStepsDetail,
            transitionSpec = {
                if (targetState) {
                    (slideInHorizontally { it } + fadeIn(tween(400)))
                        .togetherWith(slideOutHorizontally { -it / 2 } + fadeOut(tween(400)))
                } else {
                    (slideInHorizontally { -it / 2 } + fadeIn(tween(400)))
                        .togetherWith(slideOutHorizontally { it } + fadeOut(tween(400)))
                }
            },
            label = "ScreenTransition"
        ) { isDetail ->
            if (isDetail) {
                StepsDetailScreen(
                    viewModel = viewModel,
                    onBack = { showStepsDetail = false }
                )
            } else {
                SummaryView(onStepsClick = { showStepsDetail = true })
            }
        }
    }
}

@Composable
fun SummaryView(onStepsClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(GradientStart, GradientMid, GradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Highlights",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            val currentDate = remember { SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date()) }
            Text(text = currentDate, color = TextSecondary, fontSize = 16.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            ActivityRingsSection()
            
            Spacer(modifier = Modifier.height(24.dp))
            StepsHighlightCard(onClick = onStepsClick)
            
            Spacer(modifier = Modifier.height(16.dp))
            ActiveEnergyHighlightCard()
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    HeartRateHighlightCard()
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    SleepHighlightCard()
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            WaterIntakeHighlightCard()

            Spacer(modifier = Modifier.height(24.dp))
            WorkoutsSection()
            
            Spacer(modifier = Modifier.height(100.dp))
        }
        BottomTabBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun ActivityRingsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(22.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(100.dp)) {
            // Move Ring (Orange/Red)
            ActivityRing(
                progress = 0.75f,
                color = Orange,
                strokeWidth = 10.dp,
                modifier = Modifier.fillMaxSize()
            )
            // Exercise Ring (Green)
            ActivityRing(
                progress = 0.6f,
                color = Color(0xFF30D158),
                strokeWidth = 10.dp,
                modifier = Modifier.fillMaxSize().padding(14.dp)
            )
            // Stand Ring (Blue)
            ActivityRing(
                progress = 0.9f,
                color = Color(0xFF00D2FF),
                strokeWidth = 10.dp,
                modifier = Modifier.fillMaxSize().padding(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(24.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ActivityRingLabel(label = "Move", value = "450/600 kcal", color = Orange)
            ActivityRingLabel(label = "Exercise", value = "18/30 min", color = Color(0xFF30D158))
            ActivityRingLabel(label = "Stand", value = "11/12 hr", color = Color(0xFF00D2FF))
        }
    }
}

@Composable
fun ActivityRing(progress: Float, color: Color, strokeWidth: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        animatedProgress.animateTo(progress, tween(1000, easing = FastOutSlowInEasing))
    }
    
    Canvas(modifier = modifier) {
        // Background track
        drawArc(
            color = color.copy(alpha = 0.2f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
        // Foreground progress
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = animatedProgress.value * 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ActivityRingLabel(label: String, value: String, color: Color) {
    Column {
        Text(text = label, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun StepsHighlightCard(onClick: () -> Unit) {
    val todaySteps = 1352
    val averageSteps = 3979

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.DirectionsRun, contentDescription = null, tint = Orange, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Steps",
                    color = Orange,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("● Today", color = Orange)
                    Text(text = "$todaySteps", color = Orange, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text("steps", color = TextSecondary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("● Average", color = GrayLine)
                    Text(text = "$averageSteps", color = GrayLine, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text("steps", color = TextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            StepsLineGraph()
        }
    }
}

@Composable
fun ActiveEnergyHighlightCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(22.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Whatshot, contentDescription = null, tint = Orange, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Active Energy", color = Orange, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "450", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = " kcal", color = TextSecondary, fontSize = 18.sp, modifier = Modifier.padding(bottom = 4.dp))
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(100.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.75f)
                            .clip(CircleShape)
                            .background(Orange)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Moving well today! 75% of your goal.", color = TextSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
fun HeartRateHighlightCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "HeartBeat")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFFF2D55),
                    modifier = Modifier.size(18.dp).graphicsLayer(scaleX = scale, scaleY = scale)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Heart Rate", color = Color(0xFFFF2D55), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "72", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "BPM", color = TextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Normal", color = Color(0xFF30D158), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SleepHighlightCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.NightsStay, contentDescription = null, tint = Color(0xFF5E5CE6), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sleep", color = Color(0xFF5E5CE6), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "7h 20m", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Last night", color = TextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Good quality", color = Color(0xFF5E5CE6), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WaterIntakeHighlightCard() {
    var waterAmount by remember { mutableFloatStateOf(1.2f) }
    val goal = 2.5f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(22.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF00D2FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Hydration", color = Color(0xFF00D2FF), fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Water",
                    tint = TextPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .clickable { waterAmount = (waterAmount + 0.25f).coerceAtMost(goal) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "%.1f".format(waterAmount), color = TextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(text = "/$goal L", color = TextSecondary, fontSize = 18.sp, modifier = Modifier.padding(bottom = 6.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                val animatedProgress by animateFloatAsState(
                    targetValue = waterAmount / goal,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "waterProgress"
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF00D2FF), Color(0xFF3A7BD5))
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun WorkoutsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Workouts", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Show All", color = Orange, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(12.dp))
        WorkoutItem("Outdoor Run", "Today, 8:30 AM", "5.20 km", Icons.AutoMirrored.Filled.DirectionsRun, Orange)
        Spacer(modifier = Modifier.height(12.dp))
        WorkoutItem("Functional Strength Training", "Yesterday", "45 min", Icons.Default.FitnessCenter, Color(0xFF30D158))
    }
}

@Composable
fun WorkoutItem(title: String, time: String, value: String, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(text = time, color = TextSecondary, fontSize = 14.sp)
        }
        Text(text = value, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StepsDetailScreen(viewModel: StepViewModel, onBack: () -> Unit) {
    val selectedRange by viewModel.selectedRange.collectAsState()
    val stepData by viewModel.stepData.collectAsState()
    val totalSteps by viewModel.totalSteps.collectAsState()

    BackHandler(onBack = onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.clickable { onBack() }.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, tint = Orange, modifier = Modifier.size(32.dp))
            Text(text = "Summary", color = Orange, fontSize = 17.sp)
        }
        Text(text = "Steps", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Range Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C1C1E), RoundedCornerShape(10.dp))
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TimeRange.entries.forEach { range ->
                val isSelected = selectedRange == range
                val label = when(range) {
                    TimeRange.D -> "D"
                    TimeRange.W -> "W"
                    TimeRange.M -> "M"
                    TimeRange.SixM -> "6M"
                    TimeRange.Y -> "Y"
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .background(
                            if (isSelected) Color(0xFF636366) else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { viewModel.setRange(range) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        AnimatedContent(
            targetState = totalSteps,
            transitionSpec = {
                (fadeIn(tween(300)) + scaleIn(initialScale = 0.95f))
                    .togetherWith(fadeOut(tween(300)) + scaleOut(targetScale = 0.95f))
            },
            label = "TotalStepsTransition"
        ) { steps ->
            Column {
                Text(text = "TOTAL", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = "%,d".format(steps), color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                    Text(text = " steps", color = Color.Gray, fontSize = 18.sp, modifier = Modifier.padding(bottom = 6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        
        // Smooth transition for the chart itself
        Box(modifier = Modifier.height(190.dp).fillMaxWidth()) {
            key(selectedRange) { // Re-trigger entry animation when range changes
                StepsAnimatedBarChart(data = stepData)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color(0xFF38383A), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Distance Insights", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C1C1E), RoundedCornerShape(14.dp))
                .padding(18.dp)
        ) {
            Column {
                val distanceKm = totalSteps * 0.000762 // Avg stride length 0.762m
                Text(text = "Estimated Distance", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "%.2f km".format(distanceKm), color = Orange, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "These steps are calculated based on your physical activity and motion data.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun StepsAnimatedBarChart(data: List<StepData>) {
    val animatables = remember(data) {
        data.map { Animatable(0f) }
    }

    // Infinite transition for fluid and glass effects
    val infiniteTransition = rememberInfiniteTransition(label = "LiquidGlassEffect")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )
    
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    LaunchedEffect(data) {
        animatables.forEachIndexed { index, animatable ->
            animatable.animateTo(
                targetValue = data[index].value,
                animationSpec = tween(
                    durationMillis = 300, 
                    delayMillis = index * 5,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        val barWidth = size.width / (data.size * 2)
        val cornerRadius = 16.dp.toPx() 

        data.forEachIndexed { index, _ ->
            val animatedValue = animatables[index].value
            val barHeight = animatedValue * size.height
            val barTopLeft = Offset(x = index * barWidth * 2 + barWidth / 2, y = size.height - barHeight)
            val barSize = Size(barWidth, barHeight)

            if (barHeight > 0) {
                // 1. Transparent Glass Container
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.05f),
                    topLeft = barTopLeft,
                    size = barSize,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )

                // 2. Liquid/Water Filling with Wave Effect
                val waterBrush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00D2FF).copy(alpha = 0.7f), // Water Blue
                        Color(0xFF3A7BD5).copy(alpha = 0.9f)  // Deeper Blue
                    ),
                    startY = barTopLeft.y,
                    endY = size.height
                )

                // Draw the water level path to simulate waves/tilt
                val liquidPath = Path().apply {
                    val currentWaveHeight = 4.dp.toPx() * sin(waveOffset + index)
                    moveTo(barTopLeft.x, size.height)
                    lineTo(barTopLeft.x, barTopLeft.y + currentWaveHeight)
                    quadraticBezierTo(
                        barTopLeft.x + barWidth / 2, barTopLeft.y - currentWaveHeight,
                        barTopLeft.x + barWidth, barTopLeft.y + currentWaveHeight
                    )
                    lineTo(barTopLeft.x + barWidth, size.height)
                    close()
                }

                clipPath(liquidPath) {
                    drawRoundRect(
                        brush = waterBrush,
                        topLeft = barTopLeft,
                        size = barSize,
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                }

                // 3. Transparent Glass Edges (moving subtle colors)
                val glassEdgeBrush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = shimmerAlpha),
                        Color.White.copy(alpha = 0.2f)
                    ),
                    start = Offset(barTopLeft.x, barTopLeft.y + (sin(waveOffset) * 20)),
                    end = Offset(barTopLeft.x + barWidth, barTopLeft.y + barHeight)
                )

                drawRoundRect(
                    brush = glassEdgeBrush,
                    topLeft = barTopLeft,
                    size = barSize,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(width = 1.dp.toPx())
                )
                
                // 4. Specular Highlight (The "Glass" shine)
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.4f), Color.Transparent),
                        startY = barTopLeft.y,
                        endY = barTopLeft.y + 20.dp.toPx()
                    ),
                    topLeft = barTopLeft.copy(x = barTopLeft.x + 2.dp.toPx()),
                    size = Size(barWidth - 4.dp.toPx(), barHeight.coerceAtMost(25.dp.toPx())),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
        }

        drawLine(
            color = Color.White.copy(alpha = 0.1f),
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }
}

@Composable
fun StepsLineGraph() {
    val todayData = listOf(0f, 100f, 200f, 400f, 600f, 900f, 1352f)
    val avgData = listOf(200f, 600f, 1000f, 1800f, 2500f, 3200f, 3979f)

    Canvas(
        modifier = Modifier.fillMaxWidth().height(120.dp)
    ) {
        val maxValue = 4000f
        val spacing = size.width / (todayData.size - 1)

        avgData.forEachIndexed { i, value ->
            if (i < avgData.lastIndex) {
                drawLine(
                    color = GrayLine,
                    start = Offset(spacing * i, size.height - (value / maxValue) * size.height),
                    end = Offset(spacing * (i + 1), size.height - (avgData[i + 1] / maxValue) * size.height),
                    strokeWidth = 4f, cap = StrokeCap.Round
                )
            }
        }

        todayData.forEachIndexed { i, value ->
            if (i < todayData.lastIndex) {
                drawLine(
                    color = Orange,
                    start = Offset(spacing * i, size.height - (value / maxValue) * size.height),
                    end = Offset(spacing * (i + 1), size.height - (todayData[i + 1] / maxValue) * size.height),
                    strokeWidth = 5f, cap = StrokeCap.Round
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SummaryScreenPreview() {
    FitnessTheme {
        // Constructing ViewModel inside remember to avoid construction during composition issue
        val viewModel = remember { StepViewModel() }
        SummaryScreen(viewModel = viewModel)
    }
}
