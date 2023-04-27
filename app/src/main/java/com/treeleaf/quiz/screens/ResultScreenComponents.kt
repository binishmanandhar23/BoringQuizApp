package com.treeleaf.quiz.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.treeleaf.quiz.R


@Composable
fun ResultScreen(isTimeOut: Boolean = false, name: String, score:Int, totalSize: Int, onRestart: () -> Unit) {
    BackHandler(enabled = true) {
        onRestart()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        ResultHeader(name= name, isTimeOut = isTimeOut)
        ResultCard(score = score, totalSize = totalSize)
        RestartQuizButton(onRestart = onRestart)
    }
}

@Composable
private fun ResultHeader(isTimeOut: Boolean, name:String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = R.mipmap.trophy),
            contentDescription = "Trophy",
            contentScale = ContentScale.Fit
        )
        Text(
            text = "${if(isTimeOut) "Time's out!" else "Well played!"} $name",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun ResultCard(score: Int, totalSize: Int) {
    CustomCard {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Score",
                    tint = Color.Yellow
                )
                Spacer(modifier = Modifier.size(3.dp))
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            ScoreDesign(score = score, totalSize = totalSize)
            Text(
                text = "Your score is $score ${if(score < 2) "point" else "points"}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun ScoreDesign(score: Int, totalSize: Int) {
    var finalScore by remember {
        mutableStateOf(0f)
    }
    LaunchedEffect(key1 = score){
        animate(initialValue = 0f, targetValue = score / totalSize.toFloat(), animationSpec = tween(1500, easing = FastOutSlowInEasing)){ value, _ ->
            finalScore = value
        }
    }
    Box {
        CircularProgressIndicator(
            modifier = Modifier.size(150.dp),
            progress = finalScore,
            color = Color.Yellow,
            strokeWidth = 15.dp,
            strokeCap = StrokeCap.Round
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = score.toString(),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun RestartQuizButton(onRestart:() -> Unit) {
    val hapticFeedback = LocalHapticFeedback.current
    Button(onClick = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        onRestart()
    }, contentPadding = PaddingValues(horizontal = 35.dp, vertical = 12.dp)) {
        Text(
            text = "Take another quiz",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}