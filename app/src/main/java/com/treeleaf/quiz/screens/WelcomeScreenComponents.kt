package com.treeleaf.quiz.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.treeleaf.quiz.utils.getBitmapFromAssets
import kotlinx.coroutines.flow.StateFlow


@Composable
fun WelcomeScreen(onLetsGetStarted: () -> Unit) {
    val hapticFeedback = LocalHapticFeedback.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        val mainBitmap = LocalContext.current.getBitmapFromAssets("boring_quiz_app.webp")
        Image(mainBitmap.asImageBitmap(), contentDescription = "Boring Quiz app")
        Text(
            text = "Welcome \nto\n Boring Quiz App",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        )
        Button(onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onLetsGetStarted()
        }) {
            Text(text = "Let's get started", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun NameScreen(
    nameState: () -> StateFlow<String>,
    onNameChange: (String) -> Unit,
    onSaveName: () -> Unit,
    onError: (String) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val name by nameState().collectAsState()

    var error by remember {
        mutableStateOf(false)
    }
    val shake by animateFloatAsState(
        targetValue = if (error) -25f else 25f,
        animationSpec = repeatable(
            iterations = 5,
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        finishedListener = {
            error = false
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        AppName()
        CustomCard(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .graphicsLayer {
                    translationX = if (error) shake else 0f
                },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    value = name,
                    onValueChange = onNameChange,
                    textStyle = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    ),
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Enter your name",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }
        Button(onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            error = name.isEmpty()
            if(name.isNotEmpty())
                onSaveName()
            else
                onError("Please enter your name.")
        }) {
            Text(text = "Save", style = MaterialTheme.typography.bodyLarge)
        }
    }
}